package flextrade.flexvision.fx.steps;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import flextrade.flexvision.fx.audit.pojo.AuditLog;
import flextrade.flexvision.fx.audit.service.AuditLogService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@WebIntegrationTest
public class AuditLogStepDefs extends AbstractSteps {
    public static final String AUDIT_LOG_URL = "/auditlog";

    private AuditLog auditLog;

    private ResponseEntity<String> httpPostResponse;

    private String httpGetResponse;

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private ObjectMapper objectMapper;

    private RestTemplate restTemplate = new TestRestTemplate();

    @Given("^maxx user is \"([^\"]*)\", operation is \"([^\"]*)\", audit time is at \"([^\"]*)\", and remark is \"([^\"]*)\"$")
    public void maxx_user_is_operation_is_audit_time_is_at_and_remark_is(String maxxUser, String operation, String auditDate, String remarks) throws Throwable {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        auditLog = new AuditLog();
        auditLog.setMaxxUser(maxxUser);
        auditLog.setOperation(operation);
        auditLog.setRemarks(remarks);
        auditLog.setAuditDate(dateFormatter.parse(auditDate));
    }

    @Then("^the client receives status code of (\\d+)$")
    public void the_client_receives_status_code_of(int httpStatusCode) throws Throwable {
        assertThat(httpStatusCode, Matchers.is(httpPostResponse.getStatusCode().value()));
    }

    @When("^the client call Maxx restful service to get audit log with Maxx user \"([^\"]*)\", start date \"([^\"]*)\", end date \"([^\"]*)\"$")
    public void the_client_call_Maxx_restful_service_to_get_audit_log_with_Maxx_user_start_date_end_date(String maxxUser, String startDate, String endDate) throws Throwable {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXX");
        Map<String, Object> urlVariables = new HashMap<>();
        urlVariables.put("maxxUser", maxxUser);
        urlVariables.put("startDate", dateFormatter.parse(startDate));
        urlVariables.put("endDate", dateFormatter.parse(endDate));

        httpGetResponse = restTemplate.getForObject(getBaseUrl() + AUDIT_LOG_URL, String.class, urlVariables);
    }

    @Then("^the audit log server response should be maxx user is \"([^\"]*)\", operation is \"([^\"]*)\", audit time is at \"([^\"]*)\", and remark is \"([^\"]*)\"$")
    public void the_audit_log_server_response_should_be_maxx_user_is_operation_is_audit_time_is_at_and_remark_is(String expectedMaxxUser, String ExpectedOperation, String expectedAuditDate, String expectedRemark) throws Throwable {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXX");
        List<AuditLog> auditLogsFromServerResponse = objectMapper.readValue(httpGetResponse, new TypeReference<List<AuditLog>>() {
        });

        assertEquals(1, auditLogsFromServerResponse.size());
        AuditLog auditLogFromServerResponse = auditLogsFromServerResponse.get(0);
        assertEquals(expectedMaxxUser, auditLogFromServerResponse.getMaxxUser());
        assertEquals(ExpectedOperation, auditLogFromServerResponse.getOperation());
        assertEquals(expectedRemark, auditLogFromServerResponse.getRemarks());
        assertEquals(dateFormatter.parse(expectedAuditDate), auditLogFromServerResponse.getAuditDate());
    }

    @And("^the Maxx restful service database save audit log with Maxx user \"([^\"]*)\", operation is \"([^\"]*)\", audit time is at \"([^\"]*)\", and remark is \"([^\"]*)\"$")
    public void the_Maxx_restful_service_database_save_audit_log_with_Maxx_user_operation_is_audit_time_is_at_and_remark_is(String expectedMaxxUser, String expectedOperation, String expectedAuditDate, String expectedRemark) throws Throwable {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXX");
        List<AuditLog> auditLogs = auditLogService.findAll();

        assertEquals(1, auditLogs.size());
        AuditLog auditLog = auditLogs.get(0);
        assertEquals(expectedMaxxUser, auditLog.getMaxxUser());
        assertEquals(expectedOperation, auditLog.getOperation());
        assertEquals(expectedRemark, auditLog.getRemarks());
        assertEquals(dateFormatter.parse(expectedAuditDate), auditLog.getAuditDate());
    }

    @When("^the client post http post message to Maxx restful service$")
    public void the_client_post_http_post_message_to_Maxx_restful_service() throws Throwable {
        httpPostResponse = restTemplate.postForEntity(getBaseUrl() + AUDIT_LOG_URL, auditLog, String.class);
    }
}
