package io.micronaut.examples;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;

import javax.inject.Singleton;
import java.util.Arrays;

@Singleton
public class PersonService {

    private MeterRegistry meterRegistry;

    public PersonService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    String upperCase(String name) {
        meterRegistry.counter("web.service", Arrays.asList(Tag.of("service", "person"), Tag.of("method", "capitalize"))).increment();
        return name.toUpperCase();
    }
}
