package index;

import lombok.RequiredArgsConstructor;
import tokenizer.Token;

@RequiredArgsConstructor
class Index {
    private final DocumentId documentId;
    private final Token token;
    private final Frequency frequency;

    static Index init(DocumentId documentId, Token token) {
        return new Index(documentId, token, Frequency.zero());
    }

    Index incrementFrequency() {
        return new Index(documentId, token, frequency.increment());
    }
}
