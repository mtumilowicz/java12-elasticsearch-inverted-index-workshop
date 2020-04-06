package index;

import lombok.RequiredArgsConstructor;

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
}
