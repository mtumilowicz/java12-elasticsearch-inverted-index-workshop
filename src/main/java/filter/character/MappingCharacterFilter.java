package filter.character;

import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class MappingCharacterFilter implements CharacterFilter {

    private final Map<String, String> map;

    @Override
    public String apply(String string) {
        return Stream.of(string.split(""))
                .map(str -> map.getOrDefault(str, str))
                .collect(Collectors.joining());
    }
}
