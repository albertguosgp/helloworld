package flextrade.flexvision.fx.trade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.ZonedDateTime;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class RFQController {

    @Autowired
    @Qualifier(value = "maxxAsyncTaskExecutor")
    private AsyncTaskExecutor asyncTaskExecutor;

    @Autowired
    private SimpMessagingTemplate simpleMessageTemplate;

    @MessageMapping("/rfq")
    public void handleRFQRequest(String rfqRequest) {
        log.info("received {}", rfqRequest);
        asyncTaskExecutor.submit(() -> {
            while (true) {
                simpleMessageTemplate.convertAndSend("/topic/rfq/result", ZonedDateTime.now().toString());
            }
        });
    }
}
