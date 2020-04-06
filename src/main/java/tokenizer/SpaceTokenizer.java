package tokenizer;

import java.util.stream.Stream;

public class SpaceTokenizer implements Tokenizer {

    private static final String SPACE = " ";

    @Override
    public Stream<Token> apply(String string) {
        return Stream.of(string.split(SPACE))
                .map(Token::of);
    }
}
