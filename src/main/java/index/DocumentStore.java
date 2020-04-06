package index;

import document.Document;
import document.DocumentId;

import java.util.HashMap;
import java.util.Map;

public class DocumentStore {

    private final Map<DocumentId, Document> documents = new HashMap<>();

    public void save(Document document) {
        documents.put(document.getId(), document);
    }

    public Document load(DocumentId documentId) {
        return documents.get(documentId);
    }
}
