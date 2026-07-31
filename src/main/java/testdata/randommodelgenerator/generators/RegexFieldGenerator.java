package testdata.randommodelgenerator.generators;

import testdata.randommodelgenerator.FieldValueGenerator;
import testdata.randommodelgenerator.annotations.GeneratingRule;
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
