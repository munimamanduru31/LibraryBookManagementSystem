
package com.library.repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.library.model.Book;

@Repository
public class BookRepository {

    private final Map<Integer, Book> books = new HashMap<>();

    public void save(Book book) {
        books.put(book.getBookId(), book);
    }

    public Book findById(int bookId) {
        return books.get(bookId);
    }

    public Collection<Book> findAll() {
        return books.values();
    }

    public boolean existsById(int bookId) {
        return books.containsKey(bookId);
    }

    public boolean deleteById(int bookId) {
        return books.remove(bookId) != null;
    }

    public int count() {
        return books.size();
    }
}
