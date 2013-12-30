package shiver.me.timbers.transform.language.test;

import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;
import org.junit.Test;
import shiver.me.timbers.transform.antlr4.CompositeTokenTransformation;
import shiver.me.timbers.transform.antlr4.TokenApplier;
import shiver.me.timbers.transform.antlr4.TokenTransformation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
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
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static shiver.me.timbers.transform.language.test.ExceptionUtils.assertException;
import static shiver.me.timbers.transform.language.test.PackageConstants.INVALID_PACKAGE_NAME;
import static shiver.me.timbers.transform.language.test.PackageConstants.VALID_PACKAGE_NAME;
import static shiver.me.timbers.transform.language.test.TransformationsUtils.NameTokenApplierBuilder;
import static shiver.me.timbers.transform.language.test.TransformationsUtils.buildTransformation;
import static shiver.me.timbers.transform.language.test.TransformationsUtils.buildTransformations;
import static shiver.me.timbers.transform.language.test.TransformationsUtils.buildWrappingTransformations;
import static shiver.me.timbers.transform.language.test.TransformationsUtils.buildWrappingTransformationsFromPackageName;
import static shiver.me.timbers.transform.language.test.TransformationsUtils.listTransformationsInPackage;

public class TransformationsUtilsTest {

    @Test
    public void testCreate() {

        new PrivateCreate<TransformationsUtils>(TransformationsUtils.class);
    }

    @Test
    public void testBuildWrappingTransformationsFromPackageNameWithValidPackageName() {

        final List<TestTokenTransformation> transformations =
                buildWrappingTransformationsFromPackageName(VALID_PACKAGE_NAME);

        assertCorrectTransformation(transformations);
    }

    @Test
    public void testBuildWrappingTransformationsFromPackageNameWithInvalidPackageName() {

        final List<TestTokenTransformation> transformations =
                buildWrappingTransformationsFromPackageName(INVALID_PACKAGE_NAME);

        assertThat("no transformations should be produced.", transformations, empty());
    }

    @Test
    public void testBuildWrappingTransformationsFromPackageNameWithNullPackageName() {

        final List<TestTokenTransformation> transformations = buildWrappingTransformationsFromPackageName(null);

        assertCorrectTransformation(transformations);
    }

    @Test
    public void testListTransformationsInPackageWithValidPackageName() {

        final List<Class<TestTokenTransformation>> transformationTypes =
                listTransformationsInPackage(VALID_PACKAGE_NAME);

        assertCorrectTransformationTypes(transformationTypes);
    }

    @Test
    public void testListTransformationsInPackageWithInvalidPackageName() {

        final List<Class<CompositeTokenTransformation>> transformationTypes =
                listTransformationsInPackage(INVALID_PACKAGE_NAME);

        assertThat("no transformation types should be produced.", transformationTypes, empty());
    }

    @Test
    public void testListTransformationsInPackageWithNullPackageName() {

        final List<Class<TestTokenTransformation>> transformationTypes = listTransformationsInPackage(null);

        assertCorrectTransformationTypes(transformationTypes);
    }

    @Test
    public void testBuildWrappingTransformationsWithTransformation() {

        final List<TestTokenTransformation> transformations = buildWrappingTransformations(transformationTypes());

        assertCorrectTransformation(transformations);
    }

    @Test
    public void testBuildWrappingTransformationsWithNoTransformation() {

        final List<TestTokenTransformation> transformations =
                buildWrappingTransformations(Collections.<Class<TestTokenTransformation>>emptySet());

        assertThat("no transformations should be produced.", transformations, empty());
    }

    @Test(expected = RuntimeException.class)
    public void testBuildWrappingTransformationsWithInvalidType() {

        buildWrappingTransformations(invalidTransformationTypes());
    }

    @Test(expected = NullPointerException.class)
    public void testBuildWrappingTransformationsWithNullTypes() {

        buildWrappingTransformations(null);
    }

    @Test
    public void testBuildTransformationsWithValidTransformationTypeAndTokenApplierBuilder() {

        final TokenApplier applier = mockTokenApplier();

        final List<TestTokenTransformation> transformations = buildTransformations(transformationTypes(),
                TransformationsUtilsTest.<TestTokenTransformation>mockTokenApplierBuilder(applier));

        assertCorrectApplier(applier, transformations.get(0));
    }

    @Test(expected = RuntimeException.class)
    public void testBuildTransformationsWithInvalidTransformationType() {

        buildTransformations(invalidTransformationTypes(),
                TransformationsUtilsTest.<TestTokenTransformation>mockTokenApplierBuilder(mockTokenApplier()));
    }

    @Test(expected = NullPointerException.class)
    public void testBuildTransformationsWithNullTransformationType() {

        buildTransformations(null,
                TransformationsUtilsTest.<TestTokenTransformation>mockTokenApplierBuilder(mockTokenApplier()));
    }

    @Test(expected = RuntimeException.class)
    @SuppressWarnings("unchecked")
    public void testBuildTransformationsWithInvalidTokenApplierBuilder() {

        buildTransformations(transformationTypes(), mock(TokenApplierBuilder.class));
    }

    @Test(expected = NullPointerException.class)
    public void testBuildTransformationsWithNullTokenApplierBuilder() {

        buildTransformations(transformationTypes(), null);
    }

    @Test
    public void testBuildTransformationWithValidTransformationTypeAndTokenApplierBuilder() {

        final TokenApplier applier = mockTokenApplier();

        final TestTokenTransformation transformation = buildTransformation(TestTokenTransformation.class,
                TransformationsUtilsTest.<TestTokenTransformation>mockTokenApplierBuilder(applier));

        assertCorrectApplier(applier, transformation);
    }

    @Test
    public void testBuildTransformationWithNoTokenApplierConstructor() {

        final TokenApplier applier = mockTokenApplier();

        assertException(NoSuchMethodException.class,
                new BuildTransformationWithClass<CompositeTokenTransformation>(CompositeTokenTransformation.class,
                        TransformationsUtilsTest.<CompositeTokenTransformation>mockTokenApplierBuilder(applier)));
    }

    @Test
    public void testBuildTransformationWithTokenApplierConstructorThatThrowsAnException() {

        final TokenApplier applier = mockTokenApplier();

        assertException(InvocationTargetException.class,
                new BuildTransformationWithClass<InvocationTargetExceptionClass>(InvocationTargetExceptionClass.class,
                        TransformationsUtilsTest.<InvocationTargetExceptionClass>mockTokenApplierBuilder(applier)));
    }

    @Test
    public void testBuildTransformationWithAbstractClass() {

        final TokenApplier applier = mockTokenApplier();

        assertException(InstantiationException.class,
                new BuildTransformationWithClass<InstantiationExceptionClass>(InstantiationExceptionClass.class,
                        TransformationsUtilsTest.<InstantiationExceptionClass>mockTokenApplierBuilder(applier)));
    }

    @Test
    public void testBuildTransformationWithPrivateTokenApplierConstructor() throws NoSuchMethodException {

        final TokenApplier applier = mockTokenApplier();

        assertException(IllegalAccessException.class,
                new BuildTransformationWithConstructor<IllegalAccessExceptionClass>(IllegalAccessExceptionClass.class,
                        IllegalAccessExceptionClass.class.getDeclaredConstructor(TokenApplier.class),
                        TransformationsUtilsTest.<IllegalAccessExceptionClass>mockTokenApplierBuilder(applier)));
    }

    @Test
    public void testNameTokenApplierBuilderWithValidTransformationType() {

        final NameTokenApplierBuilder<TestTokenTransformation> tokenApplierBuilder =
                new NameTokenApplierBuilder<TestTokenTransformation>();

        assertCorrectApplier(new WrappingTokenApplier(TestTokenTransformation.NAME),
                tokenApplierBuilder.build(TestTokenTransformation.class));
    }

    @Test
    public void testNameTokenApplierBuilderWithInvalidTransformationType() {

        assertException(NoSuchFieldException.class,
                new NameTokenApplierBuilderWithType<TokenTransformation>(TokenTransformation.class));
    }

    @Test
    public void testNameTokenApplierBuilderWithPrivateName() throws NoSuchFieldException {

        assertException(IllegalAccessException.class,
                new NameTokenApplierBuilderWithField<IllegalAccessExceptionClass>(
                        IllegalAccessExceptionClass.class.getDeclaredField("NAME")));
    }

    private static void assertCorrectTransformation(List<TestTokenTransformation> transformations) {

        assertThat("some transformations should be produced.", transformations, not(empty()));
        assertThat("one transformation should be produced.", transformations, hasSize(1));
        assertThat("the produced transformation should be correct.", transformations.get(0),
                instanceOf(TestTokenTransformation.class));
    }

    private static void assertCorrectTransformationTypes(List<Class<TestTokenTransformation>> transformationTypes) {

        assertThat("some transformationTypes should be produced.", transformationTypes, not(empty()));
        assertThat("one transformation type should be produced.", transformationTypes, hasSize(1));
        assertEquals("the produced transformation type should be correct.", transformationTypes.get(0),
                TestTokenTransformation.class);
    }

    private static void assertCorrectApplier(TokenApplier expected, TokenApplier actual) {

        final RuleContext context = mock(RuleContext.class);
        final Token token = mock(Token.class);

        assertEquals("the applier should produce the correct result.", expected.apply(context, token, ""),
                actual.apply(context, token, ""));
    }

    @SuppressWarnings("unchecked")
    private static Collection<Class<TestTokenTransformation>> transformationTypes() {

        return asList(TestTokenTransformation.class);
    }

    @SuppressWarnings("unchecked")
    private static Collection<Class<TestTokenTransformation>> invalidTransformationTypes() {

        final Set<Class<TestTokenTransformation>> transformationTypes = new HashSet<Class<TestTokenTransformation>>(1);

        @SuppressWarnings("UnnecessaryLocalVariable")
        final Set looseTransformationTypes = transformationTypes;
        looseTransformationTypes.add(Object.class);

        return transformationTypes;
    }

    private static TokenApplier mockTokenApplier() {

        final TokenApplier applier = mock(TokenApplier.class);
        when(applier.apply(any(RuleContext.class), any(Token.class), anyString())).thenReturn("applier string");

        return applier;
    }

    @SuppressWarnings("unchecked")
    private static <T extends TokenTransformation> TokenApplierBuilder<T> mockTokenApplierBuilder(
            TokenApplier applier) {

        final TokenApplierBuilder<T> tokenApplierBuilder = mock(TokenApplierBuilder.class);
        when(tokenApplierBuilder.build(any(Class.class))).thenReturn(applier);

        return tokenApplierBuilder;
    }

    private static class BuildTransformationWithClass<T extends TokenTransformation> implements Callable<Void> {

        private final Class<T> type;
        private final TokenApplierBuilder<T> tokenApplierBuilder;

        private BuildTransformationWithClass(Class<T> type, TokenApplierBuilder<T> tokenApplierBuilder) {

            this.type = type;
            this.tokenApplierBuilder = tokenApplierBuilder;
        }

        @Override
        public Void call() throws Exception {

            buildTransformation(type, tokenApplierBuilder);

            return null;
        }
    }

    private static class BuildTransformationWithConstructor<T extends TokenTransformation> implements Callable<Void> {

        private final Class<T> type;
        private final Constructor<T> constructor;
        private final TokenApplierBuilder<T> tokenApplierBuilder;

        private BuildTransformationWithConstructor(Class<T> type, Constructor<T> constructor, TokenApplierBuilder<T> tokenApplierBuilder) {

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

    private static class NameTokenApplierBuilderWithType<T extends TokenTransformation> implements Callable<Void> {

        private final Class<T> type;

        private NameTokenApplierBuilderWithType(Class<T> type) {

            this.type = type;
        }

        @Override
        public Void call() throws Exception {

            new NameTokenApplierBuilder<T>().build(type);

            return null;
        }
    }

    private static class NameTokenApplierBuilderWithField<T extends TokenTransformation> implements Callable<Void> {

        private final Field field;

        private NameTokenApplierBuilderWithField(Field field) {

            this.field = field;
        }

        @Override
        public Void call() throws Exception {

            new NameTokenApplierBuilder<T>().build(field);

            return null;
        }
    }

    private static class DefaultTokenTransformation implements TokenTransformation {

        @Override
        public String apply(RuleContext context, Token token, String string) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getName() {
            throw new UnsupportedOperationException();
        }
    }

    private static class InvocationTargetExceptionClass extends DefaultTokenTransformation {

        @SuppressWarnings("UnusedParameters")
        public InvocationTargetExceptionClass(TokenApplier applier) {

            throw new RuntimeException("this should produce an InvocationTargetException.");
        }
    }

    private static abstract class InstantiationExceptionClass extends DefaultTokenTransformation {

        @SuppressWarnings("UnusedParameters")
        public InstantiationExceptionClass(TokenApplier applier) {
        }
    }

    private static abstract class IllegalAccessExceptionClass extends DefaultTokenTransformation {

        @SuppressWarnings("UnusedDeclaration")
        private static final String NAME = "illegal_access";

        @SuppressWarnings("UnusedDeclaration")
        private IllegalAccessExceptionClass(TokenApplier applier) {
        }
    }
}
