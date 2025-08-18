package main.java;

public class MathOperations {
    public static void main(String[] args) {
        System.out.println(add(2, 3));
        System.out.println(subtract(7, 3));
        System.out.println(multiply(4, 2));
        System.out.println(divide(7, 2));
        System.out.println(findMax(5, 23));
        System.out.println(difference(5, 23));
        System.out.println(squareArea(4));
        System.out.println(squarePerimeter(5));
        System.out.println(convertSecondsToMinutes(123));
        System.out.println(averageSpeed(123, 5));
        System.out.println(averageSpeed(76865, 456));
        System.out.println(findHypotenuse(45, 23));
        System.out.println(findHypotenuse(11, 17));
        System.out.println(circleCircumference(34));
        System.out.println(calculatePercentage(25, 5));
        System.out.println(celsiusToFahrenheit(36.6));
        System.out.println(fahrenheitToCelsius(88.2));

    }

    public static int add(int x, int y) {
        return x + y;
    }

    public static int subtract(int x, int y) {
        return x - y;
    }

    public static int multiply(int x, int y) {
        return x * y;
    }

    public static double divide(int x, int y) {
        return (double) x / y;
    }

    public static int findMax(int a, int b) {
        return Math.max(a, b);
    }

    public static int difference(int x, int y) {
        return Math.abs(x - y);
    }

    public static int squareArea(int side) {
        return side * side;
    }

    public static int squarePerimeter(int side) {
        return side * 4;
    }

    public static double convertSecondsToMinutes(int seconds) {
        return (double) seconds / 60;
    }

    public static double averageSpeed(double distance, double time) {
        return distance / time;
    }

    public static double findHypotenuse(double a, double b) {
        return Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
    }

    public static double circleCircumference(double radius) {
        return 2 * Math.PI * radius;
    }

    public static double calculatePercentage(double total, double part) {
        return (part / total) * 100;
    }

    public static double celsiusToFahrenheit(double c) {
        return c * 9 / 5 + 32;
    }

    public static double fahrenheitToCelsius(double f) {
        return (f - 32) * 5 / 9;
    }



}
