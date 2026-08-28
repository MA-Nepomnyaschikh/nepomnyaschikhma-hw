package common.testdata.generator.field;

import common.testdata.generator.FieldValueGenerator;
import common.testdata.generator.annotations.GeneratingRule;

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
