package index;

import pipeline.AnalyzingPipeline;
import pipeline.StandardPipeline;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Shard {
    InvertedIndex invertedIndex = new InvertedIndex();
    Map<DocumentId, Document> documents = new HashMap<>();
    AnalyzingPipeline pipeline = new StandardPipeline();

    void index(Document document) {
        documents.put(document.id, document);
        pipeline.analyze(document.content())
                .forEach(token -> invertedIndex.put(token, document.id));
    }

    Set<Index> find(String string) {
        return pipeline.analyze(Stream.of(string))
                .map(invertedIndex::get)
                .collect(Collectors.toSet());
    }
}

