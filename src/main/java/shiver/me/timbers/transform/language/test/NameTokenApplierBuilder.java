package shiver.me.timbers.transform.language.test;

import shiver.me.timbers.transform.antlr4.TokenApplier;
import shiver.me.timbers.transform.antlr4.TokenTransformation;

import java.lang.reflect.Field;

/**
 * This builder will return a {@link WrappingTokenApplier} that uses the token types static {@code NAME} fields value
 * for it's wrapping. It assumes that the supplied type has a public static {@code NAME} field.
 */
public class NameTokenApplierBuilder<T extends TokenTransformation> implements TokenApplierBuilder<T> {

    @Override
    public TokenApplier build(Class<T> type) {

        try {
            Field field = type.getField("NAME");

            return build(field);

        } catch (NoSuchFieldException e) {

            throw new RuntimeException(e);
        }
    }

    TokenApplier build(Field field) {

        try {

            String name = field.get(null).toString();

            return new WrappingTokenApplier(name);

        } catch (IllegalAccessException e) {

            throw new RuntimeException(e);
        }
    }
}
