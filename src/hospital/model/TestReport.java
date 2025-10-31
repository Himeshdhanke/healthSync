package hospital.model;

import java.time.LocalDate;

public class TestReport {
    private int reportId;
    private int patientId;
    private String patientName;
    private String testType;
    private String result;
    private LocalDate reportDate;
    private String status;

    public TestReport() {};

    public TestReport(int reportId, int patientId, String patientName,
                      String testType, String result, LocalDate reportDate, String status) {
        this.reportId = reportId;
        this.patientId = patientId;
        this.patientName = patientName;
        this.testType = testType;
        this.result = result;
        this.reportDate = reportDate;
        this.status = status;
    }

    // Getters and setters
    public int getReportId() { return reportId; }
    public int getPatientId() { return patientId; }
    public String getPatientName() { return patientName; }
    public String getTestType() { return testType; }
    public String getResult() { return result; }
    public LocalDate getReportDate() { return reportDate; }
    public String getStatus() { return status; }

    public void setReportId(int reportId) { this.reportId = reportId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    public void setTestType(String testType) { this.testType = testType; }
    public void setResult(String result) { this.result = result; }
    public void setReportDate(LocalDate reportDate) { this.reportDate = reportDate; }
    public void setStatus(String status) { this.status = status; }
}
