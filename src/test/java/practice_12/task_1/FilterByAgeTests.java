package practice_12.task_1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

public class FilterByAgeTests extends EntityManagerTest {

    @Test
    public void filterByAgeTest() {
        User user1 = new User(18, "Ivan", true);
        User user2 = new User(17, "Ivan", true);
        User user3 = new User(25, "Ivan", true);
        manager.addEntity(user1);
        manager.addEntity(user2);
        manager.addEntity(user3);

        List<User> actualResult = manager.filterByAge(18, 100);

        Assertions.assertEquals(2, actualResult.size());
        Assertions.assertEquals(List.of(user1, user3), actualResult);
        Assertions.assertFalse(actualResult.contains(user2));
    }

    @Test
    public void filterByAgeWithEqualsInputTest() {
        User user1 = new User(18, "Ivan", true);
        manager.addEntity(user1);

        List<User> actualResult = manager.filterByAge(18, 18);

        Assertions.assertEquals(1, actualResult.size());
        Assertions.assertEquals(List.of(user1), actualResult);
    }

    @Test
    public void filterByAgeEmptyTest() {
        List<User> actualResult = manager.filterByAge(18, 100);

        Assertions.assertTrue(actualResult.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("invalidDataProvider")
    public void filterByAgeMinLowerThanZeroTest(int minAge, int maxAge, String message) {
        IllegalArgumentException ex = Assertions.assertThrows(IllegalArgumentException.class, () -> manager.filterByAge(minAge, maxAge));
        Assertions.assertEquals(message, ex.getMessage());
    }

    public static Stream<Arguments> invalidDataProvider() {
        return Stream.of(
                Arguments.of(-1, 25, "Минимальный возраст не может быть меньше 0"),
                Arguments.of(30, 25, "Минимальный возраст не может быть больше максимального")
        );
    }

    @Test
    public void filterByAgeReturnsIndependentListTest() {
        User user1 = new User(18, "Ivan", true);
        User user2 = new User(17, "Petr", false);
        User user3 = new User(25, "Ivan", true);
        User user4 = new User(25, "Oleg", true);

        manager.addEntity(user1);
        manager.addEntity(user2);

        List<User> actualResult = manager.filterByAge(18, 18);

        actualResult.add(user3);
        actualResult.add(user4);

        Assertions.assertEquals(3, actualResult.size());
        Assertions.assertEquals(2, manager.size());
    }

}
