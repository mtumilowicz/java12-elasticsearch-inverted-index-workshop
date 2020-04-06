package filter.token

import spock.lang.Specification
import tokenizer.Token

import java.util.stream.Collectors

class StopWordTokenFilterTest extends Specification {

    def filter = new StopWordTokenFilter()

    def 'filtering'() {
        given:
        def input = ['however', 'the', 'saying', 'my', 'home', 'is', 'my', 'castle'].collect { Token.of(it)}

        and:
        def expectedOutput = ['saying', 'home', 'castle'].collect { Token.of(it)}

        expect:
        filter.apply(input.stream()).collect(Collectors.toList()) == expectedOutput
    }
}
