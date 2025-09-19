package com.xunim.librarymanagementsystem.controller;

import com.xunim.librarymanagementsystem.dto.book.BookCreateDTO;
import com.xunim.librarymanagementsystem.dto.book.BookDTO;
import com.xunim.librarymanagementsystem.dto.book.BookUpdateDTO;
import com.xunim.librarymanagementsystem.dto.loan.LoanCreateDTO;
import com.xunim.librarymanagementsystem.dto.loan.LoanDTO;
import com.xunim.librarymanagementsystem.dto.user.UserCreateDTO;
import com.xunim.librarymanagementsystem.dto.user.UserDTO;
import com.xunim.librarymanagementsystem.model.Loan.LoanStatus;
import com.xunim.librarymanagementsystem.service.BookService;
import com.xunim.librarymanagementsystem.service.LoanService;
import com.xunim.librarymanagementsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebController {

    private final BookService bookService;
    private final UserService userService;
    private final LoanService loanService;

    // ============= MAIN PAGES =============

    @GetMapping("/")
    public String dashboard(Model model) {
        // Dashboard with basic statistics
        Pageable pageable = PageRequest.of(0, 5);

        Page<BookDTO> popularBooks = bookService.findMostPopular(pageable);
        Page<LoanDTO> recentLoans = loanService.findAll(
                PageRequest.of(0, 5, Sort.by("loanDate").descending())
        );
        Page<LoanDTO> overdueLoans = loanService.findOverdueLoans(pageable);

        model.addAttribute("popularBooks", popularBooks);
        model.addAttribute("recentLoans", recentLoans);
        model.addAttribute("overdueLoans", overdueLoans);

        return "dashboard";
    }

    // ============= BOOKS =============

    @GetMapping("/books")
    public String listBooks(Model model,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            @RequestParam(required = false) String search,
                            @RequestParam(required = false) String category) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("title"));
        Page<BookDTO> books;

        if (search != null && !search.trim().isEmpty()) {
            books = bookService.searchByTerm(search, pageable);
            model.addAttribute("search", search);
        } else if (category != null && !category.trim().isEmpty()) {
            books = bookService.findByCategory(category, pageable);
            model.addAttribute("category", category);
        } else {
            books = bookService.findAll(pageable);
        }

        model.addAttribute("books", books);
        model.addAttribute("categories", bookService.findAllCategories());

        return "books/list";
    }

    @GetMapping("/books/new")
    public String showNewBookForm(Model model) {
        model.addAttribute("book", new BookCreateDTO());
        model.addAttribute("categories", bookService.findAllCategories());
        return "books/form";
    }

    @PostMapping("/books")
    public String createBook(@Valid @ModelAttribute("book") BookCreateDTO bookDTO,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", bookService.findAllCategories());
            return "books/form";
        }

        try {
            bookService.create(bookDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Book created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/books";
    }

    @GetMapping("/books/{id}/edit")
    public String showEditBookForm(@PathVariable Long id, Model model) {
        try {
            BookDTO book = bookService.findById(id);
            BookUpdateDTO updateDTO = BookUpdateDTO.builder()
                    .title(book.getTitle())
                    .author(book.getAuthor())
                    .category(book.getCategory())
                    .publicationDate(book.getPublicationDate())
                    .description(book.getDescription())
                    .pageCount(book.getPageCount())
                    .publisher(book.getPublisher())
                    .build();

            model.addAttribute("bookId", id);
            model.addAttribute("book", updateDTO);
            model.addAttribute("categories", bookService.findAllCategories());
            return "books/edit";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/books";
        }
    }

    @PostMapping("/books/{id}")
    public String updateBook(@PathVariable Long id,
                             @Valid @ModelAttribute("book") BookUpdateDTO bookDTO,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("bookId", id);
            model.addAttribute("categories", bookService.findAllCategories());
            return "books/edit";
        }

        try {
            bookService.update(id, bookDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Book updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/books";
    }

    @PostMapping("/books/{id}/delete")
    public String deleteBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bookService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Book deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/books";
    }

    // ============= USERS =============

    @GetMapping("/users")
    public String listUsers(Model model,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            @RequestParam(required = false) String search) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("name"));
        Page<UserDTO> users;

        if (search != null && !search.trim().isEmpty()) {
            users = userService.searchByTerm(search, pageable);
            model.addAttribute("search", search);
        } else {
            users = userService.findAll(pageable);
        }

        model.addAttribute("users", users);
        return "users/list";
    }

    @GetMapping("/users/new")
    public String showNewUserForm(Model model) {
        model.addAttribute("user", new UserCreateDTO());
        return "users/form";
    }

    @PostMapping("/users")
    public String createUser(@Valid @ModelAttribute("user") UserCreateDTO userDTO,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "users/form";
        }

        try {
            userService.create(userDTO);
            redirectAttributes.addFlashAttribute("successMessage", "User created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/users";
    }

    // ============= LOANS =============

    @GetMapping("/loans")
    public String listLoans(Model model,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            @RequestParam(required = false) String status) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("loanDate").descending());
        Page<LoanDTO> loans;

        if (status != null && !status.trim().isEmpty()) {
            try {
                loans = loanService.findByStatus(LoanStatus.valueOf(status.toUpperCase()), pageable);
                model.addAttribute("status", status);
            } catch (IllegalArgumentException e) {
                loans = loanService.findAll(pageable);
            }
        } else {
            loans = loanService.findAll(pageable);
        }

        model.addAttribute("loans", loans);
        return "loans/list";
    }

    @GetMapping("/loans/new")
    public String showNewLoanForm(Model model) {
        model.addAttribute("loan", new LoanCreateDTO());
        return "loans/form";
    }

    @PostMapping("/loans")
    public String createLoan(@Valid @ModelAttribute("loan") LoanCreateDTO loanDTO,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "loans/form";
        }

        try {
            loanService.create(loanDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Loan created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/loans";
    }

    @PostMapping("/loans/{id}/return")
    public String returnBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            loanService.returnBook(id);
            redirectAttributes.addFlashAttribute("successMessage", "Book returned successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/loans";
    }

    @PostMapping("/loans/{id}/renew")
    public String renewLoan(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            loanService.renewLoan(id);
            redirectAttributes.addFlashAttribute("successMessage", "Loan renewed successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/loans";
    }

    // ============= REPORTS =============

    @GetMapping("/reports")
    public String showReports(Model model) {
        Pageable topFive = PageRequest.of(0, 5);

        Page<BookDTO> popularBooks = bookService.findMostPopular(topFive);
        Page<LoanDTO> overdueLoans = loanService.findOverdueLoans(topFive);
        Page<BookDTO> availableBooks = bookService.findAvailable(topFive);

        model.addAttribute("popularBooks", popularBooks);
        model.addAttribute("overdueLoans", overdueLoans);
        model.addAttribute("availableBooks", availableBooks);

        return "reports/dashboard";
    }
}
