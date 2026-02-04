package express_java.practice_12.task_1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class FilterByNameTests extends EntityManagerTest {

    @Test
    public void filterByNameTest() {
        User user1 = new User(18, "Ivan", true);
        User user2 = new User(17, "Petr", true);
        User user3 = new User(25, "Ivan", true);
        manager.addEntity(user1);
        manager.addEntity(user2);
        manager.addEntity(user3);

        List<User> actualResult = manager.filterByName("Ivan");

        Assertions.assertEquals(2, actualResult.size());
        Assertions.assertEquals(List.of(user1, user3), actualResult);
        Assertions.assertFalse(actualResult.contains(user2));
    }

    @Test
    public void filterByNameIgnoreCaseTest() {
        User user1 = new User(18, "Ivan", true);
        User user2 = new User(17, "Petr", true);
        User user3 = new User(25, "ivan", true);
        manager.addEntity(user1);
        manager.addEntity(user2);
        manager.addEntity(user3);

        List<User> actualResult = manager.filterByName("Ivan");

        Assertions.assertEquals(2, actualResult.size());
        Assertions.assertEquals(List.of(user1, user3), actualResult);
        Assertions.assertFalse(actualResult.contains(user2));
    }

    @Test
    public void filterByNameEmptyManagerTest() {
        List<User> actualResult = manager.filterByName("Ivan");
        Assertions.assertTrue(actualResult.isEmpty());
    }

    @Test
    public void filterByNameEmptyStringTest() {
        List<User> result = manager.filterByName("");
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void filterByNameNullTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> manager.filterByName(null));
    }

    @Test
    public void filterByNameContainsTest() {
        User user = new User(18, "Ivan", true);
        manager.addEntity(user);

        List<User> result = manager.filterByName("Iv");

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void filterByNameReturnsIndependentListTest() {
        User user1 = new User(18, "Ivan", true);
        User user2 = new User(17, "Petr", false);
        User user3 = new User(25, "Ivan", true);
        User user4 = new User(25, "Oleg", true);

        manager.addEntity(user1);
        manager.addEntity(user2);

        List<User> actualResult = manager.filterByName("Ivan");

        actualResult.add(user3);
        actualResult.add(user4);

        Assertions.assertEquals(3, actualResult.size());
        Assertions.assertEquals(2, manager.size());
    }
}
