package filter.character;

import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public interface CharacterFilter extends UnaryOperator<Stream<String>> {

    default CharacterFilter andThen(CharacterFilter after) {
        return t -> after.apply(apply(t));
    }
}
