package tokenizer;

import java.util.Arrays;
import java.util.stream.Stream;

public class SpaceTokenizer implements Tokenizer {

    @Override
    public Stream<Token> apply(Stream<String> string) {
        return string.map(x -> x.split(" "))
                .flatMap(Arrays::stream)
                .map(Token::of);
    }
}
