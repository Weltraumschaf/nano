package de.weltraumschaf.nano.container;

import de.weltraumschaf.commons.validate.Validate;
import lombok.Getter;

import java.util.concurrent.Callable;

/**
 * Repeated the given "failing" {@link Callable} until a {@link #waitMillis wait period} and {@link #maxRetries retries}
 * is exceeded.
 * <p>
 * The callable is considered "failing", if it returns {@link Boolean#FALSE}. So the repeater will call it again and again,
 * if it returns {@link Boolean#FALSE}, until it runs out the maximum retries.
 * </p>
 *
 * @author Sven Strittmatter
 * @since 1.0.0
 */
final class Repeater {

    /**
     * Milliseconds to wait before invoking {@link Callable#call()}.
     */
    @Getter
    private final int waitMillis;

    /**
     * Maximum amount of retries before stop trying.
     */
    @Getter
    private final int maxRetries;

    /**
     * Dedicated constructor.
     * <p>
     * Use {@link #of(int, int)} to get a new instance.
     * </p>
     *
     * @param waitMillis milliseconds to wait before invoking the runnable, must be greater than 0
     * @param maxRetries maxim number of tries, must be greater than 0
     */
    private Repeater(final int waitMillis, final int maxRetries) {
        super();
        Validate.greaterThan(waitMillis, 0, "waitMillis");
        Validate.greaterThan(maxRetries, 0, "maxRetries");
        this.waitMillis = waitMillis;
        this.maxRetries = maxRetries;
    }

    /**
     * Factory method to create a new instance.
     *
     * @param waitMillis milliseconds to wait before invoking the callback, must be greater than 0
     * @param maxRetries maxim number of tries, must be greater than 0
     * @return never {@code null}, always new instance
     */
    static Repeater of(final int waitMillis, final int maxRetries) {
        return new Repeater(waitMillis, maxRetries);
    }

    /**
     * Executes the given {@link Callable} after {@link #waitMillis} for {@link #maxRetries maximum retries}.
     * <p>
     * The repeating loop exits before {@link #maxRetries} is exceeded, if the givne {@link Callable}
     * returns {@code true}.
     * </p>
     *
     * @param repeated not {@code null}
     * @return {@code true} if the callable returned {@code true} before exceeding the {@link #maxRetries}, else {@code false}
     * @throws Exception if the given {@link Callable} failed
     */
    boolean execute(final Callable<Boolean> repeated) throws Exception {
        Validate.notNull(repeated, "repeated");
        int currentRetries = 1;

        do {
            Thread.sleep(this.waitMillis);

            if (repeated.call()) {
                return true;
            }

            if (currentRetries == this.maxRetries) {
                break;
            }

            ++currentRetries;
        } while (true);

        return false;
    }
}
