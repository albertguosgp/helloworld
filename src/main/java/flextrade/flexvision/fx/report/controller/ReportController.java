package flextrade.flexvision.fx.report.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import flextrade.flexvision.fx.audit.json.AuditLogQuery;
import flextrade.flexvision.fx.audit.service.AuditLogService;
import flextrade.flexvision.fx.base.service.TaskRunner;
import flextrade.flexvision.fx.report.task.AuditLogReportTask;

@RestController
public class ReportController {

    @Autowired
    private TaskRunner taskRunner;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private AuditLogService auditLogService;

    @RequestMapping(value = "/report", method = RequestMethod.POST)
    public void publishAuditLogReport(@RequestBody AuditLogQuery auditLogQuery) {
        taskRunner.submit(new AuditLogReportTask(auditLogQuery, auditLogService, mailSender));
    }
}
