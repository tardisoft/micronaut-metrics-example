package io.micronaut.examples

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import spock.lang.Specification


class PersonServiceSpec extends Specification {

    Counter counter
    PersonService personService
    MeterRegistry meterRegistry = Mock(MeterRegistry)

    def setup(){
        counter = Mock(Counter)
        personService = new PersonService(meterRegistry)
    }

    def "upper case"() {
        when:
        String name = personService.upperCase("bob")

        then:
        name == "BOB"
        1 * meterRegistry.counter(_,_ ) >> counter
        1 * counter.increment()
    }

}
