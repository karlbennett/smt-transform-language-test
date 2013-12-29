package shiver.me.timbers.transform.language.test;

import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;
import org.reflections.Reflections;
import shiver.me.timbers.transform.Transformation;
import shiver.me.timbers.transform.antlr4.CompositeTokenTransformation;
import shiver.me.timbers.transform.antlr4.TokenApplier;
import shiver.me.timbers.transform.antlr4.TokenTransformation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public final class RuleAndTokenTestUtils {

    private RuleAndTokenTestUtils() {
    }

    private static final String APPLIER_STRING = "test apply string.";

    public static void testEachTransformationInPackage(String packageName) {

        final List<Class<TokenTransformation>> transformationTypes = listTransformationsInPackage(packageName);

        testEachTransformation(transformationTypes);
    }

    @SuppressWarnings("unchecked")
    private static List<Class<TokenTransformation>> listTransformationsInPackage(String packageName) {

        final Reflections reflections = new Reflections(packageName);

        final Set<Class<? extends CompositeTokenTransformation>> allTypeTransformationClasses =
                reflections.getSubTypesOf(CompositeTokenTransformation.class);

        final List<Class<TokenTransformation>> typeTransformationsClasses =
                new ArrayList<Class<TokenTransformation>>(allTypeTransformationClasses.size());

        for (Class type : allTypeTransformationClasses) {

            typeTransformationsClasses.add((Class<TokenTransformation>) type);
        }

        return Collections.unmodifiableList(typeTransformationsClasses);
    }

    public static void testEachTransformation(List<Class<TokenTransformation>> transformationTypes) {

        for (Class<TokenTransformation> type : transformationTypes) {

            TokenApplier mockApplier = buildMockApplier();

            TokenTransformation transformation = newTransformation(type, mockApplier);

            assertEquals(staticName(transformation), transformation.getName());

            assertEquals(APPLIER_STRING, transformation.apply(mock(RuleContext.class), mock(Token.class),
                    APPLIER_STRING));

            verify(mockApplier, times(1)).apply(any(RuleContext.class), any(Token.class), eq(APPLIER_STRING));
        }
    }

    private static TokenApplier buildMockApplier() {

        final TokenApplier mockApplier = mock(TokenApplier.class);
        when(mockApplier.apply(any(RuleContext.class), any(Token.class), eq(APPLIER_STRING)))
                .thenReturn(APPLIER_STRING);

        return mockApplier;
    }

    private static TokenTransformation newTransformation(Class<TokenTransformation> type, TokenApplier applier) {

        final Constructor<TokenTransformation> constructor;
        try {

            constructor = type.getConstructor(TokenApplier.class);

            return constructor.newInstance(applier);

        } catch (NoSuchMethodException e) {

            throw new RuntimeException(e);

        } catch (InvocationTargetException e) {

            throw new RuntimeException(e);

        } catch (InstantiationException e) {

            throw new RuntimeException(e);

        } catch (IllegalAccessException e) {

            throw new RuntimeException(e);
        }
    }

    private static String staticName(Transformation transformation) {

        try {
            final Field staticName = transformation.getClass().getField("NAME");

            return staticName.get(null).toString();

        } catch (NoSuchFieldException e) {

            throw new RuntimeException(e);

        } catch (IllegalAccessException e) {

            throw new RuntimeException(e);
        }
    }
}
