package index;

import tokenizer.Token;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

class IndexEntries {
    private final Map<DocumentId, TokenStats> map = new HashMap<>();

    void put(DocumentId documentId, Token token) {
        map.compute(documentId, xxx(token));
    }

    BiFunction<DocumentId, TokenStats, TokenStats> xxx(Token token) {
        return (docId, tokenStats) -> Objects.isNull(tokenStats)
                ? TokenStats.init(docId, token)
                : tokenStats.incrementFrequency();
    }
}
