package practice_12.task_4;

import practice_12.task_4.exceptions.InvalidMovieException;
import practice_12.task_4.exceptions.MovieNotFoundException;
import practice_12.task_4.validators.MovieValidator;
import practice_12.task_4.validators.RatingValidator;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class MovieService {

    private final Map<Movie, Queue<Rating<?>>> movieRatings = new ConcurrentHashMap<>();

    public void addRating(Movie movie, Rating<?> rating) {
        MovieValidator.validateMovie(movie);
        RatingValidator.validateRating(rating);

        movieRatings
                .computeIfAbsent(movie, k -> new ConcurrentLinkedQueue<>())
                .add(rating);
    }

    public Double getAverageRating(Movie movie) {
        Queue<Rating<?>> ratings = requireMovieExist(movie);
        return ratings.stream()
                .mapToDouble(rating -> rating.getRating().doubleValue())
                .average()
                .orElse(0.0);
    }

    public Map<Movie, Double> sortMoviesByAverageRating() {
        return movieRatings.keySet().stream()
                .map(movie -> Map.entry(
                        movie,
                        getAverageRating(movie)))
                .sorted(Comparator.<Map.Entry<Movie, Double>>comparingDouble(Map.Entry::getValue)
                        .thenComparing(e -> e.getKey().getTitle())
                        .thenComparing(e -> e.getKey().getYear())
                        .reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public void addMovie(Movie movie) {
        MovieValidator.validateMovie(movie);
        if (movieRatings.putIfAbsent(movie, new ConcurrentLinkedQueue<>()) != null) {
            throw new InvalidMovieException("Фильм " + movie.getTitle() + " уже присутствует в сервисе");
        }
    }

    public void removeMovie(Movie movie) {
        MovieValidator.validateMovie(movie);
        if (movieRatings.remove(movie) == null) {
            throw new practice_12.task_4.exceptions.MovieNotFoundException("Фильм " + movie.getTitle() + " не найден");
        }
    }

    public List<Rating<?>> getMovieRatings(Movie movie) {
        return new LinkedList<>(requireMovieExist(movie));
    }

    public int size() {
        return movieRatings.size();
    }

    public boolean isEmpty() {
        return movieRatings.isEmpty();
    }

    private Queue<Rating<?>> requireMovieExist(Movie movie) {
        MovieValidator.validateMovie(movie);
        Queue<Rating<?>> ratings = movieRatings.get(movie);
        if (ratings == null) throw new MovieNotFoundException("Фильм " + movie.getTitle() + " отсутствует в сервисе");
        return ratings;
    }
}
