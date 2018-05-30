package io.micronaut.examples;

import io.micrometer.core.instrument.MeterRegistry;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.validation.Validated;
import io.reactivex.Single;

import javax.validation.constraints.NotBlank;
import java.net.URI;

@Controller("/")
@Validated
public class IndexController {

    private MeterRegistry meterRegistry;

    public IndexController(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Produces(MediaType.TEXT_HTML)
    @Get(uri = "/")
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
    public Single hello(@NotBlank String name) {
        meterRegistry
                .counter("web.access", "controller", "index", "action", "hello")
                .increment();
        return meterRegistry
                .timer("web.timer", "controller", "index", "action", "hello")
                .record(() -> Single.just("Hello " + name));
    }

    @Get("/helloworld")
    public Single helloworld() {
        meterRegistry
                .counter("web.access", "controller", "index", "action", "helloworld")
                .increment();
        return meterRegistry
                .timer("web.timer", "controller", "index", "action", "hello")
                .record(() -> Single.just("Hello World!"));
    }
}