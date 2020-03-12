package ua.testing.authorization.dto;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class RegistrationInfoDto {

    @Pattern(regexp = "^[A-Z][a-z']{1,20}$", message = "incorrectLoginInput")
    private String username;
    private String password;
    private String passwordRepeat;
}