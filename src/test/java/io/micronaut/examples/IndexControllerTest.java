package io.micronaut.examples;

import io.micronaut.context.ApplicationContext;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IndexControllerTest {

    @Test
    public void testIndex() {
        EmbeddedServer server = ApplicationContext.run(EmbeddedServer.class);
        RxHttpClient client = server.getApplicationContext().createBean(RxHttpClient.class, server.getURL());
        assertEquals(client.toBlocking().exchange("/index.html").status(), HttpStatus.OK);
        server.stop();
    }
}
