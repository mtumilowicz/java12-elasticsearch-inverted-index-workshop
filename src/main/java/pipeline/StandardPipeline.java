package pipeline;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class StandardPipeline implements AnalyzingPipeline {
    List<Function<Stream<String>, Stream<String>>> characterFilters;
    List<Function<Stream<String>, Stream<String>>> tokenizers;
    List<Function<Stream<String>, Stream<String>>> tokenFilters;


    @Override
    public Stream<String> analyze(String string) {
        return List.of(characterFilters, tokenizers, tokenFilters)
                .stream()
                .flatMap(Collection::stream)
                .reduce(UnaryOperator.identity(), Function::andThen)
                .apply(Stream.of(string));
    }
}
