package shiver.me.timbers.transform.language.test;

import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;
import shiver.me.timbers.transform.antlr4.TokenApplier;
import shiver.me.timbers.transform.antlr4.TokenTransformation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static shiver.me.timbers.transform.language.test.TransformationsUtils.buildTransformation;

public final class TestUtils {

    private TestUtils() {
    }

    public static void assertCorrectTransformation(List<TestTokenTransformation> transformations) {

        assertThat("some transformations should be produced.", transformations, not(empty()));
        assertThat("one transformation should be produced.", transformations, hasSize(1));
        assertThat("the produced transformation should be correct.", transformations.get(0),
                instanceOf(TestTokenTransformation.class));
    }

    public static void assertCorrectTransformationTypes(List<Class<TestTokenTransformation>> transformationTypes) {

        assertThat("some transformationTypes should be produced.", transformationTypes, not(empty()));
        assertThat("one transformation type should be produced.", transformationTypes, hasSize(1));
        assertEquals("the produced transformation type should be correct.", transformationTypes.get(0),
                TestTokenTransformation.class);
    }

    public static void assertCorrectApplier(TokenApplier expected, TokenApplier actual) {

        final RuleContext context = mock(RuleContext.class);
        final Token token = mock(Token.class);

        assertEquals("the applier should produce the correct result.", expected.apply(context, token, ""),
                actual.apply(context, token, ""));
    }

    @SuppressWarnings("unchecked")
    public static Collection<Class<TestTokenTransformation>> transformationTypes() {

        return asList(TestTokenTransformation.class);
    }

    @SuppressWarnings("unchecked")
    public static Collection<Class<TestTokenTransformation>> invalidTransformationTypes() {

        final Set<Class<TestTokenTransformation>> transformationTypes = new HashSet<Class<TestTokenTransformation>>(1);

        @SuppressWarnings("UnnecessaryLocalVariable")
        final Set looseTransformationTypes = transformationTypes;
        looseTransformationTypes.add(Object.class);

        return transformationTypes;
    }

    public static TokenApplier mockTokenApplier() {

        final TokenApplier applier = mock(TokenApplier.class);
        when(applier.apply(any(RuleContext.class), any(Token.class), anyString())).thenReturn("applier string");

        return applier;
    }

    @SuppressWarnings("unchecked")
    public static <T extends TokenTransformation> TokenApplierBuilder<T> mockTokenApplierBuilder(
            TokenApplier applier) {

        final TokenApplierBuilder<T> tokenApplierBuilder = mock(TokenApplierBuilder.class);
        when(tokenApplierBuilder.build(any(Class.class))).thenReturn(applier);

        return tokenApplierBuilder;
    }

    public static class BuildTransformationWithClass<T extends TokenTransformation> implements Callable<Void> {

        private final Class<T> type;
        private final TokenApplierBuilder<T> tokenApplierBuilder;

        public BuildTransformationWithClass(Class<T> type, TokenApplierBuilder<T> tokenApplierBuilder) {

            this.type = type;
            this.tokenApplierBuilder = tokenApplierBuilder;
        }

        @Override
        public Void call() throws Exception {

            buildTransformation(type, tokenApplierBuilder);

            return null;
        }
    }

    public static class BuildTransformationWithConstructor<T extends TokenTransformation> implements Callable<Void> {

        private final Class<T> type;
        private final Constructor<T> constructor;
        private final TokenApplierBuilder<T> tokenApplierBuilder;

        public BuildTransformationWithConstructor(Class<T> type, Constructor<T> constructor, TokenApplierBuilder<T> tokenApplierBuilder) {

            this.type = type;
            this.constructor = constructor;
            this.tokenApplierBuilder = tokenApplierBuilder;
        }

        @Override
        public Void call() throws Exception {

            buildTransformation(type, constructor, tokenApplierBuilder);

            return null;
        }
    }

    public static class NameTokenApplierBuilderWithType<T extends TokenTransformation> implements Callable<Void> {

        private final Class<T> type;

        public NameTokenApplierBuilderWithType(Class<T> type) {

            this.type = type;
        }

        @Override
        public Void call() throws Exception {

            new NameTokenApplierBuilder<T>().build(type);

            return null;
        }
    }

    public static class NameTokenApplierBuilderWithField<T extends TokenTransformation> implements Callable<Void> {

        private final Field field;

        public NameTokenApplierBuilderWithField(Field field) {

            this.field = field;
        }

        @Override
        public Void call() throws Exception {

            new NameTokenApplierBuilder<T>().build(field);

            return null;
        }
    }

    public static class DefaultTokenTransformation implements TokenTransformation {

        @Override
        public String apply(RuleContext context, Token token, String string) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getName() {
            throw new UnsupportedOperationException();
        }
    }

    public static class InvocationTargetExceptionClass extends DefaultTokenTransformation {

        @SuppressWarnings("UnusedParameters")
        public InvocationTargetExceptionClass(TokenApplier applier) {

            throw new RuntimeException("this should produce an InvocationTargetException.");
        }
    }

    public static abstract class InstantiationExceptionClass extends DefaultTokenTransformation {

        @SuppressWarnings("UnusedParameters")
        public InstantiationExceptionClass(TokenApplier applier) {
        }
    }

    public static abstract class IllegalAccessExceptionClass extends DefaultTokenTransformation {

        @SuppressWarnings("UnusedDeclaration")
        private static final String NAME = "illegal_access";

        @SuppressWarnings("UnusedDeclaration")
        private IllegalAccessExceptionClass(TokenApplier applier) {
        }
    }
}
