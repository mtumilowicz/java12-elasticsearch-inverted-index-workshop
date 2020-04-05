package filter.token;

import java.util.stream.Stream;

public class LowercaseTokenFilter implements TokenFilter {
    @Override
    public Stream<String> filter(String token) {
        return Stream.of(token.toLowerCase());
    }
}
