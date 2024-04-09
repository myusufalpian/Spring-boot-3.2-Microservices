package id.mydev.auth.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestSignin {
    @NotNull(message = "username cannot be null")
    @Size(min = 4, max = 64, message = "Username must be between 4 and 64 characters")
    private String username;

    @NotNull(message = "Password cannot be null")
    @Size(min = 4, max = 64, message = "Password must be between 4 and 64 characters")
    private String password;
}
