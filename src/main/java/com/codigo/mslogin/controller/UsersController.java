package com.codigo.mslogin.controller;

import com.codigo.appointmentslibrary.response.ResponseBase;
import com.codigo.mslogin.aggregates.request.RequestUsers;
import com.codigo.mslogin.entity.UsersEntity;
import com.codigo.mslogin.service.UsersService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/v1/users")
public class UsersController {
    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/signUp")
    public ResponseBase registerUser(@RequestBody RequestUsers requestUsers) {
        return usersService.signUp(requestUsers);
    }

    @PostMapping("/login")
    public ResponseBase login(@RequestBody RequestUsers requestUsers) {
        return usersService.login(requestUsers);
    }

    @GetMapping
    public List<UsersEntity> findAllUsers() {
       return usersService.findAllUsers();
    }
}
