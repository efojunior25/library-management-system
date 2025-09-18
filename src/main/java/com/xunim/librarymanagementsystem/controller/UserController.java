package com.xunim.librarymanagementsystem.controller;

import com.xunim.librarymanagementsystem.dto.user.UserCreateDTO;
import com.xunim.librarymanagementsystem.dto.user.UserDTO;
import com.xunim.librarymanagementsystem.dto.user.UserUpdateDTO;
import com.xunim.librarymanagementsystem.service.UserService;
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
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UserDTO> users = userService.findAll(pageable);

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        log.debug("GET /api/users/{}", id);
        UserDTO user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/code/{userCode}")
    public ResponseEntity<UserDTO> getUserByCode(@PathVariable String userCode) {
        log.debug("GET /api/users/code/{}", userCode);
        UserDTO user = userService.findByUserCode(userCode);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserCreateDTO createDTO) {
        log.debug("POST /api/users - {}", createDTO);
        UserDTO newUser = userService.create(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id,
                                              @Valid @RequestBody UserUpdateDTO updateDTO) {
        log.debug("PUT /api/users/{} - {}", id, updateDTO);
        UserDTO updatedUser = userService.update(id, updateDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.debug("DELETE /api/users/{}", id);
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<UserDTO>> searchUsers(
            @RequestParam String term,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<UserDTO> users = userService.searchByTerm(term, pageable);
        return ResponseEntity.ok(users);
    }
}
