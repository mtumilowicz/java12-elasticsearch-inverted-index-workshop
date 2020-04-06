package filter.token;

import tokenizer.Token;

import java.util.stream.Stream;

public class StopWordTokenFilter implements TokenFilter {

    private static Stream<String> stopWords = Stream.of("a", "the", "however");

    @Override
    public Stream<Token> apply(Stream<Token> tokens) {
        return tokens.filter(token -> stopWords.anyMatch(token::isEqual));
    }
}
