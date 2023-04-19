package com.cegeka.javaguild;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloService {

    private final String helloServiceUrl;
    private final String helloServiceApiKey;

    public HelloService(
            @Value("hello-service.url") String helloServiceUrl,
            @Value("hello-service.api-key") String helloServiceApiKey
    ) {
        this.helloServiceUrl = helloServiceUrl;
        this.helloServiceApiKey = helloServiceApiKey;
    }

    @GetMapping("/hello")
    public String requestHello() {
        return "Requesting hello from " + helloServiceUrl + " with my super secret api key: " + helloServiceApiKey;
    }
}
