package filter.token

import spock.lang.Specification
import tokenizer.Token

import java.util.stream.Collectors
import java.util.stream.Stream

class LowercaseTokenFilterTest extends Specification {

    def filter = new LowercaseTokenFilter()

    def 'filtering'() {
        given:
        def input = [
                Token.of('A'),
                Token.of('a'),
                Token.of('B'),
                Token.of('B'),
                Token.of('C'),
                Token.of('d'),
                Token.of('e'),
                Token.of('F')
                ]

        and:
        def expectedOutput = [
                Token.of('a'),
                Token.of('a'),
                Token.of('b'),
                Token.of('b'),
                Token.of('c'),
                Token.of('d'),
                Token.of('e'),
                Token.of('f')
        ]

        expect:
        filter.apply(input.stream()).collect(Collectors.toList()) == expectedOutput
    }
}
