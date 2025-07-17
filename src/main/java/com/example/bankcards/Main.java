package com.example.bankcards;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;

/**
 * Start point of the program
 */
@SpringBootApplication
public class Main {
    /**
     * Default constructor
     */
    public Main() {
    }

    /**
     * Start point
     *
     * @param args - input arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(Main.class, args);
    }
}