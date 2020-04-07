package shard;

import analysis.pipeline.AnalyzingPipeline;
import analysis.pipeline.StandardPipeline;
import document.Document;
import document.DocumentId;
import document.DocumentStore;
import index.InvertedIndex;
import index.Match;
import index.Score;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Shard {

    InvertedIndex invertedIndex = new InvertedIndex();

    DocumentStore documents = new DocumentStore();

    AnalyzingPipeline pipeline = new StandardPipeline();

    void index(Document document) {
        documents.save(document);
        pipeline.analyze(document.getContent())
                .forEach(token -> invertedIndex.put(token, document.getId()));
    }

    Document get(DocumentId documentId) {
        return documents.load(documentId);
    }

    Set<SearchResult> find(String string) {
        Supplier<Stream<Match>> matches = () -> pipeline.analyze(string)
                .flatMap(token -> invertedIndex.get(token));

        Map<DocumentId, Score> scores = calculateScoresForEachDocument(matches);

        return scores.entrySet()
                .stream()
                .map(SearchResult::of)
                .collect(Collectors.toSet());
    }

    private Map<DocumentId, Score> calculateScoresForEachDocument(Supplier<Stream<Match>> matches) {
        return matches.get().collect(Collectors.groupingBy(Match::getDocumentId,
                Collectors.collectingAndThen(Collectors.toList(),
                        perDocumentMatches -> Match.reduce(perDocumentMatches, scoringStrategy()))));
    }

    private Function<Match, BigDecimal> scoringStrategy() {
        return match -> {
            var generalFrequency = invertedIndex.generalFrequency(match.getToken());
            return match.getFrequency().divide(generalFrequency);
        };
    }
}

