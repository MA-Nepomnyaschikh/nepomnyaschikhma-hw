package practice_12.task_4;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SortMoviesByAverageRatingTests extends MovieServiceTest {

    @Test
    public void sortMoviesByAverageRatingTest() {
        Movie movie1 = MovieTestFactory.addMovieWithRatings(movieService,
                new Movie("Фильм 1", 1997),
                new Rating<>(3.0),
                new Rating<>(2),
                new Rating<>(10.0f)
        );
        Movie movie2 = MovieTestFactory.addMovieWithRatings(movieService,
                new Movie("Фильм 2", 1997),
                new Rating<>(2.0),
                new Rating<>(4),
                new Rating<>(6.0f)
        );
        Movie movie3 = MovieTestFactory.addMovieWithRatings(movieService,
                new Movie("Фильм 3", 1997),
                new Rating<>(1.0),
                new Rating<>(3),
                new Rating<>(5.0f)
        );

        Map<Movie, Double> sortedMap = movieService.sortMoviesByAverageRating();

        List<Movie> expectedMovies = List.of(movie1, movie2, movie3);
        List<Movie> actualResult = new ArrayList<>(sortedMap.keySet());

        Assertions.assertEquals(expectedMovies, actualResult);
    }

    @Test
    public void sortMoviesWithEqualsAverageRatingTest() {
        Movie movie1 = MovieTestFactory.addMovieWithRatings(movieService,
                new Movie("Фильм 3", 1997),
                new Rating<>(3.0),
                new Rating<>(2),
                new Rating<>(10.0f)
        );
        Movie movie2 = MovieTestFactory.addMovieWithRatings(movieService,
                new Movie("Фильм 2", 1997),
                new Rating<>(3.0),
                new Rating<>(2),
                new Rating<>(10.0f)
        );
        Movie movie3 = MovieTestFactory.addMovieWithRatings(movieService,
                new Movie("Фильм 1", 1997),
                new Rating<>(3.0),
                new Rating<>(2),
                new Rating<>(10.0f)
        );

        Map<Movie, Double> sortedMap = movieService.sortMoviesByAverageRating();

        List<Movie> expectedMovies = List.of(movie1, movie2, movie3);
        List<Movie> actualResult = new ArrayList<>(sortedMap.keySet());

        Assertions.assertEquals(expectedMovies, actualResult);
    }

    @Test
    public void sortMoviesWithEqualsAverageRatingAndTitleTest() {
        Movie movie1 = MovieTestFactory.addMovieWithRatings(movieService,
                new Movie("Фильм 1", 1995),
                new Rating<>(3.0),
                new Rating<>(2),
                new Rating<>(10.0f)
        );
        Movie movie2 = MovieTestFactory.addMovieWithRatings(movieService,
                new Movie("Фильм 1", 1997),
                new Rating<>(3.0),
                new Rating<>(2),
                new Rating<>(10.0f)
        );
        Movie movie3 = MovieTestFactory.addMovieWithRatings(movieService,
                new Movie("Фильм 1", 1993),
                new Rating<>(3.0),
                new Rating<>(2),
                new Rating<>(10.0f)
        );

        Map<Movie, Double> sortedMap = movieService.sortMoviesByAverageRating();

        List<Movie> expectedMovies = List.of(movie2, movie1, movie3);
        List<Movie> actualResult = new ArrayList<>(sortedMap.keySet());

        Assertions.assertEquals(expectedMovies, actualResult);
    }

    @Test
    public void sortMoviesByAverageRatingWithoutRatingsTest() {
        Movie movie1 = MovieTestFactory.addMovieWithRatings(movieService,
                new Movie("Фильм 1", 1997));
        Movie movie2 = MovieTestFactory.addMovieWithRatings(movieService,
                new Movie("Фильм 2", 1997));
        Movie movie3 = MovieTestFactory.addMovieWithRatings(movieService,
                new Movie("Фильм 3", 1997));

        Map<Movie, Double> sortedMap = movieService.sortMoviesByAverageRating();

        List<Movie> expectedMovies = List.of(movie3, movie2, movie1);
        List<Movie> actualResult = new ArrayList<>(sortedMap.keySet());

        Assertions.assertEquals(expectedMovies, actualResult);
    }



}
