package ua.testing.authorization.dto;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class RegistrationInfoDto {

    @Email()
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
    @NotEmpty
    private String passwordRepeat;
}