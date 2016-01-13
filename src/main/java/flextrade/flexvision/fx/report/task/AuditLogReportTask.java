package flextrade.flexvision.fx.report.task;

import flextrade.flexvision.fx.report.AuditLogReportComposer;
import flextrade.flexvision.fx.report.ReportType;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import flextrade.flexvision.fx.audit.json.AuditLogQuery;
import flextrade.flexvision.fx.audit.pojo.AuditLog;
import flextrade.flexvision.fx.audit.service.AuditLogService;
import flextrade.flexvision.fx.base.service.MailService;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.endpoint.AutoConfigurationReportEndpoint;

import static flextrade.flexvision.fx.report.ReportType.EXCEL;

@Slf4j
public class AuditLogReportTask implements Callable<Path> {

    public static final String[] FILE_HEADER_MAPPING = {"id", "admin user", "operation", "audit date", "remarks"};

    @Getter
    @Setter
    private AuditLogQuery auditLogQuery;

    @Getter
    @Setter
    private MailService mailService;

    @Getter
    @Setter
    private AuditLogService auditLogService;

    @Getter
    @Setter
    private AuditLogReportComposer auditLogReportComposer;

	private ReportType reportType;

    public AuditLogReportTask(AuditLogQuery auditLogQuery, ReportType reportType, AuditLogService auditLogService, MailService mailService,
                              AuditLogReportComposer auditLogReportComposer) {
        this.auditLogQuery = auditLogQuery;
        this.auditLogService = auditLogService;
        this.mailService = mailService;
        this.auditLogReportComposer = auditLogReportComposer;
		this.reportType = reportType;
    }

    @Override
    public Path call() throws Exception {
        log.debug("Starting AuditLogReportTask with {}", auditLogQuery);
        List<AuditLog> auditLogs = auditLogService.get(auditLogQuery);
        Path tempReportPath = auditLogReportComposer.createAuditLog(auditLogs, reportType);
        sendEmail(tempReportPath);
        return tempReportPath;
    }

    private void sendEmail(Path tempCsvPath) {
        mailService.send(Arrays.asList(auditLogQuery.getReplyTo()), null, createAuditLogEmailSubject(), createAuditLogEmailBody(), Arrays.asList(tempCsvPath));
    }

    private String createAuditLogEmailSubject() {
        return "Audit log history";
    }

    private String createAuditLogEmailBody() {
        StringBuilder body = new StringBuilder();
        String lineSeparator = System.lineSeparator();
        body.append("Hi,")
                .append(lineSeparator)
                .append(lineSeparator)
                .append("Attachment is audit log history for ")
                .append(lineSeparator)
                .append(auditLogQuery.toEmailBody());
        return body.toString();
    }
}
