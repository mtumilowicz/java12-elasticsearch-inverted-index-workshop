package filter.character;

import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public interface CharacterFilter extends UnaryOperator<Stream<String>> {
}
