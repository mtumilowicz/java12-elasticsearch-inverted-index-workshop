package shard;

import document.DocumentId;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
class SearchResult {
    private final DocumentId documentId;
    private final Score score;

    static SearchResult of(DocumentId documentId, Score score) {
        return new SearchResult(documentId, score);
    }

    static SearchResult of(Map.Entry<DocumentId, Score> entry) {
        return new SearchResult(entry.getKey(), entry.getValue());
    }
}
