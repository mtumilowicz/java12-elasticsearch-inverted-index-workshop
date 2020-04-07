package answers.analysis.filter.character;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public class NoiseCharacterFilter implements CharacterFilter {

    private final static Set<String> NOISE = Set.of(
            "(", "-",
            ")",
            ".",
            ","
    );

    @Override
    public String apply(String string) {
        return Stream.of(string.split(""))
                .filter(not(NOISE::contains))
                .collect(Collectors.joining());
    }
}
