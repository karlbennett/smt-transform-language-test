package shiver.me.timbers.transform.language.test;

import java.util.concurrent.Callable;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

/**
 * This class contains assertions to help verify exceptions.
 */
public class ExceptionUtils {

    /**
     * Assert that the supplied callable produces the required exception.
     */
    public static void assertException(Class<? extends Exception> exceptionType, Callable callable) {

        try {

            callable.call();

        } catch (Exception e) {

            assertThat("a " + exceptionType.getSimpleName() + " should be thrown", e.getCause(),
                    instanceOf(exceptionType));
        }
    }
}
