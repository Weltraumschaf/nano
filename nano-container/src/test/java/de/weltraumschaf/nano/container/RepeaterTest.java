package de.weltraumschaf.nano.container;

import de.weltraumschaf.commons.testing.DelayedRepeater;
import org.junit.Test;

import java.util.concurrent.Callable;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests for {@literal Repeater}.
 */
public class RepeaterTest {
    @Test
    public void of() {
        final Repeater sut = Repeater.of(1_000, 10);

        assertThat(sut.getWaitMillis(), is(1_000));
        assertThat(sut.getMaxRetries(), is(10));
    }

    @Test
    public void execute_callableOneTimesIfItReturnsTrueOnFirstCall() throws Exception {
        final Repeater sut = Repeater.of(5, 3);
        @SuppressWarnings("unchecked") final Callable<Boolean> callable = mock(Callable.class);
        when(callable.call()).thenReturn(Boolean.TRUE);

        assertThat(sut.execute(callable), is(true));

        DelayedRepeater.create(10, 3).execute(() -> {
            verify(callable, times(1)).call();
            return null;
        });
    }

    @Test
    public void execute_callableTwoTimesIfItReturnsFalseOnFirstCall() throws Exception {
        final Repeater sut = Repeater.of(5, 3);
        @SuppressWarnings("unchecked") final Callable<Boolean> callable = mock(Callable.class);
        when(callable.call())
            .thenReturn(Boolean.FALSE)
            .thenReturn(Boolean.TRUE);

        assertThat(sut.execute(callable), is(true));

        DelayedRepeater.create(10, 3).execute(() -> {
            verify(callable, times(2)).call();
            return null;
        });
    }

    @Test
    public void execute_callableThreeTimesIfItReturnsFalseOnFirstAndSecondCall() throws Exception {
        final Repeater sut = Repeater.of(5, 3);
        @SuppressWarnings("unchecked") final Callable<Boolean> callable = mock(Callable.class);
        when(callable.call())
            .thenReturn(Boolean.FALSE)
            .thenReturn(Boolean.FALSE)
            .thenReturn(Boolean.TRUE);

        assertThat(sut.execute(callable), is(true));

        DelayedRepeater.create(10, 3).execute(() -> {
            verify(callable, times(3)).call();
            return null;
        });
    }

    @Test
    public void execute_retriesExceeded() throws Exception {
        final Repeater sut = Repeater.of(5, 3);
        @SuppressWarnings("unchecked") final Callable<Boolean> callable = mock(Callable.class);
        when(callable.call())
            .thenReturn(Boolean.FALSE)
            .thenReturn(Boolean.FALSE)
            .thenReturn(Boolean.FALSE);

        assertThat(sut.execute(callable), is(false));

        DelayedRepeater.create(10, 3).execute(() -> {
            verify(callable, times(3)).call();
            return null;
        });
    }
}