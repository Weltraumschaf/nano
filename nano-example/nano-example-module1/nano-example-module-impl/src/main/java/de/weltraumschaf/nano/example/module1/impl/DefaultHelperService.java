package de.weltraumschaf.nano.example.module1.impl;

import de.weltraumschaf.nano.example.module1.api.HelperService;

import java.util.UUID;

/**
 *
 */
public final class DefaultHelperService implements HelperService {
    @Override
    public String help() {
        return UUID.randomUUID().toString();
    }
}
