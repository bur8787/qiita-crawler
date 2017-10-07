import spock.lang.Specification
import spock.lang.Unroll

class QiitaUserNameCrawlerTest extends Specification {
    @Unroll
    def "test nextChar at #c"() {
        when:
        def test = QiitaUserNameCrawler.nextChar(c)

        then:
        test == expected

        where:
        c | expected
        "A" | "B"
        "Z" | "0"
        "0" | "1"
        "9" | "_"
        "_" | null
    }
}
