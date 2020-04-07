package answers.analysis.tokenizer;

import java.util.stream.Stream;

public class SpaceTokenizer implements Tokenizer {

    private static final String SPACE = " ";

    @Override
    public Stream<Token> apply(String term) {
        return Stream.of(term.split(SPACE))
                .map(Token::of);
    }
}
