package com.xunim.librarymanagementsystem.service;

import com.xunim.librarymanagementsystem.dto.book.BookCreateDTO;
import com.xunim.librarymanagementsystem.dto.book.BookDTO;
import com.xunim.librarymanagementsystem.dto.book.BookUpdateDTO;
import com.xunim.librarymanagementsystem.model.Book;
import com.xunim.librarymanagementsystem.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    public Page<BookDTO> findAll(Pageable pageable) {
        log.debug("Finding all books with pagination: {}", pageable);
        return bookRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    @Transactional(readOnly = true)
    public BookDTO findById(Long id) {
        log.debug("Finding book by ID: {}", id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + id));
        return convertToDTO(book);
    }

    @Transactional(readOnly = true)
    public BookDTO findByIsbn(String isbn) {
        log.debug("Finding book by ISBN: {}", isbn);
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new RuntimeException("Book not found with ISBN: " + isbn));
        return convertToDTO(book);
    }

    public BookDTO create(BookCreateDTO createDTO) {
        log.debug("Creating new book: {}", createDTO);

        if (bookRepository.existsByIsbn(createDTO.getIsbn())) {
            throw new RuntimeException("Book already exists with this ISBN: " + createDTO.getIsbn());
        }

        Book book = Book.builder()
                .isbn(createDTO.getIsbn())
                .title(createDTO.getTitle())
                .author(createDTO.getAuthor())
                .category(createDTO.getCategory())
                .publicationDate(createDTO.getPublicationDate())
                .description(createDTO.getDescription())
                .pageCount(createDTO.getPageCount())
                .publisher(createDTO.getPublisher())
                .available(true)
                .timesBorrowed(0)
                .build();

        Book savedBook = bookRepository.save(book);
        log.info("Book created successfully: {}", savedBook.getId());
        return convertToDTO(savedBook);
    }

    public BookDTO update(Long id, BookUpdateDTO updateDTO) {
        log.debug("Updating book ID: {} with data: {}", id, updateDTO);

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + id));

        book.setTitle(updateDTO.getTitle());
        book.setAuthor(updateDTO.getAuthor());
        book.setCategory(updateDTO.getCategory());
        book.setPublicationDate(updateDTO.getPublicationDate());
        book.setDescription(updateDTO.getDescription());
        book.setPageCount(updateDTO.getPageCount());
        book.setPublisher(updateDTO.getPublisher());

        Book updatedBook = bookRepository.save(book);
        log.info("Book updated successfully: {}", updatedBook.getId());
        return convertToDTO(updatedBook);
    }

    public void delete(Long id) {
        log.debug("Deleting book ID: {}", id);

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + id));

        if (!book.getAvailable()) {
            throw new RuntimeException("Cannot delete a book that is currently on loan");
        }

        bookRepository.delete(book);
        log.info("Book deleted successfully: {}", id);
    }

    @Transactional(readOnly = true)
    public Page<BookDTO> searchByTerm(String term, Pageable pageable) {
        log.debug("Searching books by term: {}", term);
        return bookRepository.searchByTerm(term, pageable)
                .map(this::convertToDTO);
    }

    @Transactional(readOnly = true)
    public Page<BookDTO> findByCategory(String category, Pageable pageable) {
        log.debug("Finding books by category: {}", category);
        return bookRepository.findByCategory(category, pageable)
                .map(this::convertToDTO);
    }

    @Transactional(readOnly = true)
    public Page<BookDTO> findAvailable(Pageable pageable) {
        log.debug("Finding available books");
        return bookRepository.findByAvailable(true, pageable)
                .map(this::convertToDTO);
    }

    @Transactional(readOnly = true)
    public Page<BookDTO> findMostPopular(Pageable pageable) {
        log.debug("Finding most popular books");
        return bookRepository.findMostPopular(pageable)
                .map(this::convertToDTO);
    }

    @Transactional(readOnly = true)
    public List<String> findAllCategories() {
        return bookRepository.findAllCategories();
    }

    private BookDTO convertToDTO(Book book) {
        return BookDTO.builder()
                .id(book.getId())
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .author(book.getAuthor())
                .category(book.getCategory())
                .available(book.getAvailable())
                .timesBorrowed(book.getTimesBorrowed())
                .publicationDate(book.getPublicationDate())
                .description(book.getDescription())
                .pageCount(book.getPageCount())
                .publisher(book.getPublisher())
                .build();
    }
}
