package shard;

import document.DocumentId;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
class SearchResult {

    private final DocumentId documentId;

    private final Score score;

    static SearchResult of(Map.Entry<DocumentId, Score> entry) {
        return new SearchResult(entry.getKey(), entry.getValue());
    }
}
