package com.xunim.librarymanagementsystem.service;

import com.xunim.librarymanagementsystem.dto.book.BookDTO;
import com.xunim.librarymanagementsystem.dto.loan.LoanCreateDTO;
import com.xunim.librarymanagementsystem.dto.loan.LoanDTO;
import com.xunim.librarymanagementsystem.dto.user.UserDTO;
import com.xunim.librarymanagementsystem.model.Book;
import com.xunim.librarymanagementsystem.model.Loan;
import com.xunim.librarymanagementsystem.model.User;
import com.xunim.librarymanagementsystem.repository.BookRepository;
import com.xunim.librarymanagementsystem.repository.LoanRepository;
import com.xunim.librarymanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    private static final int DEFAULT_LOAN_PERIOD_DAYS = 14;
    private static final int MAX_LOANS_PER_USER = 3;

    @Transactional(readOnly = true)
    public Page<LoanDTO> findAll(Pageable pageable) {
        log.debug("Finding all loans with pagination: {}", pageable);
        return loanRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    @Transactional(readOnly = true)
    public LoanDTO findById(Long id) {
        log.debug("Finding loan by ID: {}", id);
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found with ID: " + id));
        return convertToDTO(loan);
    }

    public LoanDTO create(LoanCreateDTO createDTO) {
        log.debug("Creating new loan: {}", createDTO);

        // Find book and user
        Book book = bookRepository.findById(createDTO.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + createDTO.getBookId()));

        User user = userRepository.findById(createDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + createDTO.getUserId()));

        // Validations
        if (!book.getAvailable()) {
            throw new RuntimeException("Book is not available for loan");
        }

        if (user.getStatus() != User.UserStatus.ACTIVE) {
            throw new RuntimeException("User is not active");
        }

        List<Loan> activeLoans = loanRepository.findActiveLoansByUser(user.getId());
        if (activeLoans.size() >= MAX_LOANS_PER_USER) {
            throw new RuntimeException("User has reached the limit of " + MAX_LOANS_PER_USER + " simultaneous loans");
        }

        // Check for overdue loans
        boolean hasOverdueLoans = activeLoans.stream()
                .anyMatch(Loan::isOverdue);
        if (hasOverdueLoans) {
            throw new RuntimeException("User has overdue loans");
        }

        // Create loan
        LocalDate loanDate = LocalDate.now();
        LocalDate expectedReturnDate = loanDate.plusDays(DEFAULT_LOAN_PERIOD_DAYS);

        Loan loan = Loan.builder()
                .book(book)
                .user(user)
                .loanDate(loanDate)
                .expectedReturnDate(expectedReturnDate)
                .status(Loan.LoanStatus.ACTIVE)
                .observations(createDTO.getObservations())
                .build();

        // Update book
        book.borrowBook();
        bookRepository.save(book);

        Loan savedLoan = loanRepository.save(loan);
        log.info("Loan created successfully: {}", savedLoan.getId());
        return convertToDTO(savedLoan);
    }

    public LoanDTO returnBook(Long id) {
        log.debug("Returning loan ID: {}", id);

        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found with ID: " + id));

        if (loan.getStatus() != Loan.LoanStatus.ACTIVE) {
            throw new RuntimeException("Loan is not active");
        }

        // Update loan
        loan.setActualReturnDate(LocalDate.now());
        loan.setStatus(Loan.LoanStatus.RETURNED);

        // Calculate fine if overdue
        if (loan.getOverdueDays() > 0) {
            loan.setFineAmount(loan.calculateFine());
        }

        // Update book
        Book book = loan.getBook();
        book.returnBook();
        bookRepository.save(book);

        Loan updatedLoan = loanRepository.save(loan);
        log.info("Loan returned successfully: {}", updatedLoan.getId());
        return convertToDTO(updatedLoan);
    }

    public LoanDTO renewLoan(Long id) {
        log.debug("Renewing loan ID: {}", id);

        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found with ID: " + id));

        if (loan.getStatus() != Loan.LoanStatus.ACTIVE) {
            throw new RuntimeException("Loan is not active");
        }

        if (loan.isOverdue()) {
            throw new RuntimeException("Cannot renew overdue loan");
        }

        // Extend period by 14 more days
        loan.setExpectedReturnDate(loan.getExpectedReturnDate().plusDays(DEFAULT_LOAN_PERIOD_DAYS));
        loan.setStatus(Loan.LoanStatus.RENEWED);

        Loan renewedLoan = loanRepository.save(loan);
        log.info("Loan renewed successfully: {}", renewedLoan.getId());
        return convertToDTO(renewedLoan);
    }

    @Transactional(readOnly = true)
    public Page<LoanDTO> findByUserId(Long userId, Pageable pageable) {
        log.debug("Finding loans for user ID: {}", userId);
        return loanRepository.findByUserId(userId, pageable)
                .map(this::convertToDTO);
    }

    @Transactional(readOnly = true)
    public Page<LoanDTO> findOverdueLoans(Pageable pageable) {
        log.debug("Finding overdue loans");
        return loanRepository.findOverdueLoans(pageable)
                .map(this::convertToDTO);
    }

    @Transactional(readOnly = true)
    public Page<LoanDTO> findByStatus(Loan.LoanStatus status, Pageable pageable) {
        log.debug("Finding loans by status: {}", status);
        return loanRepository.findByStatus(status, pageable)
                .map(this::convertToDTO);
    }

    private LoanDTO convertToDTO(Loan loan) {
        BookDTO bookDTO = BookDTO.builder()
                .id(loan.getBook().getId())
                .isbn(loan.getBook().getIsbn())
                .title(loan.getBook().getTitle())
                .author(loan.getBook().getAuthor())
                .category(loan.getBook().getCategory())
                .available(loan.getBook().getAvailable())
                .build();

        UserDTO userDTO = UserDTO.builder()
                .id(loan.getUser().getId())
                .userCode(loan.getUser().getUserCode())
                .name(loan.getUser().getName())
                .email(loan.getUser().getEmail())
                .status(loan.getUser().getStatus())
                .build();

        return LoanDTO.builder()
                .id(loan.getId())
                .book(bookDTO)
                .user(userDTO)
                .loanDate(loan.getLoanDate())
                .expectedReturnDate(loan.getExpectedReturnDate())
                .actualReturnDate(loan.getActualReturnDate())
                .status(loan.getStatus())
                .fineAmount(loan.getFineAmount())
                .observations(loan.getObservations())
                .isOverdue(loan.isOverdue())
                .overdueDays(loan.getOverdueDays())
                .build();
    }
}
