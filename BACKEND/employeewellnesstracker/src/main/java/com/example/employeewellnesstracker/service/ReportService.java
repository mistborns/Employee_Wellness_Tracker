package com.example.employeewellnesstracker.service;

import com.example.employeewellnesstracker.model.Report;
import com.example.employeewellnesstracker.model.SurveyResponse;
import com.example.employeewellnesstracker.repository.ReportRepository;
import com.example.employeewellnesstracker.repository.SurveyResponseRepository;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private SurveyResponseRepository surveyResponseRepository;

    @Autowired
    private ReportRepository reportRepository;

    public Report generateReport(String adminName, String department, String location, LocalDateTime startDate, LocalDateTime endDate) {
        List<SurveyResponse> responses = surveyResponseRepository.findAll().stream()
                .filter(response -> {
                    boolean departmentMatch = (department == null || department.isEmpty() ||
                            (response.getEmployee() != null && department.equalsIgnoreCase(response.getEmployee().getDepartment())));

                    boolean locationMatch = (location == null || location.isEmpty() ||
                            (response.getEmployee() != null && (response.getEmployee().getLocation() == null || location.equalsIgnoreCase(response.getEmployee().getLocation()))));

                    boolean dateMatch = (startDate == null || response.getSubmittedAt().isAfter(startDate) || response.getSubmittedAt().isEqual(startDate)) &&
                            (endDate == null || response.getSubmittedAt().isBefore(endDate) || response.getSubmittedAt().isEqual(endDate));

                    return departmentMatch && locationMatch && dateMatch;
                })
                .toList();

        // Convert responses into CSV format (for simplicity, storing as String)
        StringBuilder reportContent = new StringBuilder("Employee,Survey,Response,SubmittedAt\n");
        for (SurveyResponse response : responses) {
            reportContent.append(String.join(",",
                            response.getEmployee().getName(),
                            response.getSurvey().getTitle(),
                            response.getResponse(),
                            response.getSubmittedAt().toString()))
                    .append("\n");
        }

        // Create and save report entity
        Report report = new Report();
        report.setReportName("Employee Wellness Report");
        report.setGeneratedBy(adminName);
        report.setDepartmentFilter(department);
        report.setLocationFilter(location);
        report.setStartDate(startDate);
        report.setEndDate(endDate);
        report.setReportData(reportContent.toString());

        return reportRepository.save(report);
    }


    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public Report getReportById(Long id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));
    }

    // Export CSV
    public String exportReportAsCSV(Long reportId) {
        Report report = getReportById(reportId);
        return report.getReportData();
    }

    // Export PDF
    public byte[] exportReportAsPDF(Long reportId) {
        Report report = getReportById(reportId);

        if (report.getReportData() == null || report.getReportData().isEmpty()) {
            throw new RuntimeException("Report data is empty, cannot generate PDF");
        }

        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDType1Font font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
            PDType1Font boldFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(boldFont, 14);
                contentStream.beginText();
                contentStream.newLineAtOffset(200, 750);
                contentStream.showText("Employee Wellness Report");
                contentStream.endText();

                // Table properties
                float margin = 50;
                float yStart = 720;
                float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
                float rowHeight = 20;
                float tableYPosition = yStart;
                float[] columnWidths = {100, 100, 150, 100};

                // Column headers
                String[] headers = {"Employee Name", "Survey", "Response", "Submitted At"};

                // Draw table headers
                contentStream.setFont(boldFont, 10);
                float xPosition = margin;
                for (int i = 0; i < headers.length; i++) {
                    contentStream.beginText();
                    contentStream.newLineAtOffset(xPosition, tableYPosition);
                    contentStream.showText(headers[i]);
                    contentStream.endText();
                    xPosition += columnWidths[i];
                }

                tableYPosition -= rowHeight;

                // Draw responses
                contentStream.setFont(font, 10);
                for (String line : report.getReportData().split("\n")) {
                    String[] row = line.split(",");

                    if (row.length == 4) { // Ensure correct data format
                        xPosition = margin;
                        for (int i = 0; i < row.length; i++) {
                            contentStream.beginText();
                            contentStream.newLineAtOffset(xPosition, tableYPosition);
                            contentStream.showText(row[i]);
                            contentStream.endText();
                            xPosition += columnWidths[i];
                        }
                        tableYPosition -= rowHeight;
                    }

                    if (tableYPosition < 50) break; // Prevent text from going out of bounds
                }
            }

            document.save(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF report", e);
        }
    }


}
