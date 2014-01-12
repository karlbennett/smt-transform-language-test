package shiver.me.timbers.transform.language.test;

import org.junit.Test;
import shiver.me.timbers.transform.antlr4.CompositeTokenTransformation;
import shiver.me.timbers.transform.antlr4.TokenApplier;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.transform.language.test.ExceptionUtils.assertException;
import static shiver.me.timbers.transform.language.test.PackageConstants.INVALID_PACKAGE_NAME;
import static shiver.me.timbers.transform.language.test.PackageConstants.VALID_PACKAGE_NAME;
import static shiver.me.timbers.transform.language.test.TestUtils.BuildTransformationWithClass;
import static shiver.me.timbers.transform.language.test.TestUtils.BuildTransformationWithConstructor;
import static shiver.me.timbers.transform.language.test.TestUtils.IllegalAccessExceptionClass;
import static shiver.me.timbers.transform.language.test.TestUtils.InstantiationExceptionClass;
import static shiver.me.timbers.transform.language.test.TestUtils.InvocationTargetExceptionClass;
import static shiver.me.timbers.transform.language.test.TestUtils.assertCorrectApplier;
import static shiver.me.timbers.transform.language.test.TestUtils.assertCorrectTransformation;
import static shiver.me.timbers.transform.language.test.TestUtils.assertCorrectTransformationTypes;
import static shiver.me.timbers.transform.language.test.TestUtils.invalidTransformationTypes;
import static shiver.me.timbers.transform.language.test.TestUtils.mockTokenApplier;
import static shiver.me.timbers.transform.language.test.TestUtils.transformationTypes;
import static shiver.me.timbers.transform.language.test.TransformationsUtils.buildTransformation;
import static shiver.me.timbers.transform.language.test.TransformationsUtils.buildTransformations;
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
    public void testBuildTransformationsWithValidTransformationTypeAndTokenApplierBuilder() {

        final TokenApplier applier = mockTokenApplier();

        final List<TestTokenTransformation> transformations = buildTransformations(transformationTypes(),
                TestUtils.<TestTokenTransformation>mockTokenApplierBuilder(applier));

        assertCorrectApplier(applier, transformations.get(0));
    }

    @Test(expected = RuntimeException.class)
    public void testBuildTransformationsWithInvalidTransformationType() {

        buildTransformations(invalidTransformationTypes(),
                TestUtils.<TestTokenTransformation>mockTokenApplierBuilder(mockTokenApplier()));
    }

    @Test(expected = NullPointerException.class)
    public void testBuildTransformationsWithNullTransformationType() {

        buildTransformations(null,
                TestUtils.<TestTokenTransformation>mockTokenApplierBuilder(mockTokenApplier()));
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
                TestUtils.<TestTokenTransformation>mockTokenApplierBuilder(applier));

        assertCorrectApplier(applier, transformation);
    }

    @Test
    public void testBuildTransformationWithNoTokenApplierConstructor() {

        final TokenApplier applier = mockTokenApplier();

        assertException(NoSuchMethodException.class,
                new BuildTransformationWithClass<CompositeTokenTransformation>(CompositeTokenTransformation.class,
                        TestUtils.<CompositeTokenTransformation>mockTokenApplierBuilder(applier)));
    }

    @Test
    public void testBuildTransformationWithTokenApplierConstructorThatThrowsAnException() {

        final TokenApplier applier = mockTokenApplier();

        assertException(InvocationTargetException.class,
                new BuildTransformationWithClass<InvocationTargetExceptionClass>(InvocationTargetExceptionClass.class,
                        TestUtils.<InvocationTargetExceptionClass>mockTokenApplierBuilder(applier)));
    }

    @Test
    public void testBuildTransformationWithAbstractClass() {

        final TokenApplier applier = mockTokenApplier();

        assertException(InstantiationException.class,
                new BuildTransformationWithClass<InstantiationExceptionClass>(InstantiationExceptionClass.class,
                        TestUtils.<InstantiationExceptionClass>mockTokenApplierBuilder(applier)));
    }

    @Test
    public void testBuildTransformationWithPrivateTokenApplierConstructor() throws NoSuchMethodException {

        final TokenApplier applier = mockTokenApplier();

        assertException(IllegalAccessException.class,
                new BuildTransformationWithConstructor<IllegalAccessExceptionClass>(IllegalAccessExceptionClass.class,
                        IllegalAccessExceptionClass.class.getDeclaredConstructor(TokenApplier.class),
                        TestUtils.<IllegalAccessExceptionClass>mockTokenApplierBuilder(applier)));
    }
}
