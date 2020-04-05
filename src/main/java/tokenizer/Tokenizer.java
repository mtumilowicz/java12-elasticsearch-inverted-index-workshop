package tokenizer;

import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public interface Tokenizer extends UnaryOperator<Stream<String>> {

    default Tokenizer andThen(Tokenizer after) {
        return t -> after.apply(apply(t));
    }
}
