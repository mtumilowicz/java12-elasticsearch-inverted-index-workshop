package answers.shard;

import answers.analysis.pipeline.AnalyzingPipeline;
import answers.analysis.pipeline.StandardPipeline;
import answers.document.Document;
import answers.document.DocumentId;
import answers.document.DocumentStore;
import answers.index.InvertedIndex;
import answers.index.Match;
import answers.index.Score;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Shard {

    private final InvertedIndex invertedIndex = new InvertedIndex();

    private final DocumentStore documents = new DocumentStore();

    private final AnalyzingPipeline pipeline = new StandardPipeline();

    public void index(Document document) {
        documents.save(document);
        pipeline.analyze(document.getContent())
                .forEach(token -> invertedIndex.put(token, document.getId()));
    }

    public Document get(DocumentId documentId) {
        return documents.load(documentId);
    }

    public List<SearchResult> find(String query) {
        Map<DocumentId, Score> scores = calculateScoresForEachDocument(() -> findAllMatches(query));

        return scores.entrySet()
                .stream()
                .map(SearchResult::of)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    private Stream<Match> findAllMatches(String query) {
        return pipeline.analyze(query)
                .flatMap(invertedIndex::get);
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

