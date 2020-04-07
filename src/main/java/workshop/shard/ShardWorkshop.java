package workshop.shard;

import answers.analysis.pipeline.AnalyzingPipeline;
import answers.analysis.pipeline.StandardPipeline;
import answers.document.Document;
import answers.document.DocumentId;
import answers.document.DocumentStore;
import answers.index.InvertedIndex;
import answers.index.Match;
import answers.index.Score;
import answers.shard.SearchResult;
import workshop.analysis.pipeline.StandardPipelineWorkshop;
import workshop.index.InvertedIndexWorkshop;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ShardWorkshop {

    private final InvertedIndexWorkshop invertedIndex = new InvertedIndexWorkshop();

    private final DocumentStore documents = new DocumentStore();

    private final StandardPipelineWorkshop pipeline = new StandardPipelineWorkshop();

    public void index(Document document) {
        // save document in documents
        // analyze the content and put in inverted index, hint: pipeline.analyze
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
        // analyze query and find matches in inverted index, hint: pipeline.analyze, invertedIndex::get
        return null;
    }

    private Map<DocumentId, Score> calculateScoresForEachDocument(Supplier<Stream<Match>> matches) {
        // group matches by document id then reduce matches in each group by scoring strategy
        // hint: groupingBy, collectingAndThen, Match.reduce
        return null;
    }

    private Function<Match, BigDecimal> scoringStrategy() {
        // TF-IDF, hint: invertedIndex.generalFrequency, Frequency.divide
        return null;
    }
}

