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
}
