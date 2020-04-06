package index;

import document.Document;
import document.DocumentId;
import lombok.AllArgsConstructor;
import tokenizer.Token;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class InvertedIndex {

    Map<Token, IndexEntries> index = new HashMap<>();

    public void put(Token token, DocumentId documentId) {
        index.computeIfAbsent(token, ignore -> new IndexEntries()).put(documentId, token);

    }

    public Stream<Yyy> get(Token token) {
        return index.get(token).stream();
    }

}

