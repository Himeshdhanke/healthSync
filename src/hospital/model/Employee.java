package hospital.model;

public class Employee {
    private int eId;
    private String name;
    private String role;
    private String dept;
    private String qualification;
    private double salary;
    private String sex;
    private String mobNo;
    private String address;
    private String state;
    private String city;
    private String pinNo;

    public Employee() {}

    public Employee(int eId, String name, String role, String dept, String qualification,
                    double salary, String sex, String mobNo, String address,
                    String state, String city, String pinNo) {
        this.eId = eId;
        this.name = name;
        this.role = role;
        this.dept = dept;
        this.qualification = qualification;
        this.salary = salary;
        this.sex = sex;
        this.mobNo = mobNo;
        this.address = address;
        this.state = state;
        this.city = city;
        this.pinNo = pinNo;
    }

    public int getEId() { return eId; }
    public void setEId(int eId) { this.eId = eId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getDept() { return dept; }
    public void setDept(String dept) { this.dept = dept; }

    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }

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
