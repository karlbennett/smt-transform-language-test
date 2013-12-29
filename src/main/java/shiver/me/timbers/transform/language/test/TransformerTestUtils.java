package shiver.me.timbers.transform.language.test;

import shiver.me.timbers.transform.IndividualTransformations;
import shiver.me.timbers.transform.Transformations;
import shiver.me.timbers.transform.Transformer;
import shiver.me.timbers.transform.antlr4.TokenTransformation;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static shiver.me.timbers.transform.antlr4.NullTokenTransformation.NULL_TOKEN_TRANSFORMATION;

public class TransformerTestUtils {

    public static void transformTest(String transformedSource,
                                     Transformer<TokenTransformation> transformer,
                                     InputStream stream,
                                     Transformations<TokenTransformation> transformations) {

        assertEquals("the source should be Transformed correctly.", transformedSource,
                transformer.transform(stream, transformations));
    }

    public static void transformKeywordsOnlyTest(String transformedKeyWordSource,
                                                 Transformer<TokenTransformation> transformer,
                                                 InputStream stream,
                                                 Transformations<TokenTransformation> keyWordTransformations) {

        assertEquals("the source should be Transformed correctly.", transformedKeyWordSource,
                transformer.transform(stream, keyWordTransformations));
    }

    public static void transformCommentsOnlyTest(String transformedCommentSource,
                                                 Transformer<TokenTransformation> transformer,
                                                 InputStream stream,
                                                 Transformations<TokenTransformation> commentTransformations) {

        assertEquals("the source should be Transformed correctly.", transformedCommentSource,
                transformer.transform(stream, commentTransformations));
    }

    public static void transformWithInvalidSourceTest(String transformedInvalidSource,
                                                      Transformer<TokenTransformation> transformer,
                                                      InputStream stream,
                                                      Transformations<TokenTransformation> allTransformations) {

        assertEquals("the source should be Transformed correctly.", transformedInvalidSource,
                transformer.transform(stream, allTransformations));
    }

    public static void transformWithTypesOnlyTest(String transformedTypesSource,
                                                  Transformer<TokenTransformation> transformer,
                                                  InputStream stream,
                                                  Transformations<TokenTransformation> typesTransformations) {

        assertEquals("the source should be Transformed correctly.", transformedTypesSource,
                transformer.transform(stream, typesTransformations));
    }

    public static void transformWithRulesOnlyTest(String transformedRulesSource,
                                                  Transformer<TokenTransformation> transformer,
                                                  InputStream stream,
                                                  Transformations<TokenTransformation> rulesTransformations) {

        assertEquals("the source should be Transformed correctly.", transformedRulesSource,
                transformer.transform(stream, rulesTransformations));
    }

    public static void transformWithNoTransformationsTest(String originalSource,
                                                          Transformer<TokenTransformation> transformer,
                                                          InputStream stream) {

        assertEquals("the source should be Transformed correctly.", originalSource,
                transformer.transform(stream,
                        new IndividualTransformations<TokenTransformation>(NULL_TOKEN_TRANSFORMATION))
        );
    }

    public static void transformWithIrrelevantTransformationsTest(String originalSource,
                                                                  Transformer<TokenTransformation> transformer,
                                                                  InputStream stream,
                                                                  Transformations<TokenTransformation> unusedTransformations) {

        assertEquals("the source should be Transformed correctly.", originalSource,
                transformer.transform(stream, unusedTransformations));
    }

    public static void transformWithClosedStreamTest(Transformer<TokenTransformation> transformer,
                                                     InputStream stream,
                                                     Transformations<TokenTransformation> allTransformations)
            throws IOException {

        stream.close();

        transformer.transform(stream, allTransformations);
    }

    public static void transformWithNullTransformationsTest(Transformer<TokenTransformation> transformer,
                                                            InputStream stream) {

        transformer.transform(stream, null);
    }

    public static void transformWithNullInputStreamTest(Transformer<TokenTransformation> transformer,
                                                        Transformations<TokenTransformation> allTransformations) {

        transformer.transform(null, allTransformations);
    }
}
