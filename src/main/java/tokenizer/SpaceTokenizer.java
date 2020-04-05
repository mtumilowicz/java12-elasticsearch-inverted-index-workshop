package tokenizer;

import java.util.stream.Stream;

public class SpaceTokenizer implements Tokenizer {

    @Override
    public Stream<String> tokenize(String string) {
        return Stream.of(string.split(" "));
    }
}
