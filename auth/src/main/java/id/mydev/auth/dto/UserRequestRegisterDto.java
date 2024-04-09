package id.mydev.auth.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserRequestRegisterDto {
    @NotNull(message = "email cannot be null")
    @Email(message = "please input valid email")
    @Size(max = 100, message = "Email maximum length is 100")
    private String userEmail;

    @NotNull(message = "username cannot be null")
    @Size(min = 4, max = 64, message = "Username must be between 4 and 64 characters")
    private String userUsername;

    @NotNull(message = "password cannot be null")
    @Size(min = 4, max = 64, message = "Password must be between 4 and 64 characters")
    private String userPass;

    @NotNull(message = "confirmation password cannot be null")
    @Size(min = 4, max = 64, message = "Confirmation Password must be between 4 and 64 characters")
    private String userConfirmPass;
}
