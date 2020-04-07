package workshop.analysis.filter.token;

import answers.analysis.filter.token.TokenFilter;
import answers.analysis.tokenizer.Token;

import java.util.Set;
import java.util.stream.Stream;

public class StopWordTokenFilter implements TokenFilter {

    private static Set<String> stopWords = Set.of(
            "a",
            "and",
            "as",
            "by",
            "in",
            "my",
            "the"
    );

    @Override
    public Stream<Token> apply(Stream<Token> tokens) {
        // remove all tokens that are in stopWords, hint: token.notIn
        return null;
    }
}
