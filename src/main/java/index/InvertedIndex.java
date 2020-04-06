package index;

import document.DocumentId;
import tokenizer.Token;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class InvertedIndex {

    Map<Token, Set<DocumentId>> index = new HashMap<>();
    Map<Token, Map<DocumentId, Frequency>> stats = new HashMap<>();

    public void put(Token token, DocumentId documentId) {
        index.computeIfAbsent(token, ignore -> new HashSet<>()).add(documentId);
        stats.computeIfAbsent(token, ignore -> new HashMap<>())
                .computeIfAbsent(documentId, ignore -> Frequency.zero())
                .increment();

    }

    public Stream<Yyy> get(Token token) {
        return index.getOrDefault(token, new HashSet<>())
                .stream()
                .map(x -> new Yyy(x, stats.get(token).get(x), new Frequency(index.get(token).size())));
    }

}

