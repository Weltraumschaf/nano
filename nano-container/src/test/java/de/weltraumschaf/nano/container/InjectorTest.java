package de.weltraumschaf.nano.container;

import de.weltraumschaf.nano.api.Require;
import de.weltraumschaf.nano.api.Service;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link Injector}.
 */
public class InjectorTest {
    private final DefaultRequiredServiceOne one = new DefaultRequiredServiceOne();
    private final DefaultRequiredServiceTwo two = new DefaultRequiredServiceTwo();
    private final DefaultServiceWhichRequires requires = new DefaultServiceWhichRequires();
    private final Injector sut = new Injector(new Services(Arrays.asList(
        one,
        two,
        requires
    )));

    @Test(expected = NullPointerException.class)
    public void injectRequiredServices_notNull() {
        sut.injectRequired(null);
    }

    @Test
    public void injectRequired() {
        sut.injectRequired(requires);

        assertThat(requires.one, is(one));
        assertThat(requires.two, is(two));
    }

    @Test
    public void findRequiredFields_null() {
        assertThat(sut.findRequiredFields(null), hasSize(0));
    }

    @Test
    public void findRequiredFields() {
        assertThat(sut.findRequiredFields(requires.getClass()), hasSize(2));
    }

    @Test
    public void findRequiredFields_throughClassHierarchy() {
        assertThat(sut.findRequiredFields(Foo.class), hasSize(3));
    }

    interface ServiceWhichRequires extends Service {
    }

    private static class DefaultServiceWhichRequires implements ServiceWhichRequires {
        @Require
        private RequiredServiceOne one;
        @Require
        private RequiredServiceTwo two;
        private Object three;
        private Object four;
    }

    interface RequiredServiceOne extends Service {
    }

    private static class DefaultRequiredServiceOne implements RequiredServiceOne {
    }

    interface RequiredServiceTwo extends Service {
    }

    private static class DefaultRequiredServiceTwo implements RequiredServiceTwo {
    }

    private static class Foo extends Bar {
        @Require
        private Object foo;
    }

    private static class Bar extends Baz {
        @Require
        private Object bar;
    }

    private static class Baz {
        @Require
        private Object baz;
    }
}
