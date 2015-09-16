package flextrade.flexvision.fx.report.task;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.mail.MailSender;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Callable;

import flextrade.flexvision.fx.audit.json.AuditLogQuery;
import flextrade.flexvision.fx.audit.pojo.AuditLog;
import flextrade.flexvision.fx.audit.service.AuditLogService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import static flextrade.flexvision.fx.base.service.TimeService.toISO8601Format;

@Slf4j
public class AuditLogReportTask implements Callable<Path> {

    public static final String[] FILE_HEADER_MAPPING = {"id", "maxx user", "operation", "audit date", "remarks"};

    @Getter
    @Setter
    private AuditLogQuery auditLogQuery;

    @Getter
    @Setter
    private MailSender mailSender;

    @Getter
    @Setter
    private AuditLogService auditLogService;

    public AuditLogReportTask(AuditLogQuery auditLogQuery, AuditLogService auditLogService, MailSender mailSender) {
        this.auditLogQuery = auditLogQuery;
        this.auditLogService = auditLogService;
        this.mailSender = mailSender;
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
    }

    private Path saveAuditLogsToCsv(List<AuditLog> auditLogs) {
        FileWriter fileWriter = null;
        Path tempCsvPath = null;
        try {
            tempCsvPath = createTempFile();
            fileWriter = new FileWriter(tempCsvPath.toFile());
            printToCsv(auditLogs, fileWriter, tempCsvPath);
        } catch (IOException e) {
            log.error("Failed to create temporary audit log csv ");
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException ignored) {
                }
            }
            return tempCsvPath;
        }
    }

    private void printToCsv(List<AuditLog> auditLogs, FileWriter fileWriter, Path tempCsvPath) throws IOException {
        CSVPrinter printer = new CSVPrinter(fileWriter, createAuditLogsCsvHeaderFormat());
        for (AuditLog auditLog : auditLogs) {
            printer.printRecord(auditLog.getId(), auditLog.getMaxxUser(), auditLog.getOperation(), toISO8601Format(auditLog.getAuditDate()), auditLog.getRemarks());
        }
        log.info("Audit log CSV {} created successfully", tempCsvPath.toUri());
    }

    private Path createTempFile() throws IOException {
        return Files.createTempFile("audit-log-", ".csv");
    }

    private CSVFormat createAuditLogsCsvHeaderFormat() {
        return CSVFormat.DEFAULT.withHeader(FILE_HEADER_MAPPING);
    }
}
