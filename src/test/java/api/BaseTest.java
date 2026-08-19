package api;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import steps.AccountSteps;
import steps.AuthSteps;
import steps.UserSteps;
import supports.CleanupManager;
import supports.extensions.TimingExtension;

@ExtendWith(TimingExtension.class)
public class BaseTest {
    protected CleanupManager cleanupManager;

    protected SoftAssertions softly;

    protected AuthSteps authSteps;
    protected UserSteps userSteps;
    protected AccountSteps accountSteps;

    @BeforeAll
    static void check() {
        System.out.println(
                Runtime.getRuntime().availableProcessors()
        );
    }

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
