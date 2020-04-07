package analysis.filter.token;

import analysis.tokenizer.Token;

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
        return tokens.filter(token -> token.notIn(stopWords));
    }
}
