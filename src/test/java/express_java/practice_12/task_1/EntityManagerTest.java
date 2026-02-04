package express_java.practice_12.task_1;

import express_java.practice_12.task_1.EntityManager;
import express_java.practice_12.task_1.User;
import org.junit.jupiter.api.BeforeEach;

public class EntityManagerTest {

    protected EntityManager<User> manager;

    @BeforeEach
    public void setUp() {
        manager = new EntityManager<User>();
    }
}
