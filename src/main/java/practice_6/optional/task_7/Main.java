package main.java.practice_6.optional.task_7;

import java.util.ArrayDeque;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        if (isBalanced(s)) {
            System.out.println("Скобки расставлены правильно");
        } else {
            System.out.println("Скобки расставлены неправильно");
        }
        sc.close();
    }

    public static boolean isBalanced(String s) {
        ArrayDeque<Character> stack = new ArrayDeque<>();
        for (char c : s.toCharArray()) {
            if (c == '(' || c == '{' || c == '[') {
                stack.push(c);
            } else if (c == ')' || c == '}' || c == ']') {
                if (stack.isEmpty()) {
                    return false;
                } else {
                    char top = stack.peek();
                    if (top == '(' && c == ')' || top == '{' && c == '}' || top == '[' && c == ']') {
                        stack.pop();
                    }
                    else {
                        return false;
                    }
                }
            }
        }
        return stack.isEmpty();
    }
}
