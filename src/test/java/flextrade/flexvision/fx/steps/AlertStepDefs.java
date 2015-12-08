package flextrade.flexvision.fx.steps;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import flextrade.flexvision.fx.alert.pojo.Alert;
import flextrade.flexvision.fx.alert.pojo.AlertPriceSide;
import flextrade.flexvision.fx.alert.pojo.AlertStatus;
import flextrade.flexvision.fx.alert.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static flextrade.flexvision.fx.base.service.TimeService.toISO8601Format;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static flextrade.flexvision.fx.base.service.TimeService.toDate;

@WebIntegrationTest
public class AlertStepDefs extends AbstractSteps {
    private Alert alert;
    private ResponseEntity<String> httpPostResponse;
    private String httpGetResponse;

    @Autowired
    private AlertService alertService;

    @Autowired
    private ObjectMapper objectMapper;

    private RestTemplate restTemplate = new TestRestTemplate();

    @Given("^maxx user is \"([^\"]*)\", status is \"([^\"]*)\", symbol is \"([^\"]*)\", side is \"([^\"]*)\", price is \"([^\"]*)\", "
        + "firm is \"([^\"]*)\", delivery is \"([^\"]*)\", created is \"([^\"]*)\", expired is \"([^\"]*)\"$")
    public void maxx_user_is_status_is_side_is_price_is_currency_is_firm_is_delivery_is(String user,
        String status, String symbol, String side, String price, String firm, String delivery, String created, String expired)
        throws ParseException {
        alert = new Alert();
        alert.setMaxxUser(user);
        alert.setStatus(AlertStatus.valueOf(status));
        alert.setSymbol(symbol);
        alert.setSide(AlertPriceSide.valueOf(side));
        alert.setPrice(price);
        alert.setFirm(firm);
        alert.setDelivery(delivery);
        alert.setCreated(toDate(created));
        alert.setExpired(toDate(expired));
    }

    @When("^the alert client post http post message to Maxx restful service$")
    public void the_alert_client_post_http_post_message_to_Maxx_restful_service_alert() {
        httpPostResponse = restTemplate.postForEntity(getBaseUrl() + "/alert", alert, String.class);
    }


    @Then("^the alert client receives http status code of (\\d+) and Json body with \"([^\"]*)\"$")
    public void the_alert_client_receives_http_status_code_of_and_Json_body_with(int httpStatusCode,
        String keyword) {
        assertThat(httpPostResponse.getStatusCode().value(), is(httpStatusCode));
        assertThat(httpPostResponse.getBody(), containsString(keyword));
    }

    @And("^the Maxx restful service database saved alert with Maxx user \"([^\"]*)\", status is \"([^\"]*)\", side is \"([^\"]*)\", price is \"([^\"]*)\" and currency is \"([^\"]*)\"$")
    public void the_Maxx_restful_service_database_saved_alert_with_Maxx_user_status_is_side_is_price_is_and_currency_is(
        String user, String status, String side, String price, String symbol) throws Throwable {
        List<Alert> alerts = alertService.findAll();

        assertEquals(1, alerts.size());
        alert = alerts.get(0);
        assertEquals(user, alert.getMaxxUser());
        assertEquals(status, alert.getStatus().name());
        assertEquals(side, alert.getSide().name());
        assertEquals(price, alert.getPrice());
        assertEquals(symbol, alert.getSymbol());
    }

    @When("^the alert client fire http get message to \"([^\"]*)\" with Maxx user \"([^\"]*)\"$")
    public void the_alert_client_fire_http_get_message_to_with_Maxx_user(String alertUrl, String maxxUser)
        throws Throwable {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getBaseUrl() + alertUrl)
            .queryParam("maxxUser", maxxUser);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        HttpEntity<String> response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
        httpGetResponse = response.getBody();
    }

    @When("^the alert client fire http POST message to \"([^\"]*)\" with the new status is \"([^\"]*)\", trigger time is \"([^\"]*)\"$")
    public void the_alert_client_fire_http_POST_message_to_with_the_new_status_is_trigger_time_is(
        String url, String newStatus, String triggerTime) throws ParseException {
        alert.setStatus(AlertStatus.TRIGGERED);
        alert.setTriggered(toDate(triggerTime));
        httpPostResponse = restTemplate.postForEntity(getBaseUrl() + "/alert", alert, String.class);
    }

    @Then("^the alert server response should be maxx user is \"([^\"]*)\", status is \"([^\"]*)\", side is \"([^\"]*)\", price is \"([^\"]*)\", currency is \"([^\"]*)\" and trigger time is \"([^\"]*)\"$")
    public void the_alert_server_response_should_be_maxx_user_is_status_is_side_is_price_is_currency_is_and_trigger_time_is(
        String user, String status, String side, String price, String symbol, String triggerTime) throws Throwable {
        List<Alert> alertsFromServerResponse = objectMapper.readValue(httpGetResponse, new TypeReference<List<Alert>>() {});

        assertEquals(1, alertsFromServerResponse.size());
        Alert alertFromServerResponse = alertsFromServerResponse.get(0);
        assertEquals(user, alertFromServerResponse.getMaxxUser());
        assertEquals(status, alertFromServerResponse.getStatus().name());
        assertEquals(side, alertFromServerResponse.getSide().name());
        assertEquals(price, alertFromServerResponse.getPrice());
        assertEquals(symbol, alertFromServerResponse.getSymbol());
        assertEquals(triggerTime, toISO8601Format(alertFromServerResponse.getTriggered()));
    }

    @When("^the alert client fire http DELETE to delete the current alert$")
    public void the_alert_client_fire_http_DELETE_to_to_delete_the_current_alert() {
        restTemplate.delete(getBaseUrl() + "/alert/" + alert.getId());
    }

    @Then("^the alert server response should be empty$")
    public void the_alert_server_response_should_be_empty() throws IOException {
        List<Alert> alertsFromServerResponse =
            objectMapper.readValue(httpGetResponse, new TypeReference<List<Alert>>() {});

        assertEquals(0, alertsFromServerResponse.size());
    }
}
