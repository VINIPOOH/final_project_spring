package ua.testing.authorization.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationInfoDto {

    @Email()
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
    @NotEmpty
    private String passwordRepeat;
}