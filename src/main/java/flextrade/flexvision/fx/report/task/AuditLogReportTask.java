package flextrade.flexvision.fx.report.task;

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
import flextrade.flexvision.fx.base.service.TimeService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

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
    private TimeService timeService;

    public AuditLogReportTask(AuditLogQuery auditLogQuery, AuditLogService auditLogService, MailService mailService, TimeService timeService) {
        this.auditLogQuery = auditLogQuery;
        this.auditLogService = auditLogService;
        this.mailService = mailService;
        this.timeService = timeService;
    }

    @Override
    public Path call() throws Exception {
        log.debug("Starting AuditLogReportTask with {}", auditLogQuery);
        List<AuditLog> auditLogs = auditLogService.get(auditLogQuery);
        Path tempCsvPath = saveAuditLogsToCsv(auditLogs);
        sendEmail(tempCsvPath);
        return tempCsvPath;
    }

    private void sendEmail(Path tempCsvPath) {
        mailService.send(Arrays.asList(auditLogQuery.getReplyTo()), null, createAuditLogEmailSubject(), createAuditLogEmailBody(), Arrays.asList(tempCsvPath));
    }

    private Path saveAuditLogsToCsv(List<AuditLog> auditLogs) {
        try {
            Path tempCsvPath = createTempFile();
            printToCsv(auditLogs, tempCsvPath);
            return tempCsvPath;
        } catch (IOException e) {
            log.error("Failed to create temporary audit log csv", e);
            throw new RuntimeException("Failed to create temporary audit log csv");
        }
    }

    private void printToCsv(List<AuditLog> auditLogs, Path tempCsvPath) throws IOException {
        BufferedWriter fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempCsvPath.toFile()), "UTF-8"));
        CSVPrinter printer = new CSVPrinter(fileWriter, createAuditLogsCsvHeaderFormat());
        for (AuditLog auditLog : auditLogs) {
            printer.printRecord(auditLog.getId(), auditLog.getMaxxUser(), auditLog.getOperation(), timeService.displayInPreferredTimezone(auditLog.getAuditDate()), auditLog.getRemarks());
        }
        printer.flush();
        printer.close();
        log.info("Audit log CSV {} created successfully", tempCsvPath.toUri());
    }

    private Path createTempFile() throws IOException {
        return Files.createTempFile("audit-log-", ".csv");
    }

    private CSVFormat createAuditLogsCsvHeaderFormat() {
        return CSVFormat.DEFAULT.withHeader(FILE_HEADER_MAPPING);
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
                .append(auditLogQuery.convertToEmailMessage());
        return body.toString();
    }
}
