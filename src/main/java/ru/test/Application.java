package ru.test;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import static org.springframework.boot.SpringApplication.run;

@EnableScheduling
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        run(Application.class, args);
    }
}
