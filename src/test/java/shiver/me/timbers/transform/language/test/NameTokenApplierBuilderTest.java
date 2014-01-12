package shiver.me.timbers.transform.language.test;

import org.junit.Test;
import shiver.me.timbers.transform.antlr4.TokenTransformation;

import java.lang.reflect.Field;

import static shiver.me.timbers.transform.language.test.ExceptionUtils.assertException;
import static shiver.me.timbers.transform.language.test.TestUtils.IllegalAccessExceptionClass;
import static shiver.me.timbers.transform.language.test.TestUtils.NameTokenApplierBuilderWithField;
import static shiver.me.timbers.transform.language.test.TestUtils.NameTokenApplierBuilderWithType;
import static shiver.me.timbers.transform.language.test.TestUtils.assertCorrectApplier;

public class NameTokenApplierBuilderTest {

    @Test
    public void testNameTokenApplierBuilderWithValidTransformationType() {

        final NameTokenApplierBuilder<TestTokenTransformation> tokenApplierBuilder = new NameTokenApplierBuilder<TestTokenTransformation>();

        assertCorrectApplier(new WrappingTokenApplier(TestTokenTransformation.NAME),
                tokenApplierBuilder.build(TestTokenTransformation.class));
    }

    @Test
    public void testNameTokenApplierBuilderWithInvalidTransformationType() {

        assertException(NoSuchFieldException.class,
                new NameTokenApplierBuilderWithType<TokenTransformation>(TokenTransformation.class));
    }

    @Test(expected = NullPointerException.class)
    public void testNameTokenApplierBuilderWithNullTransformationType() {

        new NameTokenApplierBuilder<TokenTransformation>().build((Class<TokenTransformation>) null);
    }

    @Test
    public void testNameTokenApplierBuilderWithPrivateField() throws NoSuchFieldException {

        assertException(IllegalAccessException.class,
                new NameTokenApplierBuilderWithField(IllegalAccessExceptionClass.class.getDeclaredField("NAME")));
    }

    @Test(expected = NullPointerException.class)
    public void testNameTokenApplierBuilderWithNullField() throws NoSuchFieldException {

        new NameTokenApplierBuilder<TokenTransformation>().build((Field) null);
    }
}
