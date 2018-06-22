package de.weltraumschaf.nano.example.impl;

import de.weltraumschaf.nano.api.ServiceAdapter;
import de.weltraumschaf.nano.example.api.ExampleService;

/**
 *
 */
public final class DefaultExampleService extends ServiceAdapter implements ExampleService {

    @Override
    public void activate() throws Exception {
        System.out.println("Service activated.");
    }

    @Override
    public void deactivate() throws Exception {
        System.out.println("Service deactivated.");
    }

    @Override
    public void hello() {
        System.out.println("Hello, World!");
    }
}
