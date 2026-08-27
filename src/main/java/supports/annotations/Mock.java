package supports.annotations;

import lombok.Getter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Mock {

    MockScenario scenario();

    @Getter
    public enum MockScenario {
        FRAUD_CHECK_APPROVED("fraud-approved.json"),
        FRAUD_CHECK_BLOCKED("fraud-blocked.json"),
        FRAUD_CHECK_REVIEW_REQUIRED_BY_DECISION("fraud-review-required-by-decision.json"),
        FRAUD_CHECK_REVIEW_REQUIRED_BY_FLAG("fraud-review-required-by-flag.json"),
        FRAUD_CHECK_VERIFICATION_REQUIRED_BY_DECISION("fraud-verification-required-by-decision.json"),
        FRAUD_CHECK_VERIFICATION_REQUIRED_BY_FLAG("fraud-verification-required-by-flag.json"),
        FRAUD_CHECK_SERVICE_ERROR_400("fraud-service-error-400.json"),
        FRAUD_CHECK_SERVICE_ERROR_500("fraud-service-error-500.json"),
        FRAUD_CHECK_SERVICE_UNAVAILABLE_503("fraud-service-unavailable-503.json"),
        FRAUD_CHECK_EMPTY_RESPONSE("fraud-service-empty-body.json"),
        FRAUD_CHECK_TIMEOUT("fraud-service-timeout.json"),
        FRAUD_CHECK_CONNECTION_ERROR("fraud-service-connection-error.json");

        private final String mappingFile;

        MockScenario(String mappingFile) {
            this.mappingFile = mappingFile;
        }

    }
}
