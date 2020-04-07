package index

import analysis.tokenizer.Token
import document.DocumentId
import spock.lang.Specification

import java.util.stream.Collectors

class InvertedIndexTest extends Specification {

    def index = new InvertedIndex()

    def 'put - get'() {
        given:
        def token = Token.of('test')
        def documentId = DocumentId.of('1')

        when:
        index.put(token, documentId)

        and:
        def matches = index.get(token).collect(Collectors.toList())

        then: 'single match'
        matches.size() == 1

        and: 'in accordance with token and documentId'
        def match = matches[0]
        match.documentId == documentId
        match.token == token
        match.frequency == Frequency.ONE
    }

    def 'two documents same token'() {
        given:
        def token = Token.of('test')
        def documentId = DocumentId.of('1')
        def documentId2 = DocumentId.of('2')

        when:
        index.put(token, documentId)
        index.put(token, documentId2)

        and:
        def matches = index.get(token).collect(Collectors.toList())

        then: 'two matches'
        matches.size() == 2

        and: 'in accordance with token and documentId'

        and: 'one for doc1 and token'
        def match_doc1 = matches.find {it.documentId == documentId}
        match_doc1.token == token
        match_doc1.frequency == Frequency.ONE

        and: 'one for doc2 and token'
        def match_doc2 = matches.find {it.documentId == documentId2}
        match_doc2.token == token
        match_doc2.frequency == Frequency.ONE
    }

    def 'twice same document and same token'() {
        given:
        def token = Token.of('test')
        def documentId = DocumentId.of('1')

        when:
        index.put(token, documentId)
        index.put(token, documentId)

        and:
        def matches = index.get(token).collect(Collectors.toList())

        then: 'two matches'
        matches.size() == 1

        and: 'frequency is two'
        matches[0].frequency == Frequency.of(2)
    }

    def 'general frequency'() {
        given:
        def token = Token.of('test')
        def token2 = Token.of('test2')
        def documentId = DocumentId.of('1')
        def documentId2 = DocumentId.of('2')
        def documentId3 = DocumentId.of('3')

        when: '2 x token, 1 x token 2'
        index.put(token, documentId)
        index.put(token, documentId2)
        index.put(token2, documentId3)

        then:
        index.generalFrequency(token) == Frequency.of(2)
        index.generalFrequency(token2) == Frequency.ONE
        index.generalFrequency(Token.of("non-existing")) == Frequency.ZERO
    }
}
