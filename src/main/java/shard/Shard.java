package shard;

import document.Document;
import document.DocumentId;
import index.Frequency;
import index.IndexEntries;
import index.InvertedIndex;
import index.TokenStats;
import pipeline.AnalyzingPipeline;
import pipeline.StandardPipeline;

import java.util.*;
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
        Map<DocumentId, Long> collect = pipeline.analyze(string)
                .map(invertedIndex::get)
                .map(IndexEntries::getStats)
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(TokenStats::getDocumentId, Collectors.collectingAndThen(Collectors.toList(), this::evaluateScore)));

        return collect.entrySet()
                .stream()
                .map(x -> SearchResult.of(x.getKey(), x.getValue()))
                .collect(Collectors.toSet());
    }

    Long evaluateScore(List<TokenStats> stats) {
        return stats.stream()
                .map(TokenStats::getFrequency)
                .mapToLong(Frequency::raw)
                .sum();
    }
}

