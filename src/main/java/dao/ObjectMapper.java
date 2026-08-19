package dao;

import java.lang.reflect.Field;
import java.sql.ResultSet;

public class ObjectMapper<T> {

    private final Class<T> type;

    public ObjectMapper(Class<T> type) {
        this.type = type;
    }

    public T map(ResultSet resultSet) {
        try {
            T object = type.getDeclaredConstructor().newInstance();

            for (Field field : type.getDeclaredFields()) {
                String columnName = toSnakeCase(field.getName());
                Object value = resultSet.getObject(columnName);
                field.setAccessible(true);
                field.set(object, value);
            }
            return object;

        } catch (Exception e) {
            throw new RuntimeException("Failed to map ResultSet to " + type.getName(), e);
        }
    }

    private String toSnakeCase(String fieldName) {
        return fieldName.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
}