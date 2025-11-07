package practice_3;

public class Company {
    static String companyName;
    private final int employeeID;
    private String employeeName;

    static {
        companyName = "testCompany";
    }

    public Company(int employeeID, String employeeName) {
        this.employeeID = employeeID;
        this.employeeName = employeeName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public static void printCompanyName() {
        System.out.println("Company Name: " + Company.companyName);
    }
}
