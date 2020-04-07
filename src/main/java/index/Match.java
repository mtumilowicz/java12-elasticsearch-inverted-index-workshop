package index;

import document.DocumentId;
import lombok.Builder;
import lombok.Getter;
import analysis.tokenizer.Token;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.function.Function;

@Builder
@Getter
@ToString
public class Match {

    DocumentId documentId;

    Token token;

    Frequency frequency;

    public Score evaluate(Function<Frequency, BigDecimal> calc) {
        return Score.of(calc.apply(frequency));
    }
}
