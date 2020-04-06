package shard;

import document.Document;
import document.DocumentId;
import index.Frequency;
import index.InvertedIndex;
import index.Match;
import pipeline.AnalyzingPipeline;
import pipeline.StandardPipeline;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Shard {
    InvertedIndex invertedIndex = new InvertedIndex();
    Map<DocumentId, Document> documents = new HashMap<>();
    AnalyzingPipeline pipeline = new StandardPipeline();

    void index(Document document) {
        documents.put(document.getId(), document);
        pipeline.analyze(document.getContent())
                .forEach(token -> invertedIndex.put(token, document.getId()));
    }

    Set<SearchResult> find(String string) {
        var collect = pipeline.analyze(string)
                .flatMap(token -> invertedIndex.get(token))
                .collect(Collectors.groupingBy(Match::getDocumentId, Collectors.collectingAndThen(Collectors.toList(), this::evaluateScore)));

        return collect.entrySet()
                .stream()
                .map(x -> SearchResult.of(x.getKey(), x.getValue()))
                .collect(Collectors.toSet());
    }

    Score evaluateScore(List<Match> stats) {
        var value = stats.stream()
                .map(x -> x.evaluate(Frequency::divide))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return Score.of(value);
    }
}

