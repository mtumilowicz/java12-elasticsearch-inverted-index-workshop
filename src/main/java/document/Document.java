package document;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Document {

    private DocumentId id;

    private String content;
}

