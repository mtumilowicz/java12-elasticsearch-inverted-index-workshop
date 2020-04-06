package pipeline;

import filter.character.CharacterFilter;
import filter.token.TokenFilter;
import tokenizer.Token;
import tokenizer.Tokenizer;

import java.util.stream.Stream;

public interface AnalyzingPipeline {

    CharacterFilter characterFilter();

    Tokenizer tokenizer();

    TokenFilter tokenFilter();

    default Stream<Token> analyze(Stream<String> string) {
        return tokenFilter().compose(tokenizer().compose(characterFilter())).apply(string);
    }
}
