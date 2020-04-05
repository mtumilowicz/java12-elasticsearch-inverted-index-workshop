package filter.token;

import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public interface TokenFilter extends UnaryOperator<Stream<String>> {

    default TokenFilter andThen(TokenFilter after) {
        return t -> after.apply(apply(t));
    }
}
