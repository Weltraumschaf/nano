package de.weltraumschaf.nano.example.module1.impl.echo;

import de.weltraumschaf.commons.validate.Validate;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Helper class to convert {@link String strings}.
 *
 * @author Sven Strittmatter
 * @since 1.0.0
 */
final class StringConverter {
    private static final Charset ENCODING = Charset.forName("UTF-8");

    /**
     * Converts the given {@link String string} into a {@link ByteBuffer buffer}.
     *
     * @param input not {@code null}
     * @return never {@code null}
     */
    static ByteBuffer toBuffer(final String input) {
        Validate.notNull(input, "input");
        return ByteBuffer.wrap(input.getBytes(ENCODING));
    }

    /**
     * Converts the given {@link ByteBuffer buffer} into a {@link String string}.
     *
     * @param input not {@code null}
     * @return never {@code null}
     */
    static String fromBuffer(final ByteBuffer input) {
        Validate.notNull(input, "input");
        input.rewind();
        final byte[] bytes = new byte[input.remaining()];
        input.get(bytes, 0, input.remaining());

        return new String(bytes, ENCODING);
    }
}
