package index;

import tokenizer.Token;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InvertedIndex {

    Map<Token, Index> index = new HashMap<>();

    void put(Token token, DocumentId documentId) {
        index.compute(token, (k, index) -> Objects.isNull(index) ? Index.init(documentId, token) : index.incrementFrequency());
    }

    Index get(Token token) {
        return index.get(token);
    }

}

