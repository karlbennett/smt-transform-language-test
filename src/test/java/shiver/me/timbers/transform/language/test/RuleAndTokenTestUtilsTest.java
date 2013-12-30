package shiver.me.timbers.transform.language.test;

import org.junit.Test;
import shiver.me.timbers.transform.Transformation;
import shiver.me.timbers.transform.antlr4.CompositeTokenTransformation;
import shiver.me.timbers.transform.antlr4.TokenApplier;

import java.lang.reflect.Field;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.transform.language.test.PackageConstants.INVALID_PACKAGE_NAME;
import static shiver.me.timbers.transform.language.test.PackageConstants.VALID_PACKAGE_NAME;
import static shiver.me.timbers.transform.language.test.RuleAndTokenTestUtils.buildMockApplier;
import static shiver.me.timbers.transform.language.test.RuleAndTokenTestUtils.getTokenApplierField;
import static shiver.me.timbers.transform.language.test.RuleAndTokenTestUtils.staticName;
import static shiver.me.timbers.transform.language.test.RuleAndTokenTestUtils.testEachTransformation;
import static shiver.me.timbers.transform.language.test.RuleAndTokenTestUtils.testEachTransformationInPackage;

public class RuleAndTokenTestUtilsTest {

    @Test
    public void testCreate() {

        new PrivateCreate<RuleAndTokenTestUtils>(RuleAndTokenTestUtils.class);
    }

    @Test
    public void testGetTokenApplierFieldWithValidTransformationClass() throws IllegalAccessException {

        final TokenApplier applier = mock(TokenApplier.class);

        final Field field = getTokenApplierField();

        assertEquals("the applier reflectively extracted applier should be correct.", applier,
                field.get(new TestTokenTransformation(applier)));
    }

    @Test(expected = RuntimeException.class)
    public void testGetTokenApplierFieldWithInvalidTransformationClass() throws IllegalAccessException {

        getTokenApplierField(TestTokenTransformation.class);
    }

    @Test(expected = NullPointerException.class)
    public void testGetTokenApplierFieldWithNullClass() throws IllegalAccessException {

        getTokenApplierField(null);
    }

    @Test
    public void testTestEachTransformationInPackageWithValidPackage() {

        testEachTransformationInPackage(VALID_PACKAGE_NAME);
    }

    @Test(expected = AssertionError.class)
    public void testTestEachTransformationInPackageWithInvalidPackage() {

        testEachTransformationInPackage(INVALID_PACKAGE_NAME);
    }

    @Test
    public void testTestEachTransformationInPackageWithNullPackageName() {

        testEachTransformationInPackage(null);
    }

    @Test
    public void testTestEachTransformationWithTransformation() {

        testEachTransformation(
                Arrays.<CompositeTokenTransformation>asList(new TestTokenTransformation(buildMockApplier())));
    }

    @Test(expected = RuntimeException.class)
    public void testTestEachTransformationWithTransformationWithInaccessibleApplierField() throws NoSuchFieldException {

        final Field field = CompositeTokenTransformation.class.getDeclaredField("applier");

        testEachTransformation(field,
                Arrays.<CompositeTokenTransformation>asList(new TestTokenTransformation(buildMockApplier())));
    }

    @Test
    public void testStaticNameWithValidTransformation() {

        assertEquals("the extracted name should be correct.", TestTokenTransformation.NAME,
                staticName(new TestTokenTransformation(mock(TokenApplier.class))));
    }

    @Test(expected = RuntimeException.class)
    public void testStaticNameWithInvalidTransformation() {

        final Transformation transformation = mock(Transformation.class);

        staticName(transformation);
    }

    @Test(expected = RuntimeException.class)
    public void testStaticNameWithTransformationWithInaccessibleApplierField() throws NoSuchFieldException {

        staticName(PrivateNameTransformation.class.getDeclaredField("NAME"));
    }

    @Test(expected = NullPointerException.class)
    public void testStaticNameWithNullTransformation() {

        staticName((Transformation) null);
    }

    @Test(expected = NullPointerException.class)
    public void testStaticNameWithNullField() {

        staticName((Field) null);
    }

    private static class PrivateNameTransformation implements Transformation {

        @SuppressWarnings("UnusedDeclaration")
        private static final String NAME = "private name";

        @Override
        public String getName() {

            return null;
        }
    }
}
