package index;

import document.DocumentId;
import lombok.Builder;
import lombok.Getter;
import tokenizer.Token;

import java.math.BigDecimal;
import java.util.function.Function;

@Builder
@Getter
public class Match {

    DocumentId documentId;

    Token token;

    Frequency frequency;

    public BigDecimal evaluate(Function<Frequency, BigDecimal> calc) {
        return calc.apply(frequency);
    }
}
