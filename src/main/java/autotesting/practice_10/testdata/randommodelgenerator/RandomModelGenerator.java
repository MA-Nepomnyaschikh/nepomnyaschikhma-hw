package autotesting.practice_10.testdata.randommodelgenerator;

import java.lang.reflect.Field;

public final class RandomModelGenerator {

    private static final GeneratorRegistry REGISTRY = new GeneratorRegistry();

    private RandomModelGenerator() {}

    public static <T> T generate(Class<T> clazz) {

        try {

            T object = clazz.getDeclaredConstructor().newInstance();

            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                FieldValueGenerator generator = REGISTRY.find(field);
                field.set(object, generator.generate(field));
            }

            return object;

        } catch (Exception e) {
            throw new RuntimeException("Cannot generate " + clazz.getSimpleName(), e);
        }
    }
}
