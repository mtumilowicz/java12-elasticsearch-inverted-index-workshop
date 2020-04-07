package index;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.function.Function;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Score {

    private final BigDecimal score;

    public static Score of(BigDecimal score) {
        return new Score(score);
    }

    public static Score ZERO = Score.of(BigDecimal.ZERO);

    public Score add(Score other) {
        return Score.of(score.add(other.score));
    }
}
