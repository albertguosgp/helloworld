package flextrade.flexvision.fx.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icegreen.greenmail.spring.GreenMailBean;
import com.icegreen.greenmail.util.GreenMail;

import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.mail.internet.MimeMessage;

import cucumber.api.PendingException;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import flextrade.flexvision.fx.audit.json.AuditLogQuery;
import flextrade.flexvision.fx.base.util.MimeMessageParser;

import static flextrade.flexvision.fx.base.service.TimeService.toDate;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.http.HttpStatus.OK;

@WebIntegrationTest
public class AuditLogReportStepDefs extends AbstractSteps {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GreenMailBean greenMailBean;

    private RestTemplate restTemplate = new TestRestTemplate();

    @When("^the client post http post message to \"([^\"]*)\" with maxx user \"([^\"]*)\", start date \"([^\"]*)\", end date is \"([^\"]*)\", and reply to \"([^\"]*)\"$")
    public void the_client_post_http_post_message_to_with_maxx_user_start_date_end_date_is_and_reply_to(String auditLogReportUrl, String maxxUser, String startDate, String endDate, String replyTo) throws Throwable {
        Integer expectedMaxRecord = 1;
        AuditLogQuery auditLogQuery = AuditLogQuery.of(maxxUser, toDate(startDate), toDate(endDate), replyTo, expectedMaxRecord);
        restTemplate.postForEntity(getBaseUrl() + auditLogReportUrl, auditLogQuery, String.class);
    }

    @Then("^the client should receive audit log csv in email account \"([^\"]*)\" after (\\d+) seconds$")
    public void the_client_should_receive_audit_log_csv_in_email_account_after_seconds(String recipientEmailAddress, int timeToWait) throws Throwable {
        Thread.sleep(timeToWait * 1000);

        GreenMail greenMail = greenMailBean.getGreenMail();
        MimeMessage[] receivedEmails = greenMail.getReceivedMessages();

        assertThat(receivedEmails.length, Matchers.equalTo(1));
        receivedEmails[0].saveChanges();

        MimeMessageParser mimeMessageParser = new MimeMessageParser(receivedEmails[0]);
        mimeMessageParser.parse();

        assertThat(mimeMessageParser.getAttachmentList().size(), Matchers.equalTo(1));
        assertThat(mimeMessageParser.getTo().get(0).toString(), Matchers.equalTo(recipientEmailAddress));
    }
}
