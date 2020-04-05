package index;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InvertedIndex {

    Map<String, Set<DocumentId>> index = new HashMap<>();

    void put(String token, DocumentId id) {
        index.computeIfAbsent(token, ignore -> new HashSet<>()).add(id);
    }

    Set<DocumentId> get(String token) {
        return index.get(token);
    }

}
