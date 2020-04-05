package filter.character;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class ArabicNumbersCharacterFilter implements Function<Stream<String>, Stream<String>> {

    private final MappingCharacterFilter filter;

    private final static Map<String, String> arabicNumbers = Map.of(
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
    );

    public ArabicNumbersCharacterFilter() {
        filter = new MappingCharacterFilter(arabicNumbers);
    }

    @Override
    public Stream<String> apply(Stream<String> string) {
        return filter.apply(string);
    }
}
