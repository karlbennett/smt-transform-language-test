package shiver.me.timbers.transform.language.test;

import org.reflections.Reflections;
import shiver.me.timbers.transform.antlr4.CompositeTokenTransformation;
import shiver.me.timbers.transform.antlr4.TokenApplier;
import shiver.me.timbers.transform.antlr4.TokenTransformation;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * This class contains methods for easily building multiple instances of
 */
public final class TransformationsUtils {

    private TransformationsUtils() {
    }

    public static <T extends TokenTransformation> List<T> buildWrappingTransformationsFromPackageName(
            String packageName) {

        return buildTransformationsFromPackageName(packageName, new NameTokenApplierBuilder<T>());
    }

    public static <T extends TokenTransformation> List<T> buildTransformationsFromPackageName(
            String packageName, TokenApplierBuilder<T> tokenApplierBuilder) {

        final List<Class<T>> tokenTransformationClasses = listTransformationsInPackage(packageName);

        return buildTransformations(tokenTransformationClasses, tokenApplierBuilder);
    }

    @SuppressWarnings("unchecked")
    public static <T extends TokenTransformation> List<Class<T>> listTransformationsInPackage(
            String packageName) {

        final Reflections reflections = new Reflections(packageName);

        final Set<Class<? extends CompositeTokenTransformation>> allTypeTransformationClasses =
                reflections.getSubTypesOf(CompositeTokenTransformation.class);

        final List<Class<T>> typeTransformationsClasses =
                new ArrayList<Class<T>>(allTypeTransformationClasses.size());

        for (Class type : allTypeTransformationClasses) {

            typeTransformationsClasses.add((Class<T>) type);
        }

        return Collections.unmodifiableList(typeTransformationsClasses);
    }

    public static <T extends TokenTransformation> List<T> buildTransformations(Collection<Class<T>> classes,
                                                                               TokenApplierBuilder<T> tokenApplierBuilder) {

        List<T> transformations = new ArrayList<T>(classes.size());

        for (Class<T> type : classes) {

            transformations.add(buildTransformation(type, tokenApplierBuilder));
        }

        return transformations;
    }

    @SuppressWarnings("unchecked")
    static <T extends TokenTransformation> T buildTransformation(Class<T> type,
                                                                 TokenApplierBuilder<T> tokenApplierBuilder) {

        try {

            Constructor<T> constructor = type.getConstructor(TokenApplier.class);

            return buildTransformation(type, constructor, tokenApplierBuilder);

        } catch (NoSuchMethodException e) {

            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    static <T extends TokenTransformation> T buildTransformation(Class<T> type, Constructor<T> constructor,
                                                                 TokenApplierBuilder<T> tokenApplierBuilder) {

        try {

            return constructor.newInstance(tokenApplierBuilder.build(type));

        } catch (InvocationTargetException e) {

            throw new RuntimeException(e);

        } catch (InstantiationException e) {

            throw new RuntimeException(e);

        } catch (IllegalAccessException e) {

            throw new RuntimeException(e);

        }
    }
}
