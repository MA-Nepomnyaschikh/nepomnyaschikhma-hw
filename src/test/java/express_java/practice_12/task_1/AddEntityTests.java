package express_java.practice_12.task_1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AddEntityTests extends EntityManagerTest {

    @Test
    public void addEntityTest() {
        User user = new User(18, "Ivan", true);
        manager.addEntity(user);

        Assertions.assertEquals(1, manager.size());
        Assertions.assertTrue(manager.contains(user));
    }

    @Test
    public void addNullEntityTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> manager.addEntity(null));
    }
}
