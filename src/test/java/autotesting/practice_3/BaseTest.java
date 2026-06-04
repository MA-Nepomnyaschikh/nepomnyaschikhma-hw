package autotesting.practice_3;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class BaseTest {
    protected SoftAssertions softly;

    @BeforeEach
    public void beforeTests() {
        softly = new SoftAssertions();
    }

    @AfterEach
    public void afterTests() {
        softly.assertAll();
    }
}
