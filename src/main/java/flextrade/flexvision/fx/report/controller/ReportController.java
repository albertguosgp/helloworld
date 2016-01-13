package flextrade.flexvision.fx.report.controller;

import flextrade.flexvision.fx.report.AuditLogReportComposer;
import flextrade.flexvision.fx.report.ReportType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import flextrade.flexvision.fx.audit.json.AuditLogQuery;
import flextrade.flexvision.fx.audit.service.AuditLogService;
import flextrade.flexvision.fx.base.service.MailService;
import flextrade.flexvision.fx.base.service.TimeService;
import flextrade.flexvision.fx.report.task.AuditLogReportTask;

@RestController
public class ReportController {

    @Autowired
    private AsyncTaskExecutor asyncTaskExecutor;

    @Autowired
    private MailService mailService;

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private AuditLogReportComposer auditLogReportComposer;

    @RequestMapping(value = "/report/auditlog", method = RequestMethod.POST)
    public void publishAuditLogReport(@RequestBody AuditLogQuery auditLogQuery) {
        asyncTaskExecutor.submit(new AuditLogReportTask(auditLogQuery, ReportType.EXCEL, auditLogService, mailService, auditLogReportComposer));
    }
}
