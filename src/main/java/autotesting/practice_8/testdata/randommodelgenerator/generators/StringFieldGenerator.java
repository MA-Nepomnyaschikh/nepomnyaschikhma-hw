package autotesting.practice_8.testdata.randommodelgenerator.generators;

import autotesting.practice_8.testdata.randommodelgenerator.FieldValueGenerator;
import autotesting.practice_8.testdata.randommodelgenerator.annotations.GeneratingRule;

import java.lang.reflect.Field;
import java.util.UUID;

public class StringFieldGenerator implements FieldValueGenerator {
    @Override
    public boolean supports(Field field) {
        return field.getType() == String.class
                &&
                !field.isAnnotationPresent(GeneratingRule.class);
    }

    @Override
    public Object generate(Field field) {
        return UUID.randomUUID()
                .toString()
                .substring(0, 8);
    }
}
