package filter.token;

import java.util.Set;
import java.util.stream.Stream;

public class StopWordTokenFilter implements TokenFilter {

    private static Set<String> stopWords = Set.of("a", "the", "however");

    @Override
    public Stream<String> filter(String token) {
        return stopWords.contains(token)
                ? Stream.empty()
                : Stream.of(token);
    }
}
