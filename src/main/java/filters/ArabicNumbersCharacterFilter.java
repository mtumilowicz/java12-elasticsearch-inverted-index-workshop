package filters;

import java.util.Map;

public class ArabicNumbersCharacterFilter implements CharacterFilter {

    private final MappingCharacterFilter filter;

    public ArabicNumbersCharacterFilter() {
        filter = new MappingCharacterFilter(Map.of(
                "٠", "0",
                "١", "1",
                "٢", "2",
                "٣", "3",
                "٤", "4",
                "٥", "5",
                "٦", "6",
                "٧", "7",
                "٨", "8",
                "٩", "9"
        )
        );
    }

    @Override
    public String filter(String string) {
        return filter.filter(string);
    }
}
