package express_java.practice_12.task_4;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import express_java.practice_12.task_4.exceptions.InvalidMovieException;
import express_java.practice_12.task_4.exceptions.InvalidRatingException;

import java.util.stream.Stream;

public class AddRatingTests extends MovieServiceTest{

    @ParameterizedTest
    @MethodSource("validRatingProvider")
    public void addRatingTest(Rating<?> rating) {
        Movie movie = new Movie("Фильм 1", 1997);

        movieService.addRating(movie, rating);

        Number actualRating = movieService.getMovieRatings(movie).getFirst().getRating();
        Number expectedRating = rating.getRating();

        Assertions.assertEquals(1, movieService.size());
        Assertions.assertEquals(expectedRating, actualRating);
    }

    private static Stream<Arguments> validRatingProvider() {
        return Stream.of(
                Arguments.of(new Rating<>(1)),
                Arguments.of(new Rating<>(10.0)),
                Arguments.of(new Rating<>(3L)),
                Arguments.of(new Rating<>(2.5f))
        );
    }

    @ParameterizedTest
    @MethodSource("invalidRatingProvider")
    public void addInvalidRatingTest(Rating<?> rating) {
        Movie movie = new Movie("Фильм 1", 1997);

        Assertions.assertThrows(InvalidRatingException.class, () -> movieService.addRating(movie, rating));
    }

    private static Stream<Arguments> invalidRatingProvider() {
        return Stream.of(
                Arguments.of(new Rating<>(0.9)),
                Arguments.of(new Rating<>(10.1)),
                Arguments.of(new Rating<>(null)),
                Arguments.of((Object) null)
        );
    }

    @ParameterizedTest
    @MethodSource("invalidMovieProvider")
    public void addRatingWithInvalidMovieTest(Movie movie) {
        Rating<?> rating = new Rating<>(10.0);

        Assertions.assertThrows(InvalidMovieException.class, () -> movieService.addRating(movie, rating));
    }

    private static Stream<Arguments> invalidMovieProvider() {
        return Stream.of(
                Arguments.of(new Movie(null, 1997)),
                Arguments.of((Object) null)
        );
    }


}
