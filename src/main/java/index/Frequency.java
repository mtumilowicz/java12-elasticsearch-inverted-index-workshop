package index;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class Frequency {

    private final long frequency;

    static Frequency zero() {
        return new Frequency(0);
    }

    Frequency increment() {
        return new Frequency(frequency + 1);
    }

    public long raw() {
        return frequency;
    }

    public BigDecimal divide(GeneralFrequency frequency) {
        return BigDecimal.valueOf(raw()).divide(BigDecimal.valueOf(frequency.raw()));
    }
}
