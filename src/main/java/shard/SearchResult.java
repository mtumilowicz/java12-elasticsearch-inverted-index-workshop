package shard;

import document.Document;
import document.DocumentId;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
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
