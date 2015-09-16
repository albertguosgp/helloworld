package flextrade.flexvision.fx.steps;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import flextrade.flexvision.fx.MaxxRestfulMain;
import flextrade.flexvision.fx.config.IntegrationTestAppConfig;
import lombok.Getter;

@RunWith(SpringJUnit4ClassRunner.class)
@IntegrationTest(value = "server.post:0")
@ContextConfiguration(classes = {MaxxRestfulMain.class, IntegrationTestAppConfig.class},
        loader = SpringApplicationContextLoader.class)
public abstract class AbstractSteps {
    @Getter
    @Value("${local.server.port}")
    private int port;

    protected String getBaseUrl() {
        return "http://localhost:" + port;
    }
}
