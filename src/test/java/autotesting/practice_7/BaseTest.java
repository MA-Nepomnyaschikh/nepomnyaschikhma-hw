package autotesting.practice_7;

import autotesting.practice_7.steps.AccountSteps;
import autotesting.practice_7.steps.AuthSteps;
import autotesting.practice_7.steps.UserSteps;
import autotesting.practice_7.supports.CleanupManager;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class BaseTest {
    protected CleanupManager cleanupManager;

    protected SoftAssertions softly;

    protected AuthSteps authSteps;
    protected UserSteps userSteps;
    protected AccountSteps accountSteps;

    @BeforeEach
    public void beforeTests() {
        cleanupManager = new CleanupManager();

        softly = new SoftAssertions();

        authSteps = new AuthSteps();
        userSteps = new UserSteps(cleanupManager);
        accountSteps = new AccountSteps();
    }

    @AfterEach
    public void afterTests() {
        cleanupManager.cleanup();
        softly.assertAll();
    }
}
