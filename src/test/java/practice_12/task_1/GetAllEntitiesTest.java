package practice_12.task_1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class GetAllEntitiesTest extends EntityManagerTest {

    @Test
    public void getAllEntitiesTest() {
        User user1 = new User(18, "Ivan", true);
        User user2 = new User(18, "Ivan", true);
        User user3 = new User(18, "Ivan", true);
        manager.addEntity(user1);
        manager.addEntity(user2);
        manager.addEntity(user3);

        List<User> actualResult = manager.getAllEntities();

        Assertions.assertEquals(3, actualResult.size());
        Assertions.assertEquals(List.of(user1, user2, user3), actualResult);
    }

    @Test
    public void getAllEntitiesEmptyTest() {
        List<User> actualResult = manager.getAllEntities();

        Assertions.assertTrue(actualResult.isEmpty());
    }

    @Test
    public void getAllEntitiesListUnmodifiableTest() {
        User user1 = new User(18, "Ivan", true);
        User user2 = new User(18, "Ivan", true);
        manager.addEntity(user1);

        List<User> actualResult = manager.getAllEntities();

        Assertions.assertThrows(UnsupportedOperationException.class, () -> actualResult.add(user2));
    }
}
