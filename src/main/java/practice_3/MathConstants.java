package practice_3;

public class MathConstants {
    public static final double PI = 3.14159;
    public static final double E = 2.71828;

    public static double calculateCircleArea(double radius) {
        return PI * Math.pow(radius, 2);
    }

    public static double calculateCircumference(double radius) {
        return 2 * PI * radius;
    }
}
