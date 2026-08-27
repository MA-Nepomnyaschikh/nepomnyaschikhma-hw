package supports.extensions;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import supports.annotations.Mock;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static supports.annotations.Mock.*;

public class WireMockExtension
        implements BeforeEachCallback, AfterEachCallback {

    private static final String MAPPINGS_PATH = "/wiremock/mappings/";

    private WireMockServer wireMockServer;

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {

        Mock mock = findMock(context);

        if (mock == null) {
            return;
        }

        wireMockServer = new WireMockServer(
                WireMockConfiguration.wireMockConfig().port(9090)
        );

        wireMockServer.start();

        System.out.println(
                "WIREMOCK STARTED: " +
                        wireMockServer.baseUrl()
        );

        System.out.println(
                "WIREMOCK SCENARIO: " + mock.scenario()
        );

        registerMapping(mock.scenario());

        System.out.println(
                "WIREMOCK MAPPING: " +
                        mock.scenario().name()
        );
    }

    private void registerMapping(MockScenario scenario) throws IOException {

        String resourcePath = MAPPINGS_PATH + scenario.getMappingFile();

        try (InputStream inputStream =
                     WireMockExtension.class.getResourceAsStream(resourcePath)) {

            if (inputStream == null) {
                throw new IllegalStateException(
                        "WireMock mapping not found: " + resourcePath
                );
            }

            String mappingJson = new String(
                    inputStream.readAllBytes(),
                    StandardCharsets.UTF_8
            );

            StubMapping stubMapping = StubMapping.buildFrom(mappingJson);

            wireMockServer.addStubMapping(stubMapping);
        }
    }

    private Mock findMock(ExtensionContext context) {

        Mock mock = context.getTestMethod()
                .map(method -> method.getAnnotation(Mock.class))
                .orElse(null);

        if (mock != null) {
            return mock;
        }

        return context.getTestClass()
                .map(clazz -> clazz.getAnnotation(Mock.class))
                .orElse(null);
    }

    @Override
    public void afterEach(ExtensionContext context) {

        if (wireMockServer != null) {
            wireMockServer.stop();

            System.out.println(
                    "WIREMOCK STOPPED: " +
                            context.getDisplayName()
            );

            wireMockServer = null;
        }
    }

    public String getBaseUrl() {

        if (wireMockServer == null) {
            throw new IllegalStateException(
                    "WireMock server is not running"
            );
        }

        return "http://host.docker.internal:" + wireMockServer.port();
    }
}
