package index;

import document.DocumentId;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.function.BiFunction;

@AllArgsConstructor
@Getter
public class Match {
    DocumentId documentId;
    Frequency frequency;
    GeneralFrequency generalFrequency;

    public BigDecimal evaluate(BiFunction<Frequency, GeneralFrequency, BigDecimal> calc) {
        return calc.apply(frequency, generalFrequency);
    }
}
