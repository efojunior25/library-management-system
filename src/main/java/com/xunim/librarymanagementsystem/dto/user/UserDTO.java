package com.xunim.librarymanagementsystem.dto.user;

import com.xunim.librarymanagementsystem.model.User.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Long id;

    @NotBlank(message = "User code is required")
    @Size(max = 20, message = "User code must have at most 20 characters")
    private String userCode;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must have at most 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must have a valid format")
    @Size(max = 100, message = "Email must have at most 100 characters")
    private String email;

    @Size(max = 20, message = "Phone must have at most 20 characters")
    private String phone;

    @Size(max = 200, message = "Address must have at most 200 characters")
    private String address;

    @Size(max = 14, message = "Document must have at most 14 characters")
    private String document;

    private UserStatus status;
    private Integer activeLoanCount;
}
