
package com.library.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.library.model.Book;
import com.library.repository.BookRepository;

import jakarta.validation.Valid;
import jakarta.validation.Validator;

@Service
@Validated
public class BookService {

    private final BookRepository repository;
    private final Validator validator;

    public BookService(BookRepository repository, Validator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    // Add a book
    public String addBook(@Valid Book book) {
        validateBook(book);

        if (repository.existsById(book.getBookId())) {
            throw new IllegalArgumentException("Book ID already exists");
        }

        repository.save(book);
        return "Book added successfully";
    }

    // Search by ID
    public Book getBookById(int bookId) {
        Book book = repository.findById(bookId);

        if (book == null) {
            throw new NoSuchElementException("Book not found");
        }

        return book;
    }

    // Get all books
    public List<Book> getAllBooks() {
        return new ArrayList<>(repository.findAll());
    }

    // Update availability
    public String updateAvailability(int bookId, boolean available) {
        Book book = getBookById(bookId);
        book.setAvailable(available);
        repository.save(book);
        return "Availability updated successfully";
    }

    // Delete a book
    public String deleteBook(int bookId) {
        if (!repository.deleteById(bookId)) {
            throw new NoSuchElementException("Book not found");
        }

        return "Book deleted successfully";
    }

    // Search by name - Stream API
    public List<Book> searchByBookName(String name) {
        return repository.findAll().stream()
                .filter(book -> book.getBookName()
                        .toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Search by author - Stream API
    public List<Book> searchByAuthor(String author) {
        return repository.findAll().stream()
                .filter(book -> book.getAuthor()
                        .equalsIgnoreCase(author))
                .collect(Collectors.toList());
    }

    // Available books
    public List<Book> getAvailableBooks() {
        return repository.findAll().stream()
                .filter(book -> Boolean.TRUE.equals(book.getAvailable()))
                .collect(Collectors.toList());
    }

    // Sort by price
    public List<Book> sortBooksByPrice() {
        return repository.findAll().stream()
                .sorted(Comparator.comparingDouble(Book::getPrice))
                .collect(Collectors.toList());
    }

    // Sort alphabetically
    public List<Book> sortBooksByName() {
        return repository.findAll().stream()
                .sorted(Comparator.comparing(Book::getBookName,
                        String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    // Count books
    public long getTotalBookCount() {
        return repository.findAll().stream().count();
    }

    // Count available books
    public long getAvailableBookCount() {
        return getAvailableBooks().stream().count();
    }

    // Average price
    public double getAverageBookPrice() {
        return repository.findAll().stream()
                .mapToDouble(Book::getPrice)
                .average()
                .orElse(0.0);
    }

    // Most expensive book
    public Optional<Book> getMostExpensiveBook() {
        return repository.findAll().stream()
                .max(Comparator.comparingDouble(Book::getPrice));
    }

    // Books within a price range
    public List<Book> getBooksByPriceRange(double min, double max) {
        if (min < 0 || max < min) {
            throw new IllegalArgumentException("Invalid price range");
        }

        return repository.findAll().stream()
                .filter(book -> book.getPrice() >= min
                        && book.getPrice() <= max)
                .collect(Collectors.toList());
    }

    // PATCH: update only supplied fields
    public Book patchBook(int bookId, Map<String, Object> updates) {
        Book book = getBookById(bookId);

        if (updates == null || updates.isEmpty()) {
            throw new IllegalArgumentException("No updates provided");
        }

        if (updates.containsKey("bookName")) {
            book.setBookName((String) updates.get("bookName"));
        }

        if (updates.containsKey("author")) {
            book.setAuthor((String) updates.get("author"));
        }

        if (updates.containsKey("price")) {
            Object value = updates.get("price");

            if (!(value instanceof Number)) {
                throw new IllegalArgumentException("Price must be numeric");
            }

            book.setPrice(((Number) value).doubleValue());
        }

        if (updates.containsKey("available")) {
            Object value = updates.get("available");

            if (!(value instanceof Boolean)) {
                throw new IllegalArgumentException(
                        "Availability must be true or false");
            }

            book.setAvailable((Boolean) value);
        }

        validateBook(book);
        repository.save(book);
        return book;
    }

    // Validate the complete book
    private void validateBook(Book book) {
        var violations = validator.validate(book);

        if (!violations.isEmpty()) {
            String message = violations.stream()
                    .map(v -> v.getMessage())
                    .collect(Collectors.joining(", "));

            throw new IllegalArgumentException(message);
        }
    }
}
