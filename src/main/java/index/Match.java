package index;

import analysis.tokenizer.Token;
import document.DocumentId;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;
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

    public static Score score(List<Match> matches, Function<Match, Score> strategy) {
        return matches.stream()
                .map(strategy)
                .reduce(Score.ZERO, Score::add);
    }
}
