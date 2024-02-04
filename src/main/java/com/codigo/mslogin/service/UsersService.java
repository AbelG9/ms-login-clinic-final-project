package com.codigo.mslogin.service;

import com.codigo.mslogin.aggregates.request.RequestUsers;
import com.codigo.mslogin.aggregates.response.ResponseBase;
import com.codigo.mslogin.entity.UsersEntity;

import java.util.List;

public interface UsersService {
    ResponseBase signUp(RequestUsers requestUsers);
    ResponseBase login(RequestUsers requestUsers);
    List<UsersEntity> findAllUsers();
    ResponseBase updateUser(int id, RequestUsers requestUsers);
    ResponseBase deleteUser(int id);
}
