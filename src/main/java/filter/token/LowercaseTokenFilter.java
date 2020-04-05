package filter.token;

import java.util.function.Function;
import java.util.stream.Stream;

public class LowercaseTokenFilter implements Function<Stream<String>, Stream<String>> {
    @Override
    public Stream<String> apply(Stream<String> tokens) {
        return tokens.map(String::toLowerCase);
    }
}
