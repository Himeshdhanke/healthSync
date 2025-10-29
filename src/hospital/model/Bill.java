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

    public int getBillId() { return billId; }
    public int getPatientId() { return patientId; }
    public String getPatientName() { return patientName; }
    public double getAmount() { return amount; }
    public String getBillDate() { return billDate; }
    public String getStatus() { return status; }
}
