package pipeline;

import filter.character.CharacterFilter;
import filter.token.TokenFilter;
import lombok.RequiredArgsConstructor;
import lombok.With;
import tokenizer.Tokenizer;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

@RequiredArgsConstructor
@With
public class StandardPipeline implements AnalyzingPipeline {

    private final CharacterFilter characterFilter;
    private final Tokenizer tokenizer;
    private final TokenFilter tokenFilter;

    @Override
    public Stream<String> analyze(String string) {
        return List.of(characterFilter, tokenizer, tokenFilter)
                .stream()
                .reduce(Function.<Stream<String>>identity(), Function::andThen, Function::andThen)
                .apply(Stream.of(string));
    }
}
