package index;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GeneralFrequency {

    private final long frequency;

    static GeneralFrequency zero() {
        return new GeneralFrequency(0);
    }

    GeneralFrequency increment() {
        return new GeneralFrequency(frequency + 1);
    }

    public long raw() {
        return frequency;
    }
}
