package shiver.me.timbers.transform.language.test;

import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;
import shiver.me.timbers.transform.Transformation;
import shiver.me.timbers.transform.antlr4.CompositeTokenTransformation;
import shiver.me.timbers.transform.antlr4.TokenApplier;
import shiver.me.timbers.transform.antlr4.TokenTransformation;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static shiver.me.timbers.transform.language.test.TransformationsUtils.buildTransformations;
import static shiver.me.timbers.transform.language.test.TransformationsUtils.listTransformationsInPackage;

/**
 * This class contains methods that help with automatically testing all the different
 * {@link CompositeTokenTransformation} implementations.
 */
public final class RuleAndTokenTestUtils {

    private RuleAndTokenTestUtils() {
    }

    private static final String APPLIER_STRING = "test apply string.";

    private static final Field APPLIER_FIELD = getTokenApplierField();

    static Field getTokenApplierField() {

        return getTokenApplierField(CompositeTokenTransformation.class);
    }

    static <T extends CompositeTokenTransformation> Field getTokenApplierField(Class<T> type) {

        try {

            final Field applierField = type.getDeclaredField("applier");
            applierField.setAccessible(true);

            return applierField;

        } catch (NoSuchFieldException e) {

            throw new RuntimeException(e);
        }
    }

    /**
     * Test all the implementations of {@link CompositeTokenTransformation} that reside under the given package name.
     */
    public static void testEachTransformationInPackage(String packageName) {

        final List<Class<TokenTransformation>> transformationTypes = listTransformationsInPackage(packageName);

        final List<TokenTransformation> transformations = buildTransformations(transformationTypes,
                new TokenApplierBuilder() {

                    @Override
                    public TokenApplier build(Class<TokenTransformation> type) {

                        return buildMockApplier();
                    }
                });

        testEachTransformation(transformations);
    }

    static void testEachTransformation(List<TokenTransformation> transformations) {

        testEachTransformation(APPLIER_FIELD, transformations);
    }

    static void testEachTransformation(Field applierField, List<TokenTransformation> transformations) {

        if (0 == transformations.size()) {

            throw new AssertionError("no transformations found.");
        }

        for (TokenTransformation transformation : transformations) {

            assertEquals(staticName(transformation), transformation.getName());

            assertEquals(APPLIER_STRING, transformation.apply(mock(RuleContext.class), mock(Token.class),
                    APPLIER_STRING));

            try {

                verify((TokenApplier) applierField.get(transformation), times(1))
                        .apply(any(RuleContext.class), any(Token.class), eq(APPLIER_STRING));

            } catch (IllegalAccessException e) {

                throw new RuntimeException(e);
            }
        }
    }

    static TokenApplier buildMockApplier() {

        final TokenApplier mockApplier = mock(TokenApplier.class);
        when(mockApplier.apply(any(RuleContext.class), any(Token.class), eq(APPLIER_STRING)))
                .thenReturn(APPLIER_STRING);

        return mockApplier;
    }

    static String staticName(Transformation transformation) {

        try {
            final Field staticNameField = transformation.getClass().getField("NAME");

            return staticName(staticNameField);

        } catch (NoSuchFieldException e) {

            throw new RuntimeException(e);

        }
    }

    static String staticName(Field staticNameField) {

        try {
            return staticNameField.get(null).toString();

        } catch (IllegalAccessException e) {

            throw new RuntimeException(e);
        }
    }
}
