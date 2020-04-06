package shard;

import document.Document;
import document.DocumentId;
import index.IndexEntries;
import index.InvertedIndex;
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
        documents.put(document.getId(), document);
        pipeline.analyze(document.getContent())
                .forEach(token -> invertedIndex.put(token, document.getId()));
    }

    Set<IndexEntries> find(String string) {
        return pipeline.analyze(string)
                .map(invertedIndex::get)
                .collect(Collectors.toSet());
    }
}

