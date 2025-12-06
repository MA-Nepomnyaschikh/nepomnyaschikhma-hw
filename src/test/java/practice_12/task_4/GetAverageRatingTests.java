package practice_12.task_4;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import practice_12.task_4.exceptions.InvalidMovieException;
import practice_12.task_4.exceptions.MovieNotFoundException;

import java.util.stream.Stream;

public class GetAverageRatingTests extends MovieServiceTest {

    @Test
    public void getAverageRatingTest() {
        Movie movie = MovieTestFactory.addMovieWithRatings(movieService,
                new Movie("Фильм 1", 1997),
                new Rating<>(3.0),
                new Rating<>(5),
                new Rating<>(10.0f)
        );

        Double actualRating = movieService.getAverageRating(movie);

        Assertions.assertEquals(6.0, actualRating);
    }

    @Test
    public void getAverageRatingForMovieWithoutRatingsTest() {
        Movie movie = new Movie("Фильм 1", 1997);
        movieService.addMovie(movie);

        Double actualRating = movieService.getAverageRating(movie);

        Assertions.assertEquals(0.0, actualRating);
    }

    @Test
    public void getAverageRatingForUndefinedMovieTest() {
        Movie movie = new Movie("Фильм 1", 1997);

        Assertions.assertThrows(MovieNotFoundException.class, () -> movieService.getAverageRating(movie));
    }

    @ParameterizedTest
    @MethodSource("invalidMovieProvider")
    public void getAverageRatingForInvalidMovieTest(Movie movie) {
        Rating<?> rating = new Rating<>(10.0);

        Assertions.assertThrows(InvalidMovieException.class, () -> movieService.getAverageRating(movie));
    }

    private static Stream<Arguments> invalidMovieProvider() {
        return Stream.of(
                Arguments.of(new Movie(null, 1997)),
                Arguments.of((Object) null)
        );
    }
}
