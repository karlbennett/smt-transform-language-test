package shiver.me.timbers.transform.language.test;

import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;
import shiver.me.timbers.transform.Transformations;
import shiver.me.timbers.transform.antlr4.ParserBuilder;

import static org.mockito.Matchers.isNull;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static shiver.me.timbers.transform.language.test.ParserBuilderTestUtils.buildParserTest;
import static shiver.me.timbers.transform.language.test.ParserBuilderTestUtils.buildParserWithNullSourceStringTest;
import static shiver.me.timbers.transform.language.test.ParserBuilderTestUtils.buildParserWithNullTransformationsTest;
import static shiver.me.timbers.transform.language.test.ParserBuilderTestUtils.parseTest;
import static shiver.me.timbers.transform.language.test.ParserBuilderTestUtils.parseWithNullParserTest;

public class ParserBuilderTestUtilsTest {

    @Test
    public void testCreate() {

        new PrivateCreate<ParserBuilderTestUtils>(ParserBuilderTestUtils.class);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testBuildParserTestWithValidParser() {

        final Recognizer recognizer = mock(Recognizer.class);

        final ParserBuilder parserBuilder = mock(ParserBuilder.class);
        when(parserBuilder.buildParser(anyString(), any(Transformations.class))).thenReturn(recognizer);

        buildParserTest(parserBuilder);

        verify(parserBuilder, times(1)).buildParser(notNull(String.class), notNull(Transformations.class));

        verifyNoMoreInteractions(parserBuilder);
    }

    @Test(expected = AssertionError.class)
    public void testBuildParserTestWithInvalidParser() {

        final ParserBuilder parserBuilder = mock(ParserBuilder.class);

        buildParserTest(parserBuilder);
    }

    @Test(expected = NullPointerException.class)
    public void testBuildParserTestWithNullParser() {

        buildParserTest(null);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testBuildParserWithNullSourceStringTestWithParser() {

        final ParserBuilder parserBuilder = mock(ParserBuilder.class);

        buildParserWithNullSourceStringTest(parserBuilder);

        verify(parserBuilder, times(1)).buildParser(isNull(String.class), notNull(Transformations.class));

        verifyNoMoreInteractions(parserBuilder);
    }

    @Test(expected = NullPointerException.class)
    public void testBuildParserWithNullSourceStringTestWithNullParser() {

        buildParserWithNullSourceStringTest(null);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testBuildParserWithNullTransformationsTestWithParser() {

        final ParserBuilder parserBuilder = mock(ParserBuilder.class);

        buildParserWithNullTransformationsTest(parserBuilder);

        verify(parserBuilder, times(1)).buildParser(notNull(String.class), isNull(Transformations.class));

        verifyNoMoreInteractions(parserBuilder);
    }

    @Test(expected = NullPointerException.class)
    public void testBuildParserWithNullTransformationsTestWithNullParser() {

        buildParserWithNullTransformationsTest(null);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testParseTestWithValidParser() {

        final Recognizer recognizer = mock(Recognizer.class);
        final ParseTree parseTree = mock(ParseTree.class);

        final ParserBuilder<Recognizer> parserBuilder = mock(ParserBuilder.class);
        when(parserBuilder.buildParser(anyString(), any(Transformations.class))).thenReturn(recognizer);
        when(parserBuilder.parse(recognizer)).thenReturn(parseTree);

        parseTest(parserBuilder);

        verify(parserBuilder, times(1)).buildParser(notNull(String.class), notNull(Transformations.class));
        verify(parserBuilder, times(1)).parse(recognizer);

        verifyNoMoreInteractions(parserBuilder);
    }

    @Test(expected = AssertionError.class)
    public void testParseTestWithInvalidParser() {

        final ParserBuilder parserBuilder = mock(ParserBuilder.class);

        parseTest(parserBuilder);
    }

    @Test(expected = NullPointerException.class)
    public void testParseTestWithNullParser() {

        parseTest(null);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testParseWithNullParserTestWithParser() {

        final ParserBuilder<Recognizer> parserBuilder = mock(ParserBuilder.class);

        parseWithNullParserTest(parserBuilder);

        verify(parserBuilder, times(1)).parse(isNull(Recognizer.class));

        verifyNoMoreInteractions(parserBuilder);
    }

    @Test(expected = NullPointerException.class)
    public void testParseWithNullParserTestWithNullParser() {

        parseWithNullParserTest(null);
    }
}
