package de.weltraumschaf.nano.container;

import de.weltraumschaf.commons.testing.DelayedRepeater;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link Container}.
 *
 * @author Sven Strittmatter
 * @since 1.0.0
 */
class ContainerTest {

    private final ModuleFinder finder = mock(ModuleFinder.class);
    private final ServiceFactory factory = mock(ServiceFactory.class);
    private final ContainerContext context = ContainerContext.create()
        .withFinder(finder)
        .withFactory(factory);
    private final Container sut = new Container(context);

    @AfterEach
    void stopContainer() {
        sut.stop();
    }

    @Test
    void isRunning_falseByDefault() {
        assertThat(sut.isRunning(), is(false));
    }

    @Test
    void isStopped_falseByDefault() {
        assertThat(sut.isStopped(), is(false));
    }

    @Test
    void start_isRunningIsTrue() throws InterruptedException {
        new Thread(sut::start).start();

        DelayedRepeater.create(5, 3)
            .execute(() -> assertThat(sut.isRunning(), is(true)));
    }

    @Test
    void start_isStoppedIsFalse() throws InterruptedException {
        new Thread(sut::start).start();

        DelayedRepeater.create(5, 3)
            .execute(() -> assertThat(sut.isStopped(), is(false)));
    }
}