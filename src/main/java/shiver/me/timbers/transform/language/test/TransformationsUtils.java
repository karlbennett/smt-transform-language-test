package shiver.me.timbers.transform.language.test;

import org.reflections.Reflections;
import shiver.me.timbers.transform.antlr4.CompositeTokenTransformation;
import shiver.me.timbers.transform.antlr4.TokenApplier;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * This class contains methods for easily building multiple instances of
 */
public final class TransformationsUtils {

    private TransformationsUtils() {
    }

    public static List<CompositeTokenTransformation> buildWrappingTransformationsFromPackageName(String packageName) {

        final List<Class<CompositeTokenTransformation>> tokenTransformationClasses = listTransformationsInPackage(packageName);

        return buildWrappingTransformations(tokenTransformationClasses);
    }

    @SuppressWarnings("unchecked")
    public static List<Class<CompositeTokenTransformation>> listTransformationsInPackage(String packageName) {

        final Reflections reflections = new Reflections(packageName);

        final Set<Class<? extends CompositeTokenTransformation>> allTypeTransformationClasses =
                reflections.getSubTypesOf(CompositeTokenTransformation.class);

        final List<Class<CompositeTokenTransformation>> typeTransformationsClasses =
                new ArrayList<Class<CompositeTokenTransformation>>(allTypeTransformationClasses.size());

        for (Class type : allTypeTransformationClasses) {

            typeTransformationsClasses.add((Class<CompositeTokenTransformation>) type);
        }

        return Collections.unmodifiableList(typeTransformationsClasses);
    }

    public static List<CompositeTokenTransformation> buildWrappingTransformations(
            List<Class<CompositeTokenTransformation>> classes) {

        return buildTransformations(classes, new TokenApplierBuilder<CompositeTokenTransformation>() {

            @Override
            public TokenApplier build(Class<CompositeTokenTransformation> type) {

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

    public static List<CompositeTokenTransformation> buildTransformations(
            List<Class<CompositeTokenTransformation>> classes,
            TokenApplierBuilder<CompositeTokenTransformation> tokenApplierBuilder) {

        List<CompositeTokenTransformation> transformations = new ArrayList<CompositeTokenTransformation>(classes.size());

        for (Class<CompositeTokenTransformation> type : classes) {

            transformations.add(buildTransformation(type, tokenApplierBuilder));
        }

        return transformations;
    }

    static CompositeTokenTransformation buildTransformation(
            Class<CompositeTokenTransformation> type,
            TokenApplierBuilder<CompositeTokenTransformation> tokenApplierBuilder) {

        try {

            Constructor<CompositeTokenTransformation> constructor = type.getConstructor(TokenApplier.class);

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
