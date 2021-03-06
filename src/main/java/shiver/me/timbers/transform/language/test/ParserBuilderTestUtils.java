package shiver.me.timbers.transform.language.test;

import org.antlr.v4.runtime.Parser;
import shiver.me.timbers.transform.Transformations;
import shiver.me.timbers.transform.antlr4.IterableTokenTransformations;
import shiver.me.timbers.transform.antlr4.ParserBuilder;
import shiver.me.timbers.transform.antlr4.TokenTransformation;

import static org.junit.Assert.assertNotNull;

/**
 * This class contains methods that can be used to test implementations of the {@link ParserBuilder}.
 */
public final class ParserBuilderTestUtils {

    private ParserBuilderTestUtils() {
    }

    private static final String SOURCE = "";

    private static final Transformations<TokenTransformation> TRANSFORMATIONS = new IterableTokenTransformations();

    public static <P extends Parser> void buildParserTest(ParserBuilder<P> parserBuilder) {

        assertNotNull("a built parser should be produced.", parserBuilder.buildParser(SOURCE, TRANSFORMATIONS));
    }

    public static <P extends Parser> void buildParserWithNullSourceStringTest(ParserBuilder<P> parserBuilder) {

        parserBuilder.buildParser(null, TRANSFORMATIONS);
    }

    public static <P extends Parser> void buildParserWithNullTransformationsTest(ParserBuilder<P> parserBuilder) {

        parserBuilder.buildParser(SOURCE, null);
    }

    public static <P extends Parser> void parseTest(ParserBuilder<P> parserBuilder) {

        final P parser = parserBuilder.buildParser(SOURCE, TRANSFORMATIONS);

        assertNotNull("parse should produce a parse tree.", parserBuilder.parse(parser));
    }

    public static <P extends Parser> void parseWithNullParserTest(ParserBuilder<P> parserBuilder) {

        parserBuilder.parse(null);
    }
}
