package shard;

import document.Document;
import document.DocumentId;
import index.InvertedIndex;
import index.Yyy;
import pipeline.AnalyzingPipeline;
import pipeline.StandardPipeline;

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
        // token1, ... token10
        // (d1, 15, 300) d2, 30, ...,
        var collect = pipeline.analyze(string)
                .flatMap(token -> invertedIndex.get(token))
                .collect(Collectors.groupingBy(Yyy::getDocumentId, Collectors.collectingAndThen(Collectors.toList(), this::evaluateScore)));

        return collect.entrySet()
                .stream()
                .map(x -> SearchResult.of(x.getKey(), x.getValue()))
                .collect(Collectors.toSet());
    }

    Long evaluateScore(List<Yyy> stats) {
        return stats.stream()
                .mapToLong(x -> x.getFrequency().raw() / x.getGeneralFrequency().raw())
                .sum();
    }
}

