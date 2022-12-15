package com.cormacx.timaoepumba.entities.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserDTO {

    @NotNull
    private String email;
    @NotNull
    private String password;
}
