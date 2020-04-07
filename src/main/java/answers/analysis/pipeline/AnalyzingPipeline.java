package answers.analysis.pipeline;

import answers.analysis.tokenizer.Token;

import java.util.stream.Stream;

public interface AnalyzingPipeline {

    Stream<Token> analyze(String string);
}
