package answers.index;

import answers.document.DocumentId;
import answers.analysis.tokenizer.Token;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class Statistics {

    private final Map<Token, Map<DocumentId, Frequency>> stats = new HashMap<>();

    void put(Token token, DocumentId documentId) {
        stats.computeIfAbsent(token, ignore -> new HashMap<>())
                .compute(documentId, (docId, freq) -> Objects.isNull(freq) ? Frequency.ONE : freq.increment());
    }

    Map<DocumentId, Frequency> frequenciesOf(Token token) {
        return stats.getOrDefault(token, Collections.emptyMap());
    }

    Frequency frequenciesInDocument(Token token, DocumentId documentId) {
        return frequenciesOf(token).getOrDefault(documentId, Frequency.ZERO);
    }
}
