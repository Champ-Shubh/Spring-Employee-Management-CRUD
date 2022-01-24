package com.example.devtraining;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DevtrainingApplication implements CommandLineRunner {

    private static final Logger logger = LogManager.getLogger(DevtrainingApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DevtrainingApplication.class, args);
    }

    @Override
    public void run(String... args) {
        logger.debug("This is a DEBUG log");
        logger.error("This is an ERROR log");
        logger.fatal("This is a FATAL ERROR log");
        logger.info("This is an INFO log");
        logger.warn("This is a WARNING log");
    }
}
