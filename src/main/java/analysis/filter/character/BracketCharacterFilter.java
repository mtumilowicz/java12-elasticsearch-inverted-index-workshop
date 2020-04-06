package analysis.filter.character;

import java.util.Map;

public class BracketCharacterFilter implements CharacterFilter {

    private final MappingCharacterFilter filter;

    private final static Map<String, String> arabicNumbers = Map.of(
            "<", "(",
            ">", ")",
            "{", "(",
            "}", ")",
            "[", "(",
            "]", ")"
    );

    public BracketCharacterFilter() {
        filter = new MappingCharacterFilter(arabicNumbers);
    }

    @Override
    public String apply(String string) {
        return filter.apply(string);
    }
}
