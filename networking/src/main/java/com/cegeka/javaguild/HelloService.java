package com.cegeka.javaguild;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelloService.class);

    @GetMapping("/hello")
    public String sayHello(){
        LOGGER.info("Will say hello");
        return "Hello\n";
    }
}
