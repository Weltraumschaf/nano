package de.weltraumschaf.nano.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A field of a {@link Service service} annotated with this annotation signals the container that the service requires the filed as depended service.
 *
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( {ElementType.FIELD})
public @interface Require {
}
