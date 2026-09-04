package common.testdata.generator;

import common.testdata.generator.field.RegexFieldGenerator;
import common.testdata.generator.field.StringFieldGenerator;

import java.lang.reflect.Field;
import java.util.List;

public class GeneratorRegistry {
    private final List<FieldValueGenerator> generators;

    public GeneratorRegistry() {
        generators = List.of(
                new RegexFieldGenerator(),
                new StringFieldGenerator()
        );
    }


    public FieldValueGenerator find(Field field) {

        return generators.stream()
                .filter(generator -> generator.supports(field))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No generator found for " + field.getName()));
    }
}
