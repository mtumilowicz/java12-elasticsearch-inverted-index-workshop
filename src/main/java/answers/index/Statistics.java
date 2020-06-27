package answers.index;

import answers.analysis.tokenizer.Token;
import answers.document.DocumentId;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class Statistics {

    private final Map<Token, Map<DocumentId, Frequency>> stats = new HashMap<>();

    void put(Token token, DocumentId documentId) {
        stats.computeIfAbsent(token, ignore -> new HashMap<>())
                .merge(documentId, Frequency.ONE, (prev, one) -> prev.increment());
    }

    Map<DocumentId, Frequency> frequenciesOf(Token token) {
        return stats.getOrDefault(token, Collections.emptyMap());
    }

    Frequency frequenciesInDocument(Token token, DocumentId documentId) {
        return frequenciesOf(token).getOrDefault(documentId, Frequency.ZERO);
    }
}
