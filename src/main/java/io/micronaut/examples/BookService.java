/*
 * Copyright 2017-2018 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.micronaut.examples;

import io.micronaut.spring.tx.annotation.Transactional;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Simple class, don't ever copy this for actual code.  #hacked
 */
@Singleton
public class BookService {

    private final DataSource dataSourceDefault;
    private final DataSource dataSourceSecondary;

    public BookService(DataSource dataSourceDefault, @Named("secondary") DataSource dataSourceSecondary) throws SQLException {
        this.dataSourceDefault = dataSourceDefault;
        this.dataSourceSecondary = dataSourceSecondary;

        try (Connection connection = dataSourceDefault.getConnection()) {
            connection.createStatement().execute("CREATE TABLE IF NOT EXISTS book(id INTEGER AUTO_INCREMENT, title VARCHAR(100));");
            connection.createStatement().execute("INSERT INTO book(title) VALUES ('Groovy in Action');");
            connection.commit();
        }

        try (Connection connection = dataSourceSecondary.getConnection()) {
            connection.createStatement().execute("CREATE TABLE IF NOT EXISTS book(id INTEGER AUTO_INCREMENT, title VARCHAR(100));");
            connection.createStatement().execute("INSERT INTO book(title) VALUES ('Java in Action');");
            connection.commit();
        }
    }

    @Transactional
    public List<Book> list() throws SQLException {
        return list(dataSourceDefault);
    }

    @Transactional
    public List<Book> listTwo() throws SQLException {
        return list(dataSourceDefault);
    }

    @Transactional
    public Book create(Book book) throws SQLException {
        return create(dataSourceDefault, book);
    }

    @Transactional
    public Book createTwo(Book book) throws SQLException {
        return create(dataSourceDefault, book);
    }


    @Transactional("dataSourceSecondary")
    public Book updateTwo(Book book) throws SQLException {
        return update(dataSourceSecondary, book);
    }

    private List<Book> list(DataSource secondary) throws SQLException {
        List<Book> books = new ArrayList<>();
        try (Connection connection = secondary.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, title from book");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                books.add(new Book(rs.getLong(1), rs.getString(2)));
            }
        }
        return books;
    }

    private Book create(DataSource secondary, Book book) throws SQLException {
        try (Connection connection = secondary.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO book(title) values(?)");
            preparedStatement.setString(1, book.getTitle());
            return preparedStatement.executeQuery().getObject(1, Book.class);
        }
    }

    private Book update(DataSource secondary, Book book) throws SQLException {
        try (Connection connection = secondary.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE book SET title = ? where id = ?;");
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setLong(2, book.getId());
            preparedStatement.executeUpdate();

            PreparedStatement selectStatement = connection.prepareStatement("SELECT id, title FROM book where id = ?");
            selectStatement.setLong(1, book.getId());
            return selectStatement.executeQuery().getObject(1, Book.class);
        }
    }

    public List<Book> listAll() throws SQLException {
        return Stream.of(list(dataSourceDefault), list(dataSourceSecondary)).flatMap(Collection::stream).collect(Collectors.toList());
    }
}