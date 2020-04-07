package workshop.index;

import answers.analysis.tokenizer.Token;
import answers.document.DocumentId;
import answers.index.Frequency;
import answers.index.Match;

import java.util.*;
import java.util.stream.Stream;

public class InvertedIndexWorkshop {

    private final Map<Token, Set<DocumentId>> invertedIndex = new HashMap<>();

    private final StatisticsWorkshop statistics = new StatisticsWorkshop();

    public void put(Token token, DocumentId documentId) {
        // put in inverted index, hint: computeIfAbsent
        // put in statistics
        statistics.put(token, documentId);
    }

    public Frequency generalFrequency(Token token) {
        // count documents containing token, hint: getDocumentsContaining
        return null;
    }

    public Stream<Match> get(Token token) {
        // convert into stream of matches, hint: Match.builder(), statistics.frequenciesInDocument
        return null;
    }

    private Set<DocumentId> getDocumentsContaining(Token token) {
        // hint: getOrDefault
        return null;
    }
}

