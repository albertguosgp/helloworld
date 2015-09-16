package flextrade.flexvision.fx.position;

import com.google.common.collect.ImmutableMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import flextrade.flexvision.fx.base.service.MailService;
import flextrade.flexvision.fx.base.service.TimeService;
import lombok.extern.slf4j.Slf4j;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
public class PositionRollOverReminderEmailTask extends QuartzJobBean {

	private static final long RETRY_INTERVAL_IN_MILLISECOND = 30 * 1000;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		log.info("Staring check position roll over status");
		try {
			JobDataMap jobDataMap = context.getMergedJobDataMap();

			ObjectMapper objectMapper = (ObjectMapper) jobDataMap.get("objectMapper");
			String rollOverUrl = (String) jobDataMap.get("rollOverUrl");
			MailService mailService = (MailService) jobDataMap.get("mailService");
			RestTemplate restTemplate = (RestTemplate) jobDataMap.get("restTemplate");
			TimeService timeService = (TimeService) jobDataMap.get("timeService");
			String rollOverSupportEmail = (String)jobDataMap.get("rollOverSupportEmail");

			if (isNotRolledOver(objectMapper, restTemplate, rollOverUrl, timeService)) {
				sendEmailReminder(mailService, timeService, rollOverSupportEmail);
			}
		} catch (Exception e) {
			log.error("Failed to get response from backend roll over service", e);
			throw createJobExecutionExceptionForRetry(e);
		}
	}

	private JobExecutionException createJobExecutionExceptionForRetry(Exception e) {
		JobExecutionException jobExecutionException = new JobExecutionException(e);
		try {
			Thread.sleep(RETRY_INTERVAL_IN_MILLISECOND);
		} catch (InterruptedException ignored) {
		}
		jobExecutionException.setRefireImmediately(true);
		return jobExecutionException;
	}

	private void sendEmailReminder(MailService mailService, TimeService timeService, String rollOverSupportEmail) {
		log.info("Roll over is not performed and sending reminder email");
		mailService.send(Arrays.asList(rollOverSupportEmail), null, createReminderEmailSubject(), createReminderEmailBody(timeService), null);
		log.info("Roll over reminder sent successfully");
	}

	private boolean isNotRolledOver(ObjectMapper objectMapper, RestTemplate restTemplate, String rollOverUrl, TimeService timeService) throws IOException {
		JsonNode responseJsonNode = objectMapper.readTree(restTemplate.getForObject(rollOverUrl, String.class, ImmutableMap.of("valueDate", timeService.valueDate())));
		JsonNode status = responseJsonNode.findValue("status");
		return false;
	}

	private String createReminderEmailSubject() {
		return "Position is not rolled over yet";
	}

	private String createReminderEmailBody(TimeService timeService) {
		StringBuilder reminderEmailBody = new StringBuilder();
		reminderEmailBody.append("This is reminder email.")
				.append(System.lineSeparator())
				.append("Position for ").append(timeService.valueDate())
				.append(" is not rolled over yet. Please click the roll over button in Admin Monitor once you are ready to roll over the positions");
		return reminderEmailBody.toString();
	}
}
