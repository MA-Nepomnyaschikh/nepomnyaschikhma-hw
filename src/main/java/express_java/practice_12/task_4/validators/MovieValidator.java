package express_java.practice_12.task_4.validators;

import express_java.practice_12.task_4.Movie;
import express_java.practice_12.task_4.exceptions.InvalidMovieException;

public class MovieValidator {

    public static void validateMovie(Movie movie) {
        if (movie == null) throw new InvalidMovieException("Фильм не может быть null");
        if (movie.getTitle() == null) throw new InvalidMovieException("Название фильма не может быть null");
    }
}
