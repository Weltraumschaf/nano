package de.weltraumschaf.nano.example.module1.impl;

import de.weltraumschaf.nano.example.module1.api.HelperService;

import java.util.UUID;

/**
 * @author Sven Strittmatter
 * @since 1.0.0
 */
public final class DefaultHelperService implements HelperService {
    @Override
    public String help() {
        return UUID.randomUUID().toString();
    }
}
