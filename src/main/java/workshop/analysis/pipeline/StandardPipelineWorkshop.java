package workshop.analysis.pipeline;

import answers.analysis.filter.character.CharacterFilter;
import answers.analysis.filter.token.TokenFilter;
import answers.analysis.pipeline.AnalyzingPipeline;
import answers.analysis.tokenizer.Token;
import answers.analysis.tokenizer.Tokenizer;
import workshop.analysis.filter.character.NoiseCharacterFilterWorkshop;
import workshop.analysis.filter.token.LowercaseTokenFilterWorkshop;
import workshop.analysis.filter.token.StopWordTokenFilter;
import workshop.analysis.tokenizer.SpaceTokenizerWorkshop;

import java.util.stream.Stream;


public class StandardPipelineWorkshop implements AnalyzingPipeline {

    public Stream<Token> analyze(String string) {
        // NoiseCharacterFilterWorkshop -> SpaceTokenizerWorkshop -> LowercaseTokenFilterWorkshop -> StopWordTokenFilter
        return null;
    }
}
