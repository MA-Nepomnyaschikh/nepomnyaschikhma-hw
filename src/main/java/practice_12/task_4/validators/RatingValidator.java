package practice_12.task_4.validators;

import practice_12.task_4.Rating;
import practice_12.task_4.exceptions.InvalidRatingException;

public class RatingValidator {

    private static final int MIN_RATING = 1;
    private static final int MAX_RATING = 10;

    public static void validateRating(Rating<?> rating) {
        if (rating == null) throw new InvalidRatingException("Рейтинг не может быть null");
        if (rating.getRating() == null) throw new InvalidRatingException("Рейтинг не может быть null");
        validateRatingRange(rating);
    }

    private static void validateRatingRange(Rating<?> rating) {
        double value = rating.getRating().doubleValue();
        if (value < MIN_RATING || value > MAX_RATING) throw new InvalidRatingException("Рейтинг должен быть в диапазоне от " + MIN_RATING + " до " + MAX_RATING);
    }
}
