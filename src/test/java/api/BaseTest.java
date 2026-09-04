package api;

import common.configs.Config;
import database.core.ConnectionManager;
import database.core.DatabaseClient;
import database.loggers.ConsoleDbLogger;
import database.loggers.DbLogger;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import api.steps.AccountSteps;
import api.steps.AuthSteps;
import database.steps.DatabaseSteps;
import api.steps.UserSteps;
import common.cleanup.CleanupManager;
import common.extensions.TimingExtension;
import common.extensions.UserSessionExtension;
import common.extensions.WireMockExtension;

@ExtendWith(TimingExtension.class)
@ExtendWith(WireMockExtension.class)
@ExtendWith(UserSessionExtension.class)
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
