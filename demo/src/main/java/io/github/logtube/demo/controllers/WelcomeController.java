package io.github.logtube.demo.controllers;

import io.github.logtube.Logtube;
import io.github.logtube.core.IEventLogger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    private static final IEventLogger LOGGER = Logtube.getLogger(WelcomeController.class);

    @GetMapping("/hello")
    public String hello() {
        for (int i = 0; i < 1000; i++) {
            LOGGER.keyword("keyword").trace("this is a trace");
            LOGGER.keyword("keyword").debug("this is a debug");
            LOGGER.keyword("keyword").info("this is a info");
            LOGGER.keyword("keyword").warn("this is a warn");
            LOGGER.keyword("keyword").error("this is a error", new Exception("hahah"));
        }
        return "world";
    }

}
