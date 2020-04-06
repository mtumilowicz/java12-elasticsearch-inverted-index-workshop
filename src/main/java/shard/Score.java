package shard;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Score {

    private final BigDecimal score;

    public static Score of(BigDecimal score) {
        return new Score(score);
    }
}
