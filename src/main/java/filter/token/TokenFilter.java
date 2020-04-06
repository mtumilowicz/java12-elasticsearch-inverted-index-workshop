package filter.token;

import tokenizer.Token;

import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public interface TokenFilter extends UnaryOperator<Stream<Token>> {

    default TokenFilter andThen(TokenFilter after) {
        return t -> after.apply(apply(t));
    }
}
