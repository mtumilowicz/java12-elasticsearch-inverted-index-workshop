package analysis.pipeline;

import analysis.filter.character.CharacterFilter;
import analysis.filter.token.TokenFilter;
import analysis.tokenizer.Token;
import analysis.tokenizer.Tokenizer;

import java.util.stream.Stream;

public interface AnalyzingPipeline {

    CharacterFilter characterFilter();

    Tokenizer tokenizer();

    TokenFilter tokenFilter();

    default Stream<Token> analyze(String string) {
        return tokenFilter().compose(tokenizer().compose(characterFilter())).apply(string);
    }
}
