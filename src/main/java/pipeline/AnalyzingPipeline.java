package pipeline;

import java.util.stream.Stream;

public interface AnalyzingPipeline {
    Stream<String> analyze(String string);
}
