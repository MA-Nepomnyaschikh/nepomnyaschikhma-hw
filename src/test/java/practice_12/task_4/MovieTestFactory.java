package practice_12.task_4;

public class MovieTestFactory {

    public static Movie addMovieWithRatings(MovieService service, Movie movie, Rating<?>...ratings) {
        service.addMovie(movie);

        for (Rating<?> rating : ratings) {
            service.addRating(movie, rating);
        }

        return movie;
    }
}
