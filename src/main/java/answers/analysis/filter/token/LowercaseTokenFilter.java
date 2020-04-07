package answers.analysis.filter.token;

import answers.analysis.tokenizer.Token;

import java.util.stream.Stream;

public class LowercaseTokenFilter implements TokenFilter {
    @Override
    public Stream<Token> apply(Stream<Token> tokens) {
        return tokens.map(Token::toLowerCase);
    }
}
