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

import static supports.annotations.Mock.MockScenario;

public class WireMockExtension
        implements BeforeEachCallback, AfterEachCallback {

    private static final String MAPPINGS_PATH = "/wiremock/mappings/";

    private WireMockServer wireMockServer;

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {

        Mock mock = context.getRequiredTestMethod()
                .getAnnotation(Mock.class);

        if (mock == null) {
            return;
        }

        wireMockServer = new WireMockServer(
                WireMockConfiguration.wireMockConfig().port(9090)
        );

        wireMockServer.start();
        wireMockServer.resetAll();

        registerMapping(mock.scenario());

        System.out.println(
                wireMockServer.getStubMappings()
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

    @Override
    public void afterEach(ExtensionContext context) {

        if (wireMockServer != null) {
            wireMockServer.stop();
            wireMockServer = null;
        }
    }
}
