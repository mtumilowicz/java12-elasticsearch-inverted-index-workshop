package answers.index;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RequiredArgsConstructor
@EqualsAndHashCode
public class Frequency {

    private final long frequency;

    public static Frequency ZERO = new Frequency(0);

    public static Frequency ONE = new Frequency(1);

    public static Frequency of(int i) {
        return new Frequency(i);
    }

    public Frequency increment() {
        return new Frequency(frequency + 1);
    }

    public BigDecimal divide(Frequency divisor) {
        return BigDecimal.valueOf(frequency).divide(BigDecimal.valueOf(divisor.frequency), 4, RoundingMode.CEILING);
    }
}
