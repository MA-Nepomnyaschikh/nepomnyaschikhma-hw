package practice_3;

public class Main {
    public static void main(String[] args) {
        practice_3.Company employee1 = new practice_3.Company(1, "Misha");
        practice_3.Company employee2 = new practice_3.Company(2, "Dima");
        practice_3.Company.printCompanyName();
        practice_3.Company.companyName = "newCompany";
        practice_3.Company.printCompanyName();

        System.out.println();

        System.out.println(practice_3.MathConstants.calculateCircleArea(4.32));
        System.out.println(practice_3.MathConstants.calculateCircleArea(2.79));
        System.out.println(practice_3.MathConstants.calculateCircumference(4.32));
        System.out.println(practice_3.MathConstants.calculateCircumference(2.79));

        System.out.println();

        practice_3.Library library = new practice_3.Library();
        library.category = "test";
        library.author = "Misha";
        library.year = 123;

        System.out.println();

        practice_3.University student1 = new practice_3.University(1, "Misha");
        practice_3.University student2 = new practice_3.University(2, "Alex");
        practice_3.University student3 = new practice_3.University(3, "Dima");
        student1.printStudentInfo();
        student2.printStudentInfo();
        student3.printStudentInfo();
        practice_3.University.universityName = "newUniversity";
        student1.printStudentInfo();
        student2.printStudentInfo();
        student3.printStudentInfo();

        System.out.println();

        practice_3.GameSettings game1 = new practice_3.GameSettings("game1");
        practice_3.GameSettings game2 = new practice_3.GameSettings("game2");
        game1.addPlayer();
        game1.addPlayer();
        game1.addPlayer();
        game1.addPlayer();
        game2.addPlayer();
        game2.addPlayer();
        game1.printGameStatus();
        game2.printGameStatus();
        practice_3.GameSettings.setMaxPlayers(5);
        game1.addPlayer();
        game1.addPlayer();
        game2.addPlayer();
        game2.addPlayer();
        game1.printGameStatus();
        game2.printGameStatus();

        System.out.println();

        Person person1 = new Person("firstName1", "lastName1", "123-45-6789");
        Person person2 = new Person("firstName2", "lastName2", "567-23-1075");
        person1.printPersonInfo();
        person2.printPersonInfo();
        person1.setFirstName("newFirstName");
        person2.setLastName("newFirstName");
        person1.printPersonInfo();
        person2.printPersonInfo();


    }
}
