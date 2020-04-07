package workshop.analysis.tokenizer;

import answers.analysis.tokenizer.Token;
import answers.analysis.tokenizer.Tokenizer;

import java.util.stream.Stream;

public class SpaceTokenizerWorkshop implements Tokenizer {

    private static final String SPACE = " ";

    @Override
    public Stream<Token> apply(String string) {
        // split into tokens by SPACE, hint: string.split
        return null;
    }
}
