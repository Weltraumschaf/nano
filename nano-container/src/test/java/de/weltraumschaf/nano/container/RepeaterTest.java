package de.weltraumschaf.nano.container;

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
        final Repeater sut = Repeater.of(1, 3);
        @SuppressWarnings("unchecked") final Callable<Boolean> callable = mock(Callable.class);
        when(callable.call()).thenReturn(Boolean.TRUE);

        sut.execute(callable);

        verify(callable, times(1)).call();
    }
}