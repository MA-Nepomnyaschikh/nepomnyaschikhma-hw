package express_java.practice_12.task_1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RemoveEntityTests extends EntityManagerTest {

    @Test
    public void removeEntityTest() {
        User user = new User(18, "Ivan", true);
        manager.addEntity(user);

        boolean actualResult = manager.removeEntity(user);

        Assertions.assertEquals(0, manager.size());
        Assertions.assertFalse(manager.contains(user));
        Assertions.assertTrue(actualResult);
    }

    @Test
    public void removeNotPresentEntityTest() {
        User user = new User(18, "Ivan", true);

        boolean actualResult = manager.removeEntity(user);

        Assertions.assertFalse(actualResult);
    }

    @Test
    public void removeNullEntityTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> manager.removeEntity(null));
    }

    @Test
    public void removeOneOfDuplicateEntityTest() {
        User user = new User(18, "Ivan", true);
        manager.addEntity(user);
        manager.addEntity(user);

        boolean actualResult = manager.removeEntity(user);

        Assertions.assertEquals(1, manager.size());
        Assertions.assertTrue(manager.contains(user));
        Assertions.assertTrue(actualResult);
    }
}
