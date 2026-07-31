package testdata.randommodelgenerator;

import java.lang.reflect.Field;

public interface FieldValueGenerator {

    boolean supports(Field field);

    Object generate(Field field);
}
