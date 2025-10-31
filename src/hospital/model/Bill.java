package hospital.model;

public class Bill {
    private int billId;
    private int patientId;
    private String patientName;
    private double amount;
    private String billDate;
    private String status;

    public Bill(int billId, int patientId, String patientName, double amount, String billDate, String status) {
        this.billId = billId;
        this.patientId = patientId;
        this.patientName = patientName;
        this.amount = amount;
        this.billDate = billDate;
        this.status = status;
    }

    public Bill(int patientId, String patientName, double amount, String billDate, String status) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.amount = amount;
        this.billDate = billDate;
        this.status = status;
    }

    public int getBillId() { return billId; }
    public int getPatientId() { return patientId; }
    public String getPatientName() { return patientName; }
    public double getAmount() { return amount; }
    public String getBillDate() { return billDate; }
    public String getStatus() { return status; }

    public void setBillId(int billId) { this.billId = billId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setBillDate(String billDate) { this.billDate = billDate; }
    public void setStatus(String status) { this.status = status; }
}
