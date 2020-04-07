package analysis.pipeline;

import analysis.tokenizer.Token;

import java.util.stream.Stream;

public interface AnalyzingPipeline {

    Stream<Token> analyze(String string);
}
