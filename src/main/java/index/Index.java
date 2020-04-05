package index;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class Index {
    private final DocumentId documentId;
    private final String token;
    private final Frequency frequency;

    static Index init(DocumentId documentId, String token) {
        return new Index(documentId, token, Frequency.zero());
    }

    Index incrementFrequency() {
        return new Index(documentId, token, frequency.increment());
    }
}
