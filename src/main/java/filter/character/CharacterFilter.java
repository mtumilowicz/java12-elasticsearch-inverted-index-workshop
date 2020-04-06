package filter.character;

import java.util.function.UnaryOperator;

public interface CharacterFilter extends UnaryOperator<String> {

    default CharacterFilter andThen(CharacterFilter after) {
        return t -> after.apply(apply(t));
    }
}
