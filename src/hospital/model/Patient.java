package hospital.model;

import java.time.LocalDate;

public class Patient {

    private int pId;           // ✅ Matches column P_ID in DB
    private String name;
    private int age;
    private String gender;
    private LocalDate dob;
    private String mobNo;

    // ✅ Constructor without pId — for new patients (ID will be assigned later)
    public Patient(String name, int age, String gender, LocalDate dob, String mobNo) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.dob = dob;
        this.mobNo = mobNo;
    }


    // ✅ Constructor with all fields
    public Patient(int pId, String name, int age, String gender, LocalDate dob, String mobNo) {
        this.pId = pId;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.dob = dob;
        this.mobNo = mobNo;
    }

    // ✅ No-args constructor (useful for FX / serialization)
    public Patient() {}

    // ✅ Getters and Setters
    public int getPId() {
        return pId;
    }

    public void setPId(int pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getMobNo() {
        return mobNo;
    }

    public void setMobNo(String mobNo) {
        this.mobNo = mobNo;
    }

    @Override
    public String toString() {
        // ✅ Helps ComboBox/TableView display patient name instead of object hash
        return name + " (ID: " + pId + ")";
    }
}
