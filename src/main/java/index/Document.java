package index;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
class Document {
    String id;
    String body;

    Stream<String> fieldValues() {
        return Stream.ofNullable(body);
    }
}
