package org.maksymtiutiunnyk.atmproject.manager;

import org.jspecify.annotations.NonNull;

import org.maksymtiutiunnyk.atmproject.service.AtmManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AtmLauncher implements CommandLineRunner {
    private final AtmManager atmManager;

    public AtmLauncher(AtmManager atmManager) {
        this.atmManager = atmManager;
    }

    @Override
    public void run(String @NonNull ... args) {
        atmManager.atmManager();
    }
}