package hospital.model;
import java.time.LocalDate;

public class Patient {
    private int id;             // P_ID
    private String name;        // Name
    private int age;            // Age
    private String gender;      // Gender
    private LocalDate dob;      // DOB
    private String mobNo;       // Mob_No

    public Patient() {
    }

    public Patient(int id, String name, int age, String gender, LocalDate dob, String mobNo) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.dob = dob;
        this.mobNo = mobNo;
    }

    public Patient(String name, int age, String gender, LocalDate dob, String mobNo) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.dob = dob;
        this.mobNo = mobNo;
    }
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }
    public String getMobNo() { return mobNo; }
    public void setMobNo(String mobNo) { this.mobNo = mobNo; }
}

