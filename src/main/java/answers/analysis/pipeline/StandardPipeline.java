package answers.analysis.pipeline;

import answers.analysis.filter.character.NoiseCharacterFilter;
import answers.analysis.filter.character.CharacterFilter;
import answers.analysis.filter.token.LowercaseTokenFilter;
import answers.analysis.filter.token.StopWordTokenFilter;
import answers.analysis.filter.token.TokenFilter;
import answers.analysis.tokenizer.SpaceTokenizer;
import answers.analysis.tokenizer.Token;
import answers.analysis.tokenizer.Tokenizer;

import java.util.stream.Stream;

public class StandardPipeline implements AnalyzingPipeline {

    private final CharacterFilter characterFilter = new NoiseCharacterFilter();

    private final Tokenizer tokenizer = new SpaceTokenizer();

    private final TokenFilter tokenFilter = new LowercaseTokenFilter().andThen(new StopWordTokenFilter());

    public Stream<Token> analyze(String string) {
        return characterFilter.andThen(tokenizer).andThen(tokenFilter).apply(string);
    }
}
