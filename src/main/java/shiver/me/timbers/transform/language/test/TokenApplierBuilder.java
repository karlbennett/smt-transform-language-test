package shiver.me.timbers.transform.language.test;

import shiver.me.timbers.transform.antlr4.TokenApplier;
import shiver.me.timbers.transform.antlr4.TokenTransformation;

/**
 * This interface should be implemented with logic that will build a new {@code TokenApplier}.
 */
public interface TokenApplierBuilder {

    /**
     * @return a new {@code TokenApplier} for the supplied {@code TokenTransformation} type.
     */
    public TokenApplier build(Class<TokenTransformation> type);
}
