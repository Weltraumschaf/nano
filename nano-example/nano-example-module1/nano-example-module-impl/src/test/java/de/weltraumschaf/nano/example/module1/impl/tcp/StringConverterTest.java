package de.weltraumschaf.nano.example.module1.impl.tcp;

import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link StringConverter}.
 *
 * @author Sven Strittmatter
 * @since 1.0.0
 */
public class StringConverterTest {

    @Test
    public void toBuffer() {
        assertThat(StringConverter.toBuffer("hello").array(), is(new byte[] {0x68, 0x65, 0x6c, 0x6c, 0x6f}));
    }

    @Test(expected = NullPointerException.class)
    public void tooBuffer_nullGiven() {
        StringConverter.toBuffer(null);
    }

    @Test
    public void toBuffer_emptyGiven() {
        assertThat(StringConverter.toBuffer("").array(), is(new byte[] {}));
    }

    @Test
    public void fromBuffer() {
        assertThat(StringConverter.fromBuffer(ByteBuffer.wrap(new byte[] {0x68, 0x65, 0x6c, 0x6c, 0x6f})), is("hello"));
    }

    @Test(expected = NullPointerException.class)
    public void fromBuffer_nullGiven() {
        StringConverter.fromBuffer(null);
    }
}