package de.weltraumschaf.nano.container;

import de.weltraumschaf.nano.api.messaging.MessageBus;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link DefaultServiceContext}.
 *
 * @author Sven Strittmatter
 * @since 1.0.0
 */
public class DefaultServiceContextTest {

    private final MessageBus messages = mock(MessageBus.class);
    private final DefaultServiceContext sut = new DefaultServiceContext(messages);

    @Test
    public void equalsAndHashCode() {
        EqualsVerifier.forClass(DefaultServiceContext.class).verify();
    }

    @Test
    public void getMessages() {
        assertThat(sut.getMessages(), is(messages));
    }

    @Test
    public void string() {
        assertThat(sut.toString(), startsWith("DefaultServiceContext(messages="));
    }
}