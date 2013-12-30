package shiver.me.timbers.transform.language.test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * This class can be used to call the private constructor of another class.
 */
public class PrivateCreate<T> {

    private final T instance;

    public PrivateCreate(Class<T> type) {

        this(getAccessibleConstructor(type));
    }

    PrivateCreate(Constructor<T> constructor) {

        try {

            instance = constructor.newInstance();

        } catch (InvocationTargetException e) {

            throw new RuntimeException(e);

        } catch (InstantiationException e) {

            throw new RuntimeException(e);

        } catch (IllegalAccessException e) {

            throw new RuntimeException(e);
        }
    }

    private static <T> Constructor<T> getAccessibleConstructor(Class<T> type) {

        try {

            final Constructor<T> constructor = type.getDeclaredConstructor();
            constructor.setAccessible(true);

            return constructor;

        } catch (NoSuchMethodException e) {

            throw new RuntimeException(e);
        }
    }

    public T getInstance() {

        return instance;
    }
}
