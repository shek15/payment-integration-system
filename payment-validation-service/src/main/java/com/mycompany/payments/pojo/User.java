package com.mycompany.payments.pojo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class User {

    @NotBlank(message = "END_USER_ID_REQUIRED")
    @Size(max = 50, message = "END_USER_ID_TOO_LONG")
    private String endUserID;

    @NotBlank(message = "FIRSTNAME_REQUIRED")
    @Size(max = 50, message = "FIRSTNAME_TOO_LONG")
    private String firstname;

    @NotBlank(message = "LASTNAME_REQUIRED")
    @Size(max = 50, message = "LASTNAME_TOO_LONG")
    private String lastname;

    @NotBlank(message = "EMAIL_REQUIRED")
    @Email(message = "EMAIL_INVALID")
    @Size(max = 100, message = "EMAIL_TOO_LONG")
    private String email;

    @NotBlank(message = "MOBILE_PHONE_REQUIRED")
    @Pattern(
        regexp = "^\\+?\\d{10}$", // Character class \d for [0-9]
        message = "MOBILE_PHONE_INVALID"
    )
    private String mobilePhone;
}