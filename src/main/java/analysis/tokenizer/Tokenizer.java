package analysis.tokenizer;

import java.util.function.Function;
import java.util.stream.Stream;

public interface Tokenizer extends Function<String, Stream<Token>> {
}
