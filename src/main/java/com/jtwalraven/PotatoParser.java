package com.jtwalraven;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;

@BuildParseTree
public class PotatoParser extends BaseParser<Query> {

    public Rule POTATOR() {
        return Sequence(IgnoreCase("POTATOR"), WhiteSpace());
    }

    public Rule Char() {
        return AnyOf("0123456789" +
                "abcdefghijklmnopqrstuvwxyz" +
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                "-_");
    }

    public Rule WhiteSpace() {
        return OneOrMore(AnyOf(" \t\f"));
    }

    public Rule Term() {
        return Sequence(OneOrMore(Char()), push(new TermQuery(new Term(match()))));
    }

    public Rule PotatorExpression() {
        return ZeroOrMore(
                Term(), POTATOR(), Term(), push(new BooleanQuery.Builder()
                        .add(pop(), BooleanClause.Occur.SHOULD)
                        .add(pop(), BooleanClause.Occur.SHOULD)
                        .build())
        );
    }

    public Rule Query() {
        return Sequence(PotatorExpression(), EOI);
    }
}
