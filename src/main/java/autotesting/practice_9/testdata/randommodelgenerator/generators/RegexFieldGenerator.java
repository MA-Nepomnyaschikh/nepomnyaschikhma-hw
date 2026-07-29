package autotesting.practice_9.testdata.randommodelgenerator.generators;

import autotesting.practice_9.testdata.randommodelgenerator.FieldValueGenerator;
import autotesting.practice_9.testdata.randommodelgenerator.annotations.GeneratingRule;
import com.github.curiousoddman.rgxgen.RgxGen;

import java.lang.reflect.Field;

public class RegexFieldGenerator implements FieldValueGenerator {
    @Override
    public boolean supports(Field field) {
        return field.isAnnotationPresent(GeneratingRule.class);
    }

    @Override
    public Object generate(Field field) {
        GeneratingRule annotation = field.getAnnotation(GeneratingRule.class);

        return RgxGen
                .parse(annotation.regex())
                .generate();
    }
}
