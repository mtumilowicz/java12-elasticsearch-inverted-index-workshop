package filter.token;

import java.util.stream.Stream;

public class LowercaseTokenFilter implements TokenFilter {
    @Override
    public Stream<String> apply(Stream<String> tokens) {
        return tokens.map(String::toLowerCase);
    }
}
