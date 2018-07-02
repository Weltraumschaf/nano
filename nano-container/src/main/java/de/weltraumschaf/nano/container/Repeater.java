package de.weltraumschaf.nano.container;

import de.weltraumschaf.commons.validate.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * Repeated the given {@link Callable} until a {@link #waitMillis wait period} and {@link #maxRetries retries} is exceeded.
 *
 * @author Sven Strittmatter
 * @since 1.0.0
 */
final class Repeater {

    private static final Logger LOG = LoggerFactory.getLogger(Repeater.class);
    /**
     * Milliseconds to wait before invoking {@link Callable#call()}.
     */
    private final int waitMillis;

    /**
     * Maximum amount of retries before failing.
     */
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
     * @throws Exception if the given {@link Callable} failed
     */
    void execute(final Callable<Boolean> repeated) throws Exception {
        Validate.notNull(repeated, "repeated");
        int currentRetries = 1;

        while (true) {
            Thread.sleep(this.waitMillis);

            if (repeated.call()) {
                break;
            }

            if (currentRetries == this.maxRetries) {
                break;
            }
        }

        ++currentRetries;
    }
}
