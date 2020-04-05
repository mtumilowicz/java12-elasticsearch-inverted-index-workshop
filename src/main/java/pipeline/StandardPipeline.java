package pipeline;

import filter.character.ArabicNumbersCharacterFilter;
import filter.character.CharacterFilter;
import filter.token.LowercaseTokenFilter;
import filter.token.StopWordTokenFilter;
import filter.token.TokenFilter;
import lombok.RequiredArgsConstructor;
import lombok.With;
import tokenizer.SpaceTokenizer;
import tokenizer.Tokenizer;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class StandardPipeline implements AnalyzingPipeline {

    private static final CharacterFilter characterFilter = new ArabicNumbersCharacterFilter();
    private static final Tokenizer tokenizer = new SpaceTokenizer();
    private static final TokenFilter tokenFilter = new LowercaseTokenFilter().andThen(new StopWordTokenFilter());

    @Override
    public Stream<String> analyze(String string) {
        return List.of(characterFilter, tokenizer, tokenFilter)
                .stream()
                .reduce(Function.<Stream<String>>identity(), Function::andThen, Function::andThen)
                .apply(Stream.of(string));
    }
}
