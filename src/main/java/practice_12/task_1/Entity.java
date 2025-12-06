package practice_12.task_1;

import java.util.Objects;

public class Entity {

    private final int age;
    private final String name;
    private final boolean status;

    public Entity(int age, String name, boolean status) {
        this.age = age;
        this.name = name;
        this.status = status;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public boolean getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return age == entity.age && status == entity.status && Objects.equals(name, entity.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(age, name, status);
    }
}
