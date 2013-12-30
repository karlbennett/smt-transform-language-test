package shiver.me.timbers.transform.language.test;

import shiver.me.timbers.transform.antlr4.CompositeTokenTransformation;
import shiver.me.timbers.transform.antlr4.TokenApplier;

/**
 * This {@code TokenTransformation} exeists simple to test the {@link RuleAndTokenTestUtils} class.
 */
public class TestTokenTransformation extends CompositeTokenTransformation {

    public static final String NAME = "Test";

    public TestTokenTransformation(TokenApplier applier) {

        super(NAME, applier);
    }
}
