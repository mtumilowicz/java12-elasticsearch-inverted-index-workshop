package workshop.analysis.filter.character;

import answers.analysis.filter.character.CharacterFilter;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public class NoiseCharacterFilterWorkshop implements CharacterFilter {

    private final static Set<String> NOISE = Set.of(
            "(", "-",
            ")",
            ".",
            ","
    );

    @Override
    public String apply(String string) {
        // remove all noise character from string, hint: string.split
        return "";
    }
}
