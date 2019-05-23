package io.github.logtube.demo.controllers.suppressed;

import io.github.logtube.Logtube;
import io.github.logtube.core.IEventLogger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SuppressedController {

    private static final IEventLogger LOGGER = Logtube.getLogger(SuppressedController.class);

    @GetMapping("/suppressed")
    public String suppressed() {
        LOGGER.withK("keyword1").trace("this is a trace");
        LOGGER.withK("keyword2").debug("this is a debug");
        LOGGER.withK("keyword3").info("this is a info");
        LOGGER.withK("keyword4").warn("this is a warn");
        LOGGER.withK("keyword5").error("this is a error");
        return "suppressed";
    }

}
