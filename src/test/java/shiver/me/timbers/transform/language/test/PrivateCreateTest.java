package shiver.me.timbers.transform.language.test;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;

import static org.junit.Assert.assertEquals;
import static shiver.me.timbers.transform.language.test.ExceptionUtils.assertException;

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

        assertException(NoSuchMethodException.class, new PrivateCreateWithClass(NoSuchMethodExceptionTestClass.class)
        );
    }

    @Test
    public void testCreateWithDefaultConstructorThatThrowsAnException() {

        assertException(InvocationTargetException.class, new PrivateCreateWithClass(InvocationTargetExceptionTestClass.class)
        );
    }

    @Test
    public void testCreateWithPrivateConstructor() throws NoSuchMethodException {

        assertException(IllegalAccessException.class, new PrivateCreateWithConstructor(TestClass.class.getDeclaredConstructor())
        );
    }

    @Test
    public void testCreateWithInterface() {

        assertException(InstantiationException.class, new PrivateCreateWithClass(AbstractInstantiationExceptionTestClass.class)
        );
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

    private static class PrivateCreateWithClass implements Callable<Void> {

        private final Class type;

        private PrivateCreateWithClass(Class type) {

            this.type = type;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Void call() throws Exception {

            new PrivateCreate(type);

            return null;
        }
    }

    private static class PrivateCreateWithConstructor implements Callable<Void> {

        private final Constructor constructor;

        private PrivateCreateWithConstructor(Constructor constructor) {

            this.constructor = constructor;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Void call() throws Exception {

            new PrivateCreate(constructor);

            return null;
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
