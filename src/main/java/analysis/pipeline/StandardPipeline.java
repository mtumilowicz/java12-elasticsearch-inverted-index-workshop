package analysis.pipeline;

import analysis.filter.character.BracketCharacterFilter;
import analysis.filter.character.CharacterFilter;
import analysis.filter.token.LowercaseTokenFilter;
import analysis.filter.token.StopWordTokenFilter;
import analysis.filter.token.TokenFilter;
import analysis.tokenizer.SpaceTokenizer;
import analysis.tokenizer.Tokenizer;

public class StandardPipeline implements AnalyzingPipeline {

    private final CharacterFilter characterFilter = new BracketCharacterFilter();

    private final Tokenizer tokenizer = new SpaceTokenizer();

    private final TokenFilter tokenFilter = new LowercaseTokenFilter().andThen(new StopWordTokenFilter());

    @Override
    public CharacterFilter characterFilter() {
        return characterFilter;
    }

    @Override
    public Tokenizer tokenizer() {
        return tokenizer;
    }

    @Override
    public TokenFilter tokenFilter() {
        return tokenFilter;
    }
}
