package com.xunim.librarymanagementsystem.dto.user;

import com.xunim.librarymanagementsystem.model.User.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must have a valid format")
    private String email;

    private String phone;
    private String address;
    private String document;
    private UserStatus status;
}
