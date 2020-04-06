package filter.character

import spock.lang.Specification

class BracketCharacterFilterTest extends Specification {

    def filter = new BracketCharacterFilter()

    def 'filtering'() {
        given:
        def input = '{ a ( 2 b ) c ] [ d ] e f g } h 3'

        expect:
        filter.apply(input) == '( a ( 2 b ) c ) ( d ) e f g ) h 3'
    }

}
