package com.cormacx.timaoepumba.entities.user;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserDTO {

    @NotNull
    private String email;
    @NotNull
    private String password;
}
