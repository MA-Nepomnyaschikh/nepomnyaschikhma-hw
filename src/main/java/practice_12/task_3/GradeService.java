package practice_12.task_3;

import practice_12.task_3.validators.GradeValidator;

import java.util.ArrayList;
import java.util.List;

public class GradeService <T extends Number> {

    private final List<StudentGrade<T>> grades = new ArrayList<>();

    public synchronized void addGrade(StudentGrade<T> grade) {
        GradeValidator.validateStudentGrade(grade);
        grades.add(grade);
    }

    public synchronized Double getAverageGrade(String subject) {
        GradeValidator.validateSubject(subject);
        return grades.stream()
                .filter(grade -> grade.getSubject().equalsIgnoreCase(subject))
                .mapToDouble(grade -> grade.getGrade().doubleValue())
                .average()
                .orElse(0.0);
    }

    public synchronized List<StudentGrade<T>> getGrades() {
        return List.copyOf(grades);
    }

    public synchronized int size() {
        return grades.size();
    }

    public synchronized boolean contains(StudentGrade<T> grade) {
        GradeValidator.validateStudentGrade(grade);
        return grades.contains(grade);
    }

    public synchronized boolean isEmpty() {
        return grades.isEmpty();
    }
}
