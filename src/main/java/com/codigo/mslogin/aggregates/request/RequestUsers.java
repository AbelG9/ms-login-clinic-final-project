package com.codigo.mslogin.aggregates.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RequestUsers {
    private String username;
    private String password;
}
