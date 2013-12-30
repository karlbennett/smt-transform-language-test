package shiver.me.timbers.transform.language.test;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class PrivateCreateTest {

    private static final String TEST_STRING = "test string";

    @Test
    public void testCreateType() {

        new PrivateCreate<TestClass>(TestClass.class);
    }

    @Test
    public void testCreateConstructor() throws NoSuchMethodException {

        new PrivateCreate<Object>(Object.class.getConstructor());
    }

    @Test
    public void testCreateWithNoDefaultConstructor() {

        assertException(NoSuchMethodExceptionTestClass.class, NoSuchMethodException.class);
    }

    @Test
    public void testCreateWithDefaultConstructorThatThrowsAnException() {

        assertException(InvocationTargetExceptionTestClass.class, InvocationTargetException.class);
    }

    @Test
    public void testCreateWithPrivateConstructor() throws NoSuchMethodException {

        assertException(TestClass.class.getDeclaredConstructor(), IllegalAccessException.class);
    }

    @Test
    public void testCreateWithInterface() {

        assertException(AbstractInstantiationExceptionTestClass.class, InstantiationException.class);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateWithNullType() {

        new PrivateCreate<TestClass>((Class<TestClass>) null);
    }

    @Test
    public void testGetInstance() {

        final TestClass testClass = new PrivateCreate<TestClass>(TestClass.class).getInstance();

        assertEquals("the test class should be instantiated.", TEST_STRING, testClass.getString());
    }

    @SuppressWarnings("unchecked")
    private static void assertException(Class objectToInstantiate, Class<? extends Exception> exceptionType) {

        try {

            new PrivateCreate(objectToInstantiate);

        } catch (RuntimeException e) {

            assertThat("a " + exceptionType.getSimpleName() + " should be thrown", e.getCause(),
                    instanceOf(exceptionType));
        }
    }

    @SuppressWarnings("unchecked")
    private static void assertException(Constructor constructor, Class<? extends Exception> exceptionType) {

        try {

            new PrivateCreate(constructor);

        } catch (RuntimeException e) {

            assertThat("a " + exceptionType.getSimpleName() + " should be thrown", e.getCause(),
                    instanceOf(exceptionType));
        }
    }

    private static class TestClass {

        private final String string;

        private TestClass() {

            this.string = TEST_STRING;
        }

        private String getString() {

            return string;
        }
    }

    private static class NoSuchMethodExceptionTestClass {

        @SuppressWarnings("UnusedDeclaration")
        public NoSuchMethodExceptionTestClass(Object noDefaultConstructor) {
        }
    }

    private static class InvocationTargetExceptionTestClass {

        public InvocationTargetExceptionTestClass() {

            throw new RuntimeException("this should produce an InvocationTargetException");
        }
    }

    private static abstract class AbstractInstantiationExceptionTestClass {
    }
}
