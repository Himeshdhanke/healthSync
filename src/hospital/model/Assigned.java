package hospital.model;

public class Assigned {
    private int patientId;
    private int roomId;
    private String patientName;
    private String roomType;
    private int capacity;
    private String availability;

    public Assigned(int patientId, int roomId, String patientName, String roomType, int capacity, String availability) {
        this.patientId = patientId;
        this.roomId = roomId;
        this.patientName = patientName;
        this.roomType = roomType;
        this.capacity = capacity;
        this.availability = availability;
    }

    public int getPatientId() { return patientId; }
    public int getRoomId() { return roomId; }
    public String getPatientName() { return patientName; }
    public String getRoomType() { return roomType; }
    public int getCapacity() { return capacity; }
    public String getAvailability() { return availability; }
}
