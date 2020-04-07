package analysis.pipeline;

import analysis.filter.character.NoiseCharacterFilter;
import analysis.filter.character.CharacterFilter;
import analysis.filter.token.LowercaseTokenFilter;
import analysis.filter.token.StopWordTokenFilter;
import analysis.filter.token.TokenFilter;
import analysis.tokenizer.SpaceTokenizer;
import analysis.tokenizer.Token;
import analysis.tokenizer.Tokenizer;

import java.util.stream.Stream;

public class StandardPipeline implements AnalyzingPipeline {

    private final CharacterFilter characterFilter = new NoiseCharacterFilter();

    private final Tokenizer tokenizer = new SpaceTokenizer();

    private final TokenFilter tokenFilter = new LowercaseTokenFilter().andThen(new StopWordTokenFilter());

    public Stream<Token> analyze(String string) {
        return characterFilter.andThen(tokenizer).andThen(tokenFilter).apply(string);
    }
}
