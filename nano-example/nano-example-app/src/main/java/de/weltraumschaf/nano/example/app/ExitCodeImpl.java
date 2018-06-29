package de.weltraumschaf.nano.example.app;

import de.weltraumschaf.commons.system.ExitCode;

/**
 * Defines exit codes.
 *
 * @since 1.0.0
 */
public enum ExitCodeImpl implements ExitCode {
    /**
     * Everything fine.
     */
    OK(0),
    /**
     * Something went wrong.
     */
    ERROR(256);

    private final int code;

    /**
     * Dedicated constructor.
     *
     * @param code any int
     */
    ExitCodeImpl(final int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }
}
