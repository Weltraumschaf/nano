package de.weltraumschaf.nano.container;

import de.weltraumschaf.commons.system.ExitCode;

/**
 * Defines exit codes.
 *
 * @since 1.0.0
 */
public enum ExitCodeImpl implements ExitCode {
    OK(0), ERROR(1);

    private final int code;

    ExitCodeImpl(final int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }
}
