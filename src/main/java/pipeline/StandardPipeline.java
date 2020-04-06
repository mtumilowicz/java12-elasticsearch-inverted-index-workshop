package pipeline;

import filter.character.BracketCharacterFilter;
import filter.character.CharacterFilter;
import filter.token.LowercaseTokenFilter;
import filter.token.StopWordTokenFilter;
import filter.token.TokenFilter;
import tokenizer.SpaceTokenizer;
import tokenizer.Tokenizer;

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
