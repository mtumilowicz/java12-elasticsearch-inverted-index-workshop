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

    public static Score reduce(List<Match> matches, Function<Match, BigDecimal> strategy) {
        return matches.stream()
                .map(strategy)
                .map(Score::of)
                .reduce(Score.ZERO, Score::add);
    }
}
