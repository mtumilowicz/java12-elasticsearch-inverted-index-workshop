package index;

import document.DocumentId;
import tokenizer.Token;

import java.util.*;
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

    public Set<DocumentId> getDocumentsContaining(Token token) {
        return index.getOrDefault(token, Collections.emptySet());
    }

    public Map<DocumentId, Frequency> frequenciesOf(Token token) {
        return stats.getOrDefault(token, Collections.emptyMap());
    }

    public Frequency frequenciesInDocument(Token token, DocumentId documentId) {
        return frequenciesOf(token).getOrDefault(documentId, Frequency.zero());
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
                        .frequency(frequenciesInDocument(token, documentId))
                        .build());
    }

}

