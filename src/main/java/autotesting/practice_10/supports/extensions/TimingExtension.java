package autotesting.practice_10.supports.extensions;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class TimingExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

    private static final ExtensionContext.Namespace NAMESPACE =
            ExtensionContext.Namespace.create(TimingExtension.class);

    private static final String START_TIME = "startTime";

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        String testName = context.getRequiredTestClass().getPackageName() + "." + context.getDisplayName();
        context.getStore(NAMESPACE)
                .put(START_TIME, System.currentTimeMillis());
        System.out.println("Thread " + Thread.currentThread().getName() + " started test: " + testName);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        String testName = context.getRequiredTestClass().getPackageName() + "." + context.getDisplayName();
        Long startTime = context.getStore(NAMESPACE)
                .get(START_TIME, Long.class);
        long testDuration = System.currentTimeMillis() - startTime;
        System.out.println("Thread " + Thread.currentThread().getName() + " finished test: " + testName + ", test duration " + testDuration + " ms");
    }
}
