package io.micronaut.examples;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.sql.SQLException;
import java.util.List;

@Controller("/books")
public class BookController {

    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Get
    public HttpResponse<List<Book>> list() throws SQLException {
        return HttpResponse.ok(bookService.list());
    }

    @Get("/all")
    public HttpResponse<List<Book>> listAll() throws SQLException {
        return HttpResponse.ok(bookService.listAll());
    }
}
