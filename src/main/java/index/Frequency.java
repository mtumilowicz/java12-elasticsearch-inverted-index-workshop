package index;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class Frequency {
    private final Long frequency;

    Frequency of(Long frequency) {
        return new Frequency(frequency);
    }

    static Frequency zero() {
        return new Frequency(0L);
    }

    Frequency increment() {
        return new Frequency(frequency + 1);
    }
}
