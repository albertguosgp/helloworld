package flextrade.flexvision.fx.report;

import flextrade.flexvision.fx.audit.pojo.AuditLog;
import flextrade.flexvision.fx.base.service.TimeService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

@Service
@Slf4j
public class AuditLogReportComposer {

    public static final String[] FILE_HEADER_MAPPING = {"id", "admin user", "operation", "audit date", "remarks"};

    @Setter
    @Autowired
    private TimeService timeService;

    public Path createAuditLog(List<AuditLog> auditLogs, ReportType reportType) throws IOException {
        Objects.requireNonNull(reportType, "Audit log report type must be provided");
        switch (reportType) {
            case CSV:
                return createCsvReport(auditLogs);
            case EXCEL:
                return createExcelReport(auditLogs);
            default:
                throw new IllegalArgumentException("Unknown report type");
        }
    }

    private Path createExcelReport(List<AuditLog> auditLogs) throws IOException {
		Workbook workbook = createNewWorkBook();
		Sheet auditLogSheet = createAuditLogSheet(workbook);
		Map<String, CellStyle> styles = createStyles(workbook);

		initWorkBookSheetStyle(auditLogSheet);
        createAuditLogSheetTitleRow(auditLogSheet, styles);
        createAuditLogHeaderRow(auditLogSheet, styles);
        createAuditLogCell(auditLogs, auditLogSheet, styles);

		auditLogSheet.autoSizeColumn(0);
		auditLogSheet.autoSizeColumn(1);
		auditLogSheet.autoSizeColumn(2);
		auditLogSheet.autoSizeColumn(3);

		return createExcelFile(workbook);
	}

	private Path createExcelFile(Workbook workbook) throws IOException {
		Path xlsx = Files.createTempFile("audit-log-", ".xlsx");
		workbook.write(new BufferedOutputStream(Files.newOutputStream(xlsx, StandardOpenOption.WRITE)));
		workbook.close();

		return xlsx;
	}

	private void initWorkBookSheetStyle(Sheet auditLogSheet) {
		PrintSetup printSetup = auditLogSheet.getPrintSetup();
		printSetup.setLandscape(true);
		auditLogSheet.setFitToPage(true);
		auditLogSheet.setHorizontallyCenter(true);
	}

	private Sheet createAuditLogSheet(Workbook workbook) {
		return workbook.createSheet("Audit Log");
	}

	private Workbook createNewWorkBook() {
		return new XSSFWorkbook();
	}

	private void createAuditLogCell(List<AuditLog> auditLogs, Sheet auditLogSheet, Map<String, CellStyle> styles) {
        int rowNumStart = 2;
        for (AuditLog auditLog : auditLogs) {
            Row row = auditLogSheet.createRow(rowNumStart++);
            cell(row, 0, auditLog.getId().toString(), styles);
            cell(row, 1, auditLog.getMaxxUser(), styles);
            cell(row, 2, auditLog.getOperation(), styles);
            cell(row, 3, timeService.displayInPreferredTimezone(auditLog.getAuditDate()), styles);
            cell(row, 4, auditLog.getRemarks(), styles);
        }
    }

    private void createAuditLogSheetTitleRow(Sheet sheet, Map<String, CellStyle> styles) {
        Row titleRow = sheet.createRow(0);
        titleRow.setHeightInPoints(45);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Audit log report");
        titleCell.setCellStyle(styles.get("title"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$E$1"));
    }

    private void cell(Row row, int column, String cellValue, Map<String, CellStyle> styles) {
        Cell cell = row.createCell(column);
        cell.setCellStyle(styles.get("cell"));
        cell.setCellValue(cellValue);
    }


    private void createAuditLogHeaderRow(Sheet sheet, Map<String, CellStyle> styles) {
        Row headerRow = sheet.createRow(1);
        headerRow.setHeightInPoints(40);
        List<String> headers = Arrays.asList(FILE_HEADER_MAPPING);
        for (int i = 0; i < headers.size(); ++i) {
            Cell headerCell = headerRow.createCell(i);
            headerCell.setCellValue(headers.get(i));
            headerCell.setCellStyle(styles.get("header"));
        }
    }

    private static Map<String, CellStyle> createStyles(Workbook wb) {
        Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
        CellStyle style;
        Font titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short) 18);
        titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setFont(titleFont);
        styles.put("title", style);

        Font monthFont = wb.createFont();
        monthFont.setFontHeightInPoints((short) 11);
        monthFont.setColor(IndexedColors.WHITE.getIndex());
        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFont(monthFont);
        style.setWrapText(true);
        styles.put("header", style);

        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setWrapText(true);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        styles.put("cell", style);

        return styles;
    }

    private Path createCsvReport(List<AuditLog> auditLogs) {
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
}
