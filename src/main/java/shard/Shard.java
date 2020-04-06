package shard;

import document.Document;
import document.DocumentId;
import index.DocumentStore;
import index.InvertedIndex;
import index.Match;
import pipeline.AnalyzingPipeline;
import pipeline.StandardPipeline;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Shard {

    InvertedIndex invertedIndex = new InvertedIndex();

    DocumentStore documents = new DocumentStore();

    AnalyzingPipeline pipeline = new StandardPipeline();

    void index(Document document) {
        documents.save(document);
        pipeline.analyze(document.getContent())
                .forEach(token -> invertedIndex.put(token, document.getId()));
    }

    Set<SearchResult> find(String string) {
        Map<DocumentId, Score> scores = pipeline.analyze(string)
                .flatMap(token -> invertedIndex.get(token))
                .collect(Collectors.groupingBy(Match::getDocumentId,
                        Collectors.collectingAndThen(Collectors.toList(), this::evaluateScore)));

        return scores.entrySet()
                .stream()
                .map(SearchResult::of)
                .collect(Collectors.toSet());
    }

    private Score evaluateScore(List<Match> stats) {
        var value = stats.stream()
                .map(scoringStrategy())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return Score.of(value);
    }

    private Function<Match, BigDecimal> scoringStrategy() {
        return match -> {
            var generalFrequency = invertedIndex.generalFrequency(match.getToken());
            return match.evaluate(frequency -> frequency.divide(generalFrequency));
        };
    }
}
