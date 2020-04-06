package index;

import document.DocumentId;
import tokenizer.Token;

import java.util.HashMap;
import java.util.Map;

public class InvertedIndex {

    Map<Token, IndexEntries> index = new HashMap<>();

    public void put(Token token, DocumentId documentId) {
        index.computeIfAbsent(token, ignore -> new IndexEntries()).put(documentId, token);

    }

    public IndexEntries get(Token token) {
        return index.get(token);
    }

}

