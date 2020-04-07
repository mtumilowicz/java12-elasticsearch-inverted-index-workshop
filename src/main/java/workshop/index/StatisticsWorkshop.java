package workshop.index;

import answers.analysis.tokenizer.Token;
import answers.document.DocumentId;
import answers.index.Frequency;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class StatisticsWorkshop {

    private final Map<Token, Map<DocumentId, Frequency>> stats = new HashMap<>();

    void put(Token token, DocumentId documentId) {
        // hint: computeIfAbsent, compute
    }

    Map<DocumentId, Frequency> frequenciesOf(Token token) {
        // frequencies of token by document, hint: getOrDefault
        return null;
    }

    Frequency frequenciesInDocument(Token token, DocumentId documentId) {
        // frequencies of token in specific document, hint: frequenciesOf, getOrDefault
        return null;
    }
}
