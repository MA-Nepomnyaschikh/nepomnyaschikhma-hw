package express_java.practice_2;

public class Main {
    public static void main(String[] args) {
        Car car = new Car("Toyota", 2016);
        car.print();
        car.setYear(2017);
        car.print();

        System.out.println();

        Rectangle rectangle = new Rectangle(12.1, 7.3);
        System.out.println(rectangle.calculateArea());
        rectangle.setWidth(15.7);
        System.out.println(rectangle.calculateArea());

        System.out.println();

        Book book = new Book("test title", "test author");
        book.printInfo();
        book.setAuthor("new author");
        book.printInfo();

        System.out.println();

        BankAccount account = new BankAccount("test owner", 1000000.0);
        account.printBalance();
        account.deposit(2000);
        account.printBalance();
        account.withdraw(45000);
        account.printBalance();

        System.out.println();

        Point point = new Point(5, 7);
        point.print();
        point.setX(12);
        point.print();

        System.out.println();

        StudentGroup testGroup = new StudentGroup("test group", 30);
        testGroup.printInfo();
        testGroup.setStudentCount(27);
        testGroup.printInfo();

        System.out.println();

        Circle circle = new Circle(31.2);
        System.out.println(circle.calculateArea());
        System.out.println(circle.calculateCircumference());
        circle.setRadius(15.7);
        System.out.println(circle.calculateArea());
        System.out.println(circle.calculateCircumference());

        System.out.println();

        Teacher teacher = new Teacher("test name", "test subject");
        teacher.printInfo();
        teacher.setSubject("new subject");
        teacher.printInfo();

        System.out.println();

        Product product = new Product("test name", 55.3);
        product.printInfo();
        product.setPrice(77.1);
        product.printInfo();
        product.applyDiscount(15.0);
        product.printInfo();

        System.out.println();

        Laptop laptop = new Laptop("Lenovo", 150500.0);
        laptop.printInfo();
        laptop.setPrice(120300.0);
        laptop.printInfo();
    }
}
