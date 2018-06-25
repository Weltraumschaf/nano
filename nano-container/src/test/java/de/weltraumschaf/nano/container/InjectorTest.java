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
        sut.injectRequiredServices(null);
    }

    @Test
    public void injectRequiredServices() {
        sut.injectRequiredServices(requires);

        assertThat(requires.getOne(), is(one));
        assertThat(requires.getTwo(), is(two));
    }

    @Test
    public void findRequiredFields_null() {
        assertThat(sut.findRequiredFields(null), hasSize(0));
    }

    @Test
    public void findRequiredFields() {
        assertThat(sut.findRequiredFields(requires), hasSize(2));
    }

    interface ServiceWhichRequires extends Service {
    }

    static class DefaultServiceWhichRequires implements ServiceWhichRequires {
        @Require
        private RequiredServiceOne one;
        @Require
        private RequiredServiceTwo two;
        private Object three;
        private Object four;

        RequiredServiceOne getOne() {
            return one;
        }

        RequiredServiceTwo getTwo() {
            return two;
        }
    }

    interface RequiredServiceOne extends Service {
    }

    private static class DefaultRequiredServiceOne implements RequiredServiceOne {
    }

    interface RequiredServiceTwo extends Service {
    }

    private static class DefaultRequiredServiceTwo implements RequiredServiceTwo {
    }
}
