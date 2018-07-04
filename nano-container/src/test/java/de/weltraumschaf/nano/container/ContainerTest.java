package de.weltraumschaf.nano.container;

import de.weltraumschaf.commons.testing.DelayedRepeater;
import lombok.ToString;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link Container}.
 *
 * @author Sven Strittmatter
 * @since 1.0.0
 */
public class ContainerTest {

    private final ModuleFinder finder = mock(ModuleFinder.class);
    private final ServiceFactory factory = mock(ServiceFactory.class);
    private final ContainerContext context = ContainerContext.create()
        .withFinder(finder)
        .withFactory(factory);
    private final Container sut = new Container(context);

    @After
    public void stopContainer() {
        sut.stop();
    }

    @Test
    public void isRunning_falseByDefault() {
        assertThat(sut.isRunning(), is(false));
    }

    @Test
    public void isStopped_falseByDefault() {
        assertThat(sut.isStopped(), is(false));
    }

    @Test
    public void start_isRunningIsTrue() throws InterruptedException {
        new Thread(sut::start).start();

        DelayedRepeater.create(5, 3).execute(() -> {
            assertThat(sut.isRunning(), is(true));
        });
    }

    @Test
    public void start_isStoppedIsFalse() throws InterruptedException {
        new Thread(sut::start).start();

        DelayedRepeater.create(5, 3).execute(() -> {
            assertThat(sut.isStopped(), is(false));
        });
    }
}