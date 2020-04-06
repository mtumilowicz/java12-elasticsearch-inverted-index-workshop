package filter.token

import analysis.filter.token.LowercaseTokenFilter
import spock.lang.Specification
import analysis.tokenizer.Token

import java.util.stream.Collectors

class LowercaseTokenFilterTest extends Specification {

    def filter = new LowercaseTokenFilter()

    def 'filtering'() {
        given:
        def input = ['A', 'a', 'B', 'B', 'C', 'd', 'e', 'F'].collect { Token.of(it) }

        and:
        def expectedOutput = ['a', 'a', 'b', 'b', 'c', 'd', 'e', 'f'].collect { Token.of(it) }

        expect:
        filter.apply(input.stream()).collect(Collectors.toList()) == expectedOutput
    }
}
