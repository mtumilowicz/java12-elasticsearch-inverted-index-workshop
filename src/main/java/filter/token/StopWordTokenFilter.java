package filter.token;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public class StopWordTokenFilter implements Function<Stream<String>, Stream<String>> {

    private static Set<String> stopWords = Set.of("a", "the", "however");

    @Override
    public Stream<String> apply(Stream<String> tokens) {
        return tokens.filter(not(stopWords::contains));
    }
}
