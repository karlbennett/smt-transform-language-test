package shiver.me.timbers.transform.language.test;

import org.reflections.Reflections;
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

public final class TransformationsUtils {

    private TransformationsUtils() {
    }

    public static List<TokenTransformation> buildWrappingTransformationsFromPackageName(String packageName) {

        final List<Class<TokenTransformation>> tokenTransformationClasses = listTransformationsInPackage(packageName);

        return buildWrappingTransformations(tokenTransformationClasses);
    }

    @SuppressWarnings("unchecked")
    public static List<Class<TokenTransformation>> listTransformationsInPackage(String packageName) {

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

    public static List<TokenTransformation> buildWrappingTransformations(List<Class<TokenTransformation>> classes) {

        return buildTransformations(classes, new TokenApplierBuilder() {

            @Override
            public TokenApplier build(Class<TokenTransformation> type) {

                try {
                    Field field = type.getField("NAME");

                    String name = field.get(null).toString();

                    return new WrappingTokenApplier(name);

                } catch (NoSuchFieldException e) {

                    throw new RuntimeException(e);

                } catch (IllegalAccessException e) {

                    throw new RuntimeException(e);
                }
            }
        });
    }

    public static List<TokenTransformation> buildTransformations(List<Class<TokenTransformation>> classes,
                                                                 TokenApplierBuilder tokenApplierBuilder) {

        List<TokenTransformation> transformations = new ArrayList<TokenTransformation>(classes.size());

        for (Class<TokenTransformation> type : classes) {

            transformations.add(buildTransformation(type, tokenApplierBuilder));
        }

        return transformations;
    }

    private static TokenTransformation buildTransformation(Class<TokenTransformation> type,
                                                           TokenApplierBuilder tokenApplierBuilder) {

        try {

            Constructor<TokenTransformation> constructor = type.getConstructor(TokenApplier.class);

            return constructor.newInstance(tokenApplierBuilder.build(type));

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
}
