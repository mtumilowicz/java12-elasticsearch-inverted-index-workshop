package index;

import document.DocumentId;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import tokenizer.Token;

@RequiredArgsConstructor
@Getter
public class TokenStats {
    private final DocumentId documentId;
    private final Token token;
    private final Frequency frequency;

    static TokenStats init(DocumentId documentId, Token token) {
        return new TokenStats(documentId, token, Frequency.zero());
    }

    TokenStats incrementFrequency() {
        return new TokenStats(documentId, token, frequency.increment());
    }
}
