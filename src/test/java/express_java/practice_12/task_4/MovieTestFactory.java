package express_java.practice_12.task_4;

import express_java.practice_12.task_4.Movie;
import express_java.practice_12.task_4.MovieService;
import express_java.practice_12.task_4.Rating;

public class MovieTestFactory {

    public static Movie addMovieWithRatings(MovieService service, Movie movie, Rating<?>...ratings) {
        service.addMovie(movie);

        for (Rating<?> rating : ratings) {
            service.addRating(movie, rating);
        }

        return movie;
    }
}
