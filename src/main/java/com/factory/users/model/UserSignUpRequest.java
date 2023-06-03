package com.factory.users.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserSignUpRequest {

    private String name;

    @Email(message = "Formato de 'email' no valido")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    @NotBlank(message = "Campo 'email' no debe estar vacío")
    private String email;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d.*\\d){1,2}(?=.*[a-z])[a-zA-Z0-9]{8,12}$")
    @Length(min = 8, max = 12, message = "Largo mínimo 8, Largo máximo 12")
    @NotBlank(message = "Campo 'password' no debe estar vacío")
    private String password;

    private List<PhoneReq> phones;
}
