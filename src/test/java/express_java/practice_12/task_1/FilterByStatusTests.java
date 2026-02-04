package express_java.practice_12.task_1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

public class FilterByStatusTests extends EntityManagerTest {

    @ParameterizedTest
    @MethodSource("dataProvider")
    public void filterByStatusTest(User user1, User user2, User user3, boolean status) {
        manager.addEntity(user1);
        manager.addEntity(user2);
        manager.addEntity(user3);

        List<User> actualResult = manager.filterByStatus(status);

        Assertions.assertEquals(2, actualResult.size());
        Assertions.assertEquals(List.of(user1, user3), actualResult);
        Assertions.assertFalse(actualResult.contains(user2));
    }

    private static Stream<Arguments> dataProvider() {
        User user1 = new User(18, "Ivan", true);
        User user2 = new User(17, "Ivan", false);
        User user3 = new User(25, "Ivan", true);
        User user4 = new User(29, "Ivan", false);
        return Stream.of(
                Arguments.of(user1, user2, user3, true),
                Arguments.of(user2, user3, user4, false)
        );
    }

    @Test
    public void filterByStatusEmptyTest() {
        List<User> actualResult = manager.filterByStatus(true);

        Assertions.assertTrue(actualResult.isEmpty());
    }

    @Test
    public void filterByStatusReturnsIndependentListTest() {
        User user1 = new User(18, "Ivan", true);
        User user2 = new User(17, "Ivan", false);
        User user3 = new User(25, "Ivan", true);
        User user4 = new User(25, "Oleg", true);

        manager.addEntity(user1);
        manager.addEntity(user2);

        List<User> actualResult = manager.filterByStatus(true);

        actualResult.add(user3);
        actualResult.add(user4);

        Assertions.assertEquals(3, actualResult.size());
        Assertions.assertEquals(2, manager.size());
    }
}
