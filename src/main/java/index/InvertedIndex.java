package index;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InvertedIndex {

    Map<String, Set<String>> index = new HashMap<>();

    void put(String token) {
        index.computeIfAbsent(token, ignore -> new HashSet<>()).add(token);
    }

    Set<String> get(String token) {
        return index.get(token);
    }

}
