package autotesting.practice_9.supports.extensions;

import autotesting.practice_9.supports.annotations.AdminSession;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import static autotesting.practice_9.pages.BasePage.setAuthToken;
import static autotesting.practice_9.testdata.AuthData.ADMIN_TOKEN;

public class AdminSessionExtension implements BeforeEachCallback {
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        // Шаг 1: Проверить, есть ли у теста аннотация AdminSession
        AdminSession annotation = context.getRequiredTestMethod().getAnnotation(AdminSession.class);
        // Шаг 2: Если есть, добавить в локал сторейдж токен админа
        if (annotation != null) {
            setAuthToken(ADMIN_TOKEN);
        }
    }
}
