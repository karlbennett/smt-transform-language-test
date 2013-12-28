package shiver.me.timbers.transform.language.test;

import org.antlr.v4.runtime.Recognizer;
import shiver.me.timbers.transform.IndividualTransformations;
import shiver.me.timbers.transform.Transformations;
import shiver.me.timbers.transform.antlr4.ParserBuilder;
import shiver.me.timbers.transform.antlr4.TokenTransformation;

import static org.junit.Assert.assertNotNull;
import static shiver.me.timbers.transform.antlr4.NullTokenTransformation.NULL_TOKEN_TRANSFORMATION;

public final class ParserBuilderTestUtils {

    private ParserBuilderTestUtils() {
    }

    public static final String SOURCE = "";

    public static final Transformations<TokenTransformation> TRANSFORMATIONS =
            new IndividualTransformations<TokenTransformation>(NULL_TOKEN_TRANSFORMATION);

    public static <P extends Recognizer> void buildParserTest(ParserBuilder<P> parserBuilder) {

        assertNotNull("a built parser should be produced.", parserBuilder.buildParser(SOURCE, TRANSFORMATIONS));
    }

    public static <P extends Recognizer> void buildParserWithNullSourceStringTest(ParserBuilder<P> parserBuilder) {

        parserBuilder.buildParser(null, TRANSFORMATIONS);
    }

    public static <P extends Recognizer> void buildParserWithNullTransformationsTest(ParserBuilder<P> parserBuilder) {

        parserBuilder.buildParser(SOURCE, null);
    }

    public static <P extends Recognizer> void parseTest(ParserBuilder<P> parserBuilder) {

        final P parser = parserBuilder.buildParser(SOURCE, TRANSFORMATIONS);

        assertNotNull("parse should produce a parse tree.", parserBuilder.parse(parser));
    }

    public static <P extends Recognizer> void parseWithNullParserTest(ParserBuilder<P> parserBuilder) {

        parserBuilder.parse(null);
    }
}
