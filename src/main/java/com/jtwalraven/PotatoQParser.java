package com.jtwalraven;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.join.QueryBitSetProducer;
import org.apache.lucene.search.join.ScoreMode;
import org.apache.lucene.search.join.ToParentBlockJoinQuery;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.QParser;
import org.apache.solr.search.SyntaxError;
import org.parboiled.Parboiled;
import org.parboiled.errors.ErrorUtils;
import org.parboiled.parserunners.RecoveringParseRunner;
import org.parboiled.support.ParsingResult;

public class PotatoQParser extends QParser {

    /**
     * Constructor for the QParser
     *
     * @param qstr        The part of the query string specific to this parser
     * @param localParams The set of parameters that are specific to this QParser.  See http://wiki.apache.org/solr/LocalParams
     * @param params      The rest of the {@link SolrParams}
     * @param req         The original {@link SolrQueryRequest}.
     */
    public PotatoQParser(String qstr, SolrParams localParams, SolrParams params, SolrQueryRequest req) {
        super(qstr, localParams, params, req);
    }

    @Override
    public Query parse() throws SyntaxError {
        PotatoParser potatoParser = Parboiled.createParser(PotatoParser.class);
        ParsingResult<?> result = new RecoveringParseRunner<Query>(potatoParser.Query()).run(this.qstr);
        if (!result.parseErrors.isEmpty()) {
            throw new SyntaxError(ErrorUtils.printParseError(result.parseErrors.get(0)));
        }

        Query query = (Query) result.parseTreeRoot.getValue();
        Query parentQuery = new TermQuery(new Term("type", "potato"));

        return new ToParentBlockJoinQuery(query, new QueryBitSetProducer(parentQuery), ScoreMode.None);
    }
}
