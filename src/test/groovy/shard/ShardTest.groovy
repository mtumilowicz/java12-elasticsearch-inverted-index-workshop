package shard

import document.Document
import document.DocumentId
import index.Score
import spock.lang.Specification

class ShardTest extends Specification {

    def shard = new Shard()

    def 'index-get: when you index document, it is saved'() {
        given:
        def document = Document.builder()
                .id(DocumentId.of('1'))
                .content('content')
                .build()

        when:
        shard.index(document)

        then:
        shard.get(document.id) is document
    }

    def 'single case: when you search for a document - score are calculated'() {
        given:
        def document = Document.builder()
                .id(DocumentId.of('1'))
                .content('content')
                .build()

        and:
        shard.index(document)

        when:
        def results = shard.find('content')

        then:
        results.size() == 1
        def result = results[0]
        result.documentId == document.id
        result.score == Score.of(BigDecimal.valueOf(1))
    }

    def 'many cases: when you search for a documents - scores are calculated'() {
        given:
        def document = Document.builder()
                .id(DocumentId.of('1'))
                .content('The event takes place in the class')
                .build()

        def document2 = Document.builder()
                .id(DocumentId.of('2'))
                .content('The event is DECLINED')
                .build()

        def document3 = Document.builder()
                .id(DocumentId.of('3'))
                .content('The event takes place in the class and outdoors as well')
                .build()

        def document4 = Document.builder()
                .id(DocumentId.of('4'))
                .content('no way that I will participate')
                .build()

        and:
        shard.index(document)
        shard.index(document2)
        shard.index(document3)
        shard.index(document4)

        when:
        def results = shard.find('event declined')

        then:
        println results
        results.size() == 3
        def top1 = results[0]
        top1.documentId == document2.id
        top1.score == Score.of(BigDecimal.valueOf(1.3334))

        and:
        results[1].documentId == document.id
        results[2].documentId == document3.id
        results[1].score == results[2].score
    }
}
