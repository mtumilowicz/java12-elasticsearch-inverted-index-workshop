package index;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@Getter
class Document {
    DocumentId id;
    String content;

    Stream<String> content() {
        return Stream.ofNullable(content);
    }
}

