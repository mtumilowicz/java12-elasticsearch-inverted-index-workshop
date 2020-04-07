package answers.shard;

import answers.index.Score;
import answers.document.DocumentId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Map;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
class SearchResult implements Comparable<SearchResult> {

    private final DocumentId documentId;

    private final Score score;

    static SearchResult of(Map.Entry<DocumentId, Score> entry) {
        return new SearchResult(entry.getKey(), entry.getValue());
    }

    @Override
    public int compareTo(SearchResult o) {
        return score.compareTo(o.score);
    }
}
