package com.xunim.librarymanagementsystem.service;

import com.xunim.librarymanagementsystem.dto.user.UserCreateDTO;
import com.xunim.librarymanagementsystem.dto.user.UserDTO;
import com.xunim.librarymanagementsystem.dto.user.UserUpdateDTO;
import com.xunim.librarymanagementsystem.model.User;
import com.xunim.librarymanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<UserDTO> findAll(Pageable pageable) {
        log.debug("Finding all users with pagination: {}", pageable);
        return userRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        log.debug("Finding user by ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
        return convertToDTO(user);
    }

    @Transactional(readOnly = true)
    public UserDTO findByUserCode(String userCode) {
        log.debug("Finding user by code: {}", userCode);
        User user = userRepository.findByUserCode(userCode)
                .orElseThrow(() -> new RuntimeException("User not found with code: " + userCode));
        return convertToDTO(user);
    }

    public UserDTO create(UserCreateDTO createDTO) {
        log.debug("Creating new user: {}", createDTO);

        if (userRepository.existsByUserCode(createDTO.getUserCode())) {
            throw new RuntimeException("User already exists with this code: " + createDTO.getUserCode());
        }

        if (userRepository.existsByEmail(createDTO.getEmail())) {
            throw new RuntimeException("User already exists with this email: " + createDTO.getEmail());
        }

        User user = User.builder()
                .userCode(createDTO.getUserCode())
                .name(createDTO.getName())
                .email(createDTO.getEmail())
                .phone(createDTO.getPhone())
                .address(createDTO.getAddress())
                .document(createDTO.getDocument())
                .status(User.UserStatus.ACTIVE)
                .build();

        User savedUser = userRepository.save(user);
        log.info("User created successfully: {}", savedUser.getId());
        return convertToDTO(savedUser);
    }

    public UserDTO update(Long id, UserUpdateDTO updateDTO) {
        log.debug("Updating user ID: {} with data: {}", id, updateDTO);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        // Check if email is not being used by another user
        if (!user.getEmail().equals(updateDTO.getEmail()) &&
                userRepository.existsByEmail(updateDTO.getEmail())) {
            throw new RuntimeException("Email already being used by another user: " + updateDTO.getEmail());
        }

        user.setName(updateDTO.getName());
        user.setEmail(updateDTO.getEmail());
        user.setPhone(updateDTO.getPhone());
        user.setAddress(updateDTO.getAddress());
        user.setDocument(updateDTO.getDocument());
        if (updateDTO.getStatus() != null) {
            user.setStatus(updateDTO.getStatus());
        }

        User updatedUser = userRepository.save(user);
        log.info("User updated successfully: {}", updatedUser.getId());
        return convertToDTO(updatedUser);
    }

    public void delete(Long id) {
        log.debug("Deleting user ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        Integer activeLoans = userRepository.countActiveLoansByUserId(id);
        if (activeLoans > 0) {
            throw new RuntimeException("Cannot delete user with active loans");
        }

        userRepository.delete(user);
        log.info("User deleted successfully: {}", id);
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> searchByTerm(String term, Pageable pageable) {
        log.debug("Searching users by term: {}", term);
        return userRepository.searchByTerm(term, pageable)
                .map(this::convertToDTO);
    }

    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .userCode(user.getUserCode())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .document(user.getDocument())
                .status(user.getStatus())
                .activeLoanCount(user.getActiveLoanCount())
                .build();
    }
}
