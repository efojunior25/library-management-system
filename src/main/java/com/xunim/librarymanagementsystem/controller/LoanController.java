package com.xunim.librarymanagementsystem.controller;

import com.xunim.librarymanagementsystem.dto.loan.LoanCreateDTO;
import com.xunim.librarymanagementsystem.dto.loan.LoanDTO;
import com.xunim.librarymanagementsystem.model.Loan.LoanStatus;
import com.xunim.librarymanagementsystem.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class LoanController {

    private final LoanService loanService;

    @GetMapping
    public ResponseEntity<Page<LoanDTO>> getAllLoans(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "loanDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<LoanDTO> loans = loanService.findAll(pageable);

        return ResponseEntity.ok(loans);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanDTO> getLoanById(@PathVariable Long id) {
        log.debug("GET /api/loans/{}", id);
        LoanDTO loan = loanService.findById(id);
        return ResponseEntity.ok(loan);
    }

    @PostMapping
    public ResponseEntity<LoanDTO> createLoan(@Valid @RequestBody LoanCreateDTO createDTO) {
        log.debug("POST /api/loans - {}", createDTO);
        LoanDTO newLoan = loanService.create(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newLoan);
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<LoanDTO> returnBook(@PathVariable Long id) {
        log.debug("PUT /api/loans/{}/return", id);
        LoanDTO returnedLoan = loanService.returnBook(id);
        return ResponseEntity.ok(returnedLoan);
    }

    @PutMapping("/{id}/renew")
    public ResponseEntity<LoanDTO> renewLoan(@PathVariable Long id) {
        log.debug("PUT /api/loans/{}/renew", id);
        LoanDTO renewedLoan = loanService.renewLoan(id);
        return ResponseEntity.ok(renewedLoan);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<LoanDTO>> getLoansByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("loanDate").descending());
        Page<LoanDTO> loans = loanService.findByUserId(userId, pageable);
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/overdue")
    public ResponseEntity<Page<LoanDTO>> getOverdueLoans(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("expectedReturnDate").ascending());
        Page<LoanDTO> overdueLoans = loanService.findOverdueLoans(pageable);
        return ResponseEntity.ok(overdueLoans);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<LoanDTO>> getLoansByStatus(
            @PathVariable LoanStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("loanDate").descending());
        Page<LoanDTO> loans = loanService.findByStatus(status, pageable);
        return ResponseEntity.ok(loans);
    }
}