package com.xunim.librarymanagementsystem.config;

import com.xunim.librarymanagementsystem.dto.book.BookCreateDTO;
import com.xunim.librarymanagementsystem.dto.user.UserCreateDTO;
import com.xunim.librarymanagementsystem.service.BookService;
import com.xunim.librarymanagementsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final BookService bookService;
    private final UserService userService;

    @Override
    public void run(String... args) throws Exception {
        initializeSampleData();
    }

    private void initializeSampleData() {
        try {
            // Initialize sample books
            initializeBooks();

            // Initialize sample users
            initializeUsers();

            log.info("Sample data initialized successfully");
        } catch (Exception e) {
            log.warn("Error initializing sample data: {}", e.getMessage());
        }
    }

    private void initializeBooks() {
        BookCreateDTO[] books = {
                BookCreateDTO.builder()
                        .isbn("978-0132350884")
                        .title("Clean Code: A Handbook of Agile Software Craftsmanship")
                        .author("Robert C. Martin")
                        .category("Programming")
                        .publicationDate(LocalDate.of(2008, 8, 11))
                        .description("A comprehensive guide to writing clean, readable, and maintainable code.")
                        .pageCount(464)
                        .publisher("Prentice Hall")
                        .build(),

                BookCreateDTO.builder()
                        .isbn("978-0201616224")
                        .title("The Pragmatic Programmer")
                        .author("Andrew Hunt, David Thomas")
                        .category("Programming")
                        .publicationDate(LocalDate.of(1999, 10, 20))
                        .description("Your journey to mastery in software development.")
                        .pageCount(352)
                        .publisher("Addison-Wesley")
                        .build(),

                BookCreateDTO.builder()
                        .isbn("978-0134685991")
                        .title("Effective Java")
                        .author("Joshua Bloch")
                        .category("Java")
                        .publicationDate(LocalDate.of(2017, 12, 27))
                        .description("The definitive guide to Java programming best practices.")
                        .pageCount(412)
                        .publisher("Addison-Wesley")
                        .build(),

                BookCreateDTO.builder()
                        .isbn("978-0596009205")
                        .title("Head First Design Patterns")
                        .author("Eric Freeman, Elisabeth Robson")
                        .category("Design Patterns")
                        .publicationDate(LocalDate.of(2004, 10, 25))
                        .description("A brain-friendly guide to design patterns.")
                        .pageCount(694)
                        .publisher("O'Reilly Media")
                        .build(),

                BookCreateDTO.builder()
                        .isbn("978-1449344672")
                        .title("Spring in Action")
                        .author("Craig Walls")
                        .category("Spring Framework")
                        .publicationDate(LocalDate.of(2018, 10, 2))
                        .description("Comprehensive guide to Spring Framework development.")
                        .pageCount(520)
                        .publisher("Manning Publications")
                        .build()
        };

        for (BookCreateDTO book : books) {
            try {
                bookService.create(book);
                log.debug("Created sample book: {}", book.getTitle());
            } catch (Exception e) {
                log.debug("Book already exists: {}", book.getTitle());
            }
        }
    }

    private void initializeUsers() {
        UserCreateDTO[] users = {
                UserCreateDTO.builder()
                        .userCode("USR001")
                        .name("John Smith")
                        .email("john.smith@email.com")
                        .phone("(11) 99999-9999")
                        .address("123 Main Street, City")
                        .document("123.456.789-01")
                        .build(),

                UserCreateDTO.builder()
                        .userCode("USR002")
                        .name("Mary Johnson")
                        .email("mary.johnson@email.com")
                        .phone("(11) 88888-8888")
                        .address("456 Oak Avenue, City")
                        .document("987.654.321-02")
                        .build(),

                UserCreateDTO.builder()
                        .userCode("USR003")
                        .name("Robert Brown")
                        .email("robert.brown@email.com")
                        .phone("(11) 77777-7777")
                        .address("789 Pine Road, City")
                        .document("456.789.123-03")
                        .build(),

                UserCreateDTO.builder()
                        .userCode("USR004")
                        .name("Sarah Davis")
                        .email("sarah.davis@email.com")
                        .phone("(11) 66666-6666")
                        .address("321 Elm Street, City")
                        .document("789.123.456-04")
                        .build(),

                UserCreateDTO.builder()
                        .userCode("USR005")
                        .name("Michael Wilson")
                        .email("michael.wilson@email.com")
                        .phone("(11) 55555-5555")
                        .address("654 Maple Drive, City")
                        .document("159.753.486-05")
                        .build()
        };

        for (UserCreateDTO user : users) {
            try {
                userService.create(user);
                log.debug("Created sample user: {}", user.getName());
            } catch (Exception e) {
                log.debug("User already exists: {}", user.getName());
            }
        }
    }
}
