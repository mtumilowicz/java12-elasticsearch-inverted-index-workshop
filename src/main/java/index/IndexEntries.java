package index;

import document.DocumentId;
import tokenizer.Token;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public class IndexEntries {
    private final Map<DocumentId, TokenStats> map = new HashMap<>();

    void put(DocumentId documentId, Token token) {
        map.compute(documentId, xxx(token));
    }

    BiFunction<DocumentId, TokenStats, TokenStats> xxx(Token token) {
        return (docId, tokenStats) -> Objects.isNull(tokenStats)
                ? TokenStats.init(docId, token)
                : tokenStats.incrementFrequency();
    }

    public List<TokenStats> getStats() {
        return new LinkedList<>(map.values());
    }

    public Stream<Yyy> stream() {
        return map.entrySet().stream().map(x -> new Yyy(x.getKey(), x.getValue().getFrequency(), new Frequency(map.size())));
    }
}
