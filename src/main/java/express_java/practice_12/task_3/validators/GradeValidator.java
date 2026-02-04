package express_java.practice_12.task_3.validators;

import express_java.practice_12.task_3.StudentGrade;
import express_java.practice_12.task_3.exceptions.InvalidGradeException;

public class GradeValidator {

    public static void validateStudentGrade(StudentGrade<?> grade) {
        if (grade == null) throw new InvalidGradeException("StudentGrade не может быть null");
        if (grade.getGrade() == null) throw new InvalidGradeException("Оценка не может быть пустой");
        validateSubject(grade.getSubject());
        validateName(grade.getName());
        validatePositiveGrade(grade.getGrade().doubleValue());
    }

    public static void validateSubject(String subject) {
        if (subject == null || subject.isBlank()) throw new InvalidGradeException("Предмет не может быть пустым");
    }

    public static void validateName(String name) {
        if (name == null || name.isBlank()) throw new InvalidGradeException("Имя студента не может быть пустым");
    }

    public static void validatePositiveGrade(Double grade) {
        if (grade < 0) throw new InvalidGradeException("Оценка не может быть отрицательной");
    }
}
