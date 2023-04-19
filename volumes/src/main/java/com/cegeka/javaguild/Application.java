package com.cegeka.javaguild;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

@SpringBootApplication
public class Application {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
        System.out.println("Hello Java Guild");
        Files.writeString(Path.of("/data/hello.txt"), "Hello at " + LocalDateTime.now());
    }
}