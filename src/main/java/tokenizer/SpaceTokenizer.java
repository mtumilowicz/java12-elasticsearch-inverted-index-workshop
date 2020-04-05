package tokenizer;

import java.util.stream.Stream;

public class SpaceTokenizer implements Tokenizer {

    @Override
    public Stream<String> apply(Stream<String> string) {
        return string.flatMap(x -> Stream.of(x.split(" ")));
    }
}
