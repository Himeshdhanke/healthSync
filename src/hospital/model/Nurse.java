package hospital.model;

public class Nurse {
    private int eId;
    private String name;
    private double salary;
    private String sex;
    private String mobNo;
    private String address;
    private String state;
    private String city;
    private String pinNo;

    // Getters and setters
    public int geteId() { return eId; }
    public void seteId(int eId) { this.eId = eId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }

    public String getMobNo() { return mobNo; }
    public void setMobNo(String mobNo) { this.mobNo = mobNo; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getPinNo() { return pinNo; }
    public void setPinNo(String pinNo) { this.pinNo = pinNo; }
}
