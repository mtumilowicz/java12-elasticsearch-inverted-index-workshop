package shard;

import document.Document;
import document.DocumentId;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
class SearchResult {
    private final DocumentId documentId;
    private final BigDecimal score;

    static SearchResult of(DocumentId documentId, BigDecimal score) {
        return new SearchResult(documentId, score);
    }
}
