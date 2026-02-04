package express_java.practice_12.task_4;

import express_java.practice_12.task_4.MovieService;
import org.junit.jupiter.api.BeforeEach;

public class MovieServiceTest {

    protected MovieService movieService;

    @BeforeEach
    public void setUp() {
        movieService = new MovieService();
    }
}
