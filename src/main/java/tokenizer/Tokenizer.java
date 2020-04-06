package tokenizer;

import java.util.function.Function;
import java.util.stream.Stream;

public interface Tokenizer extends Function<Stream<String>, Stream<Token>> {
}
