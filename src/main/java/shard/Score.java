package shard;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class Score {
    private final BigDecimal score;

    public static Score of(BigDecimal score) {
        return new Score(score);
    }
}
