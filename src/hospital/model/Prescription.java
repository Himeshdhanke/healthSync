package hospital.model;

import java.sql.Timestamp;

public class Prescription {
    private int id;
    private int patientId;
    private String doctorName;
    private String prescription;
    private Timestamp date;

    // Constructor for new prescriptions (before saving)
    public Prescription(int patientId, String doctorName, String prescription) {
        this.patientId = patientId;
        this.doctorName = doctorName;
        this.prescription = prescription;
    }

    // Constructor for retrieving from DB
    public Prescription(int id, int patientId, String doctorName, String prescription, Timestamp date) {
        this.id = id;
        this.patientId = patientId;
        this.doctorName = doctorName;
        this.prescription = prescription;
        this.date = date;
    }

    // Getters and setters
    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public String getDoctorName() { return doctorName; }
    public String getPrescription() { return prescription; }
    public Timestamp getDate() { return date; }

    public void setPrescription(String prescription) { this.prescription = prescription; }
}
