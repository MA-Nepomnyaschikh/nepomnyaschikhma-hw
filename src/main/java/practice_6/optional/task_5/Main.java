package main.java.practice_6.optional.task_5;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        trackActions();
    }

    public static void trackActions() {
        ArrayDeque<String> actions = new ArrayDeque<>();
        ArrayDeque<String> canceledActions = new ArrayDeque<>();
        Scanner sc = new Scanner(System.in);

        while (true) {
            String action = sc.nextLine();

            if (action.equals("Выход")) {
                break;
            } else if (action.equals("Отменить действие")) {
                if (!actions.isEmpty()) {
                    canceledActions.push(actions.peek());
                    System.out.println("Действие " + actions.pop() + " отменено");
                } else {
                    System.out.println("Нет действий для отмены");
                }
            } else if (action.equals("Повторить отмененное действие")) {
                if (!canceledActions.isEmpty()) {
                    actions.push(canceledActions.peek());
                    System.out.println(canceledActions.pop());
                } else {
                    System.out.println("Нет отмененных действий");
                }
            } else {
                actions.push(action);
                canceledActions.clear();
            }
        }
        sc.close();
    }
}
