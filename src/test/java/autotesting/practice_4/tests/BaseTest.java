package autotesting.practice_4.tests;

import autotesting.practice_4.steps.UserSteps;
import autotesting.practice_4.steps.AuthSteps;
import autotesting.practice_4.steps.AdminSteps;
import autotesting.practice_4.supports.CleanupManager;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class BaseTest {
    protected CleanupManager cleanupManager;

    protected SoftAssertions softly;

    protected AuthSteps authSteps;
    protected AdminSteps adminSteps;
    protected UserSteps userSteps;

    @BeforeEach
    public void beforeTests() {
        cleanupManager = new CleanupManager();

        softly = new SoftAssertions();

        authSteps = new AuthSteps();
        adminSteps = new AdminSteps(cleanupManager);
        userSteps = new UserSteps();
    }

    @AfterEach
    public void afterTests() {
        cleanupManager.cleanup();
        softly.assertAll();
    }
}
