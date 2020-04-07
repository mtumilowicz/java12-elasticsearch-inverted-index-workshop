package workshop.analysis.filter.token;

import answers.analysis.filter.token.TokenFilter;
import answers.analysis.tokenizer.Token;

import java.util.stream.Stream;

public class LowercaseTokenFilterWorkshop implements TokenFilter {
    @Override
    public Stream<Token> apply(Stream<Token> tokens) {
        // lowercase each token, hint: Token::toLowerCas
        return null;
    }
}
