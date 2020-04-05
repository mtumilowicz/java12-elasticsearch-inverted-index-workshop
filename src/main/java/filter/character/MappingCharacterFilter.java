package filter.character;

import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

@AllArgsConstructor
public class MappingCharacterFilter implements Function<Stream<String>, Stream<String>> {

    private Map<String, String> map = new HashMap<>();

    {
        map.put("٠", "0");
        map.put("١", "1");
        map.put("٢", "2");
        map.put("٣", "3");
        map.put("٤", "4");
        map.put("٥", "5");
        map.put("٦", "6");
        map.put("٧", "7");
        map.put("٨", "8");
        map.put("٩", "9");
    }

    @Override
    public Stream<String> apply(Stream<String> string) {
        return string.map(str -> map.getOrDefault(str, str));
    }
}
