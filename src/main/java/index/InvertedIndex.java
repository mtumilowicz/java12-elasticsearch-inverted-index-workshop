package index;

import document.DocumentId;
import tokenizer.Token;

import java.util.*;
import java.util.stream.Stream;

public class InvertedIndex {

    private final Map<Token, Set<DocumentId>> invertedIndex = new HashMap<>();

    private final Statistics statistics = new Statistics();

    public void put(Token token, DocumentId documentId) {
        invertedIndex.computeIfAbsent(token, ignore -> new HashSet<>()).add(documentId);
        statistics.put(token, documentId);
    }

    public Set<DocumentId> getDocumentsContaining(Token token) {
        return invertedIndex.getOrDefault(token, Collections.emptySet());
    }

    public Frequency generalFrequency(Token token) {
        return new Frequency(getDocumentsContaining(token).size());
    }

    public Stream<Match> get(Token token) {
        return getDocumentsContaining(token)
                .stream()
                .map(documentId -> Match.builder()
                        .token(token)
                        .documentId(documentId)
                        .frequency(statistics.frequenciesInDocument(token, documentId))
                        .build());
    }

}

