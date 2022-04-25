package ru.ogneva.clubtab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClubTabApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClubTabApplication.class, args);
        System.out.println("Hello club!");
    }
}
