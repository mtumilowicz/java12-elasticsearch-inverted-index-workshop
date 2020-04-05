package index;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InvertedIndex {

    Map<String, Index> index = new HashMap<>();

    void put(String token, DocumentId documentId) {
        index.compute(token, (k, index) -> Objects.isNull(index) ? Index.init(documentId, token) : index.incrementFrequency());
    }

    Index get(String token) {
        return index.get(token);
    }

}

