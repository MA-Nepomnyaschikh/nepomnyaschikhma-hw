package practice_12.task_1;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.synchronizedList;

public class EntityManager <T extends Entity> {

    private List<T> entities = synchronizedList(new ArrayList<>());

    public void addEntity(T entity) {
        validate(entity != null, "Добавляемая сущность не может быть null");
        entities.add(entity);
    }

    public boolean removeEntity(T entity) {
        validate(entity != null, "Удаляемая сущность не может быть null");
        return entities.remove(entity);
    }

    public List<T> getAllEntities() {
        synchronized(entities) {
            return List.copyOf(entities);
        }
    }

    public List<T> filterByAge(int minAge, int maxAge) {
        validate(minAge >= 0, "Возраст не может быть меньше 0");
        validate(maxAge >= minAge, "Минимальный возраст не может быть больше максимального");

        synchronized(entities) {
            return entities.stream()
                    .filter(entity -> entity.getAge() >= minAge && entity.getAge() <= maxAge)
                    .collect(Collectors.toList());
        }
    }

    public List<T> filterByName(String name) {
        validate(name != null, "Имя сущности не может быть null");
        synchronized(entities) {
            return entities.stream()
                    .filter(entity -> entity.getName().equalsIgnoreCase(name))
                    .collect(Collectors.toList());
        }
    }

    public List<T> filterByStatus(boolean status) {
        synchronized(entities) {
            return entities.stream()
                    .filter(entity -> entity.getStatus() == status)
                    .collect(Collectors.toList());
        }
    }

    public int size() {
        return entities.size();
    }

    public boolean contains(T entity) {
        return entities.contains(entity);
    }


    private void validate(boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }
}
