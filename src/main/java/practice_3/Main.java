package main.java.practice_3;

public class Main {
    public static void main(String[] args) {
        Company employee1 = new Company(1, "Misha");
        Company employee2 = new Company(2, "Dima");
        Company.printCompanyName();
        Company.companyName = "newCompany";
        Company.printCompanyName();

        System.out.println();

        System.out.println(MathConstants.calculateCircleArea(4.32));
        System.out.println(MathConstants.calculateCircleArea(2.79));
        System.out.println(MathConstants.calculateCircumference(4.32));
        System.out.println(MathConstants.calculateCircumference(2.79));

        System.out.println();

        Library library = new Library();
        library.category = "test";
        library.author = "Misha";
        library.year = 123;

        System.out.println();

        University student1 = new University(1, "Misha");
        University student2 = new University(2, "Alex");
        University student3 = new University(3, "Dima");
        student1.printStudentInfo();
        student2.printStudentInfo();
        student3.printStudentInfo();
        University.universityName = "newUniversity";
        student1.printStudentInfo();
        student2.printStudentInfo();
        student3.printStudentInfo();

        System.out.println();

        GameSettings game1 = new GameSettings("game1");
        GameSettings game2 = new GameSettings("game2");
        game1.addPlayer();
        game1.addPlayer();
        game1.addPlayer();
        game1.addPlayer();
        game2.addPlayer();
        game2.addPlayer();
        game1.printGameStatus();
        game2.printGameStatus();
        GameSettings.setMaxPlayers(5);
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
