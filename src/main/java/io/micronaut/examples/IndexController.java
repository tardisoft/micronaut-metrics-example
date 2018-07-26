package io.micronaut.examples;

import io.micrometer.core.instrument.MeterRegistry;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.reactivex.Single;

import javax.validation.constraints.NotBlank;
import java.net.URI;

@Controller("/")
public class IndexController {

    private MeterRegistry meterRegistry;
    private PersonService personService;

    public IndexController(MeterRegistry meterRegistry, PersonService personService) {
        this.meterRegistry = meterRegistry;
        this.personService = personService;
    }

    @Produces(MediaType.TEXT_HTML)
    @Get("/")
    HttpResponse index() {
        meterRegistry
                .counter("web.access", "controller", "index", "action", "index")
                .increment();
        return HttpResponse.redirect(URI.create("/index.html"));
    }

    @Get("/exception")
    public Single exception() {
        throw new RuntimeException("Bad Request");
    }

    @Get("/hello/{name}")
    public Single<String> hello(@NotBlank String name) {
        meterRegistry
                .counter("web.access", "controller", "index", "action", "hello")
                .increment();
        return meterRegistry
                .timer("web.timer", "controller", "index", "action", "hello")
                .record(() -> Single.just("Hello " + personService.upperCase(name)));
    }

    @Get("/helloworld")
    public Single<String> helloworld() {
        meterRegistry
                .counter("web.access", "controller", "index", "action", "helloworld")
                .increment();
        return meterRegistry
                .timer("web.timer", "controller", "index", "action", "hello")
                .record(() -> Single.just("Hello World!"));
    }
}