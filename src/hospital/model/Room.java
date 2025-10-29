package hospital.model;

public class Room {
    private int rId;
    private String type;
    private int capacity;
    private String availability;

    public Room(int rId, String type, int capacity, String availability) {
        this.rId = rId;
        this.type = type;
        this.capacity = capacity;
        this.availability = availability;
    }

    public int getRId() { return rId; }
    public void setRId(int rId) { this.rId = rId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }
}
