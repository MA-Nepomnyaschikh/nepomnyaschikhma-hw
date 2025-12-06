package practice_12.task_1;

import org.junit.jupiter.api.BeforeEach;

public class EntityManagerTest {

    protected EntityManager<User> manager;

    @BeforeEach
    public void setUp() {
        manager = new EntityManager<User>();
    }
}
