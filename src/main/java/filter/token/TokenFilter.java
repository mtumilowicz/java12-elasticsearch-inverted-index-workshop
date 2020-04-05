package filter.token;

import java.util.stream.Stream;

public interface TokenFilter {
    Stream<String> filter(String token);
}
