package index;

import pipeline.AnalyzingPipeline;
import pipeline.StandardPipeline;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Shard {
    InvertedIndex invertedIndex = new InvertedIndex();
    Map<DocumentId, Document> documents = new HashMap<>();
    AnalyzingPipeline pipeline = new StandardPipeline();

    void index(Document document) {
        documents.put(document.id, document);
        document.content().flatMap(pipeline::analyze).forEach(token -> invertedIndex.put(token, document.id));
    }

    Set<Index> find(String string) {
        return pipeline.analyze(string)
                .map(invertedIndex::get)
                .collect(Collectors.toSet());
    }
}

