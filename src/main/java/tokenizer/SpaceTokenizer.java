package tokenizer;

import java.util.function.Function;
import java.util.stream.Stream;

public class SpaceTokenizer implements Function<Stream<String>, Stream<String>> {

    @Override
    public Stream<String> apply(Stream<String> string) {
        return string.flatMap(x -> Stream.of(x.split(" ")));
    }
}
