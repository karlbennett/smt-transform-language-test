package shiver.me.timbers.transform.language.test;

import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;
import shiver.me.timbers.transform.antlr4.TokenApplier;

/**
 * This applier wraps a string in a given wrapper string.
 */
public class WrappingTokenApplier implements TokenApplier {

    private final String wrapping;

    public WrappingTokenApplier(String wrapping) {

        this.wrapping = wrapping;
    }

    @Override
    public String apply(RuleContext context, Token token, String string) {

        return wrapping() + string + wrapping();
    }

    private String wrapping() {

        return '[' + wrapping + ']';
    }
}