package api;

import configs.Config;
import dao.ConnectionManager;
import dao.DatabaseClient;
import dao.loggers.ConsoleDbLogger;
import dao.loggers.DbLogger;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import steps.AccountSteps;
import steps.AuthSteps;
import steps.DatabaseSteps;
import steps.UserSteps;
import supports.CleanupManager;
import supports.extensions.TimingExtension;
import supports.extensions.UserSessionExtension;

@ExtendWith(UserSessionExtension.class)
@ExtendWith(TimingExtension.class)
public class BaseTest {
    protected CleanupManager cleanupManager;

    protected ConnectionManager connectionManager;
    protected DbLogger logger;
    protected DatabaseClient databaseClient;

    protected SoftAssertions softly;

    protected AuthSteps authSteps;
    protected UserSteps userSteps;
    protected AccountSteps accountSteps;
    protected DatabaseSteps databaseSteps;



    @BeforeAll
    static void check() {
        System.out.println(
                Runtime.getRuntime().availableProcessors()
        );
    }

    @BeforeEach
    public void beforeTests() {
        cleanupManager = new CleanupManager();

        connectionManager = new ConnectionManager(
                Config.getProperty("db.url"),
                Config.getProperty("db.username"),
                Config.getProperty("db.password")
        );
        logger = new ConsoleDbLogger();

        databaseClient = new DatabaseClient(connectionManager, logger);

        softly = new SoftAssertions();

        authSteps = new AuthSteps();
        userSteps = new UserSteps(cleanupManager);
        accountSteps = new AccountSteps();
        databaseSteps = new DatabaseSteps(databaseClient);
    }

    @AfterEach
    public void afterTests() {
        cleanupManager.cleanup();
        softly.assertAll();
    }
}
