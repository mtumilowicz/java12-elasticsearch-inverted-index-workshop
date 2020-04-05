package index;

import pipeline.AnalyzingPipeline;
import pipeline.StandardPipeline;

import java.util.HashMap;
import java.util.Map;

public class Index {
    InvertedIndex invertedIndex = new InvertedIndex();
    Map<String, Document> documents = new HashMap<>();
    AnalyzingPipeline pipeline = new StandardPipeline();

    void index(Document document) {
        documents.put(document.id, document);
        document.fieldValues().flatMap(pipeline::analyze).forEach(invertedIndex::put);
    }
}

