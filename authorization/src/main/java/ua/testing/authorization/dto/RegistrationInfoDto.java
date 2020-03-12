package ua.testing.authorization.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Data
public class RegistrationInfoDto {

    @Email()
    private String username;
    
    private String password;
    private String passwordRepeat;
}