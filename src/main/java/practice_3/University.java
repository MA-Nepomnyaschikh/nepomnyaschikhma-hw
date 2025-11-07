package practice_3;

public class University {
    static String universityName;
    final int studentID;
    String studentName;

    static {
        University.universityName = "University";
    }

    public University(int studentID, String studentName) {
        this.studentID = studentID;
        this.studentName = studentName;
    }

    public String getStudentName() {
        return studentName;
    }

    public static void changeUniversityName(String newName) {
        University.universityName = newName;
    }

    public void printStudentInfo() {
        System.out.println("Student Name: " + studentName + ", ID: " + studentID + ", University Name: " + universityName);
    }
}
