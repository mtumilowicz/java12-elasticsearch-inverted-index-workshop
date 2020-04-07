package workshop.analysis.pipeline

import answers.analysis.pipeline.StandardPipeline
import answers.analysis.tokenizer.Token
import spock.lang.Specification

import java.util.stream.Collectors

class StandardPipelineWorkshopTest extends Specification {

    def pipeline = new StandardPipelineWorkshop()

    def 'analyze'() {
        given:
        def input = 'My favourite Event (Warsaw JAVA user group) takes place in a building owned by the MIM-UW'

        and:
        def expectedOutput = [
                'favourite', 'event', 'warsaw', 'java', 'user', 'group',
                'takes', 'place', 'building', 'owned', 'mimuw'
        ].collect { Token.of it }

        expect:
        pipeline.analyze(input).collect(Collectors.toList()) == expectedOutput
    }
}
