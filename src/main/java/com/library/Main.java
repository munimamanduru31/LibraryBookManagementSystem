package com.library;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.library.config.AppConfig;
import com.library.model.Book;
import com.library.service.BookService;

public class Main {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);

        BookService service = context.getBean(BookService.class);
        Scanner sc = new Scanner(System.in);

        try {
            boolean running = true;

            while (running) {
                System.out.println("\n===== LIBRARY BOOK MANAGEMENT =====");
                System.out.println("1. Add Book");
                System.out.println("2. View All Books");
                System.out.println("3. Search Book by ID");
                System.out.println("4. Search Book by Name");
                System.out.println("5. Search Book by Author");
                System.out.println("6. View Available Books");
                System.out.println("7. Sort Books by Price");
                System.out.println("8. Update Book Price (PATCH)");
                System.out.println("9. Update Book Availability");
                System.out.println("10. Delete Book");
                System.out.println("11. Show Book Statistics");
                System.out.println("12. Exit");
                System.out.print("Enter your choice: ");

                int choice;

                try {
                    choice = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid menu number.");
                    continue;
                }

                try {
                    switch (choice) {

                        case 1: {
                            System.out.print("Enter Book ID: ");
                            int id = Integer.parseInt(sc.nextLine());

                            System.out.print("Enter Book Name: ");
                            String name = sc.nextLine();

                            System.out.print("Enter Author Name: ");
                            String author = sc.nextLine();

                            System.out.print("Enter Book Price: ");
                            double price = Double.parseDouble(sc.nextLine());

                            System.out.print("Is the book available? (true/false): ");
                            String answer = sc.nextLine();

                            if (!answer.equalsIgnoreCase("true")
                                    && !answer.equalsIgnoreCase("false")) {
                                System.out.println("Enter true or false.");
                                break;
                            }

                            boolean available = Boolean.parseBoolean(answer);

                            Book book = new Book(id, name, author, price, available);
                            System.out.println(service.addBook(book));
                            break;
                        }

                        case 2: {
                            List<Book> books = service.getAllBooks();

                            if (books.isEmpty()) {
                                System.out.println("No books found.");
                            } else {
                                books.forEach(System.out::println);
                            }
                            break;
                        }

                        case 3: {
                            System.out.print("Enter Book ID: ");
                            int id = Integer.parseInt(sc.nextLine());
                            System.out.println(service.getBookById(id));
                            break;
                        }

                        case 4: {
                            System.out.print("Enter Book Name to search: ");
                            String name = sc.nextLine();

                            service.searchByBookName(name)
                                    .forEach(System.out::println);
                            break;
                        }

                        case 5: {
                            System.out.print("Enter Author Name: ");
                            String author = sc.nextLine();

                            service.searchByAuthor(author)
                                    .forEach(System.out::println);
                            break;
                        }

                        case 6: {
                            service.getAvailableBooks()
                                    .forEach(System.out::println);
                            break;
                        }

                        case 7: {
                            service.sortBooksByPrice()
                                    .forEach(System.out::println);
                            break;
                        }

                        case 8: {
                            System.out.print("Enter Book ID: ");
                            int id = Integer.parseInt(sc.nextLine());

                            System.out.print("Enter new price: ");
                            double price = Double.parseDouble(sc.nextLine());

                            service.patchBook(id, Map.of("price", price));
                            System.out.println("Book price updated successfully.");
                            System.out.println(service.getBookById(id));
                            break;
                        }

                        case 9: {
                            System.out.print("Enter Book ID: ");
                            int id = Integer.parseInt(sc.nextLine());

                            System.out.print("Set availability (true/false): ");
                            String answer = sc.nextLine();

                            if (!answer.equalsIgnoreCase("true")
                                    && !answer.equalsIgnoreCase("false")) {
                                System.out.println("Enter true or false.");
                                break;
                            }

                            boolean available = Boolean.parseBoolean(answer);
                            service.updateAvailability(id, available);

                            System.out.println("Book availability updated.");
                            System.out.println(service.getBookById(id));
                            break;
                        }

                        case 10: {
                            System.out.print("Enter Book ID to delete: ");
                            int id = Integer.parseInt(sc.nextLine());

                            service.deleteBook(id);
                            System.out.println("Book deleted successfully.");
                            break;
                        }

                        case 11: {
                            System.out.println("Total books: "
                                    + service.getTotalBookCount());

                            System.out.println("Available books: "
                                    + service.getAvailableBookCount());

                            System.out.println("Average book price: "
                                    + service.getAverageBookPrice());

                            System.out.println("Most expensive book: "
                                    + service.getMostExpensiveBook());

                            break;
                        }

                        case 12:
                            running = false;
                            System.out.println("Exiting Library Book Management.");
                            break;

                        default:
                            System.out.println("Invalid choice. Try again.");
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Invalid number. Please try again.");
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Operation failed: " + e.getMessage());
                }
            }

        } finally {
            sc.close();
            context.close();
        }
    }
}
