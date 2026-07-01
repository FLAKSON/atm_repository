package org.maksymtiutiunnyk.atmproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AtmProjectApplication {
    public static void main(String[] args)  {
        SpringApplication.run(AtmProjectApplication.class, args);
    }
}
