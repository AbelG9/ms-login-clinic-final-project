package com.codigo.mslogin.service.impl;

import com.codigo.mslogin.aggregates.request.RequestUsers;
import com.codigo.mslogin.aggregates.response.ResponseBase;
import com.codigo.mslogin.constants.Constants;
import com.codigo.mslogin.entity.PersonsEntity;
import com.codigo.mslogin.entity.UsersEntity;
import com.codigo.mslogin.repository.PersonsRepository;
import com.codigo.mslogin.repository.UsersRepository;
import com.codigo.mslogin.security.CustomerDetailService;
import com.codigo.mslogin.security.jwt.JwtUtil;
import com.codigo.mslogin.service.UsersService;
import com.codigo.mslogin.util.UsersValidation;
import com.codigo.mslogin.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersServiceImpl implements UsersService {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private PersonsRepository personsRepository;
    @Autowired
    private UsersValidation usersValidation;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomerDetailService customerDetailService;
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public ResponseBase signUp(RequestUsers requestUsers) {
        boolean validateUsers = usersValidation.validateInput(requestUsers, false);
        if (validateUsers) {
            UsersEntity usersEntity = getUsersEntity(requestUsers);
            try {
                usersRepository.save(usersEntity);
                return new ResponseBase(Constants.CODE_SUCCESS, Constants.MESSAGE_USER_CREATED, Optional.of(usersEntity));
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseBase(Constants.CODE_ERROR_GENERIC, Constants.MESSAGE_ERROR_USER_NOT_CREATED, Optional.empty());
            }
        } else {
            return new ResponseBase(Constants.CODE_ERROR_DATA_INPUT, Constants.MESSAGE_ERROR_DATA_NOT_VALID, Optional.empty());
        }
    }

    @Override
    public ResponseBase login(RequestUsers requestUsers) {
        try{
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(requestUsers.getUsername(),requestUsers.getPassword()));
            if (authentication.isAuthenticated()) {
                if (customerDetailService.getUsersEntity().getStatus() == Constants.STATUS_ACTIVE) {
                    String token = jwtUtil.generateToken(customerDetailService.getUsersEntity().getUsername(), customerDetailService.getUsersEntity().getRole());
                    return new ResponseBase(Constants.CODE_SUCCESS, Constants.MESSAGE_SUCCESS, Optional.of(token));
                } else {
                    return new ResponseBase(Constants.CODE_ERROR_DATA_INPUT, Constants.MESSAGE_ERROR_USER_INACTIVE, Optional.empty());
                }
            } else {
                return new ResponseBase(Constants.CODE_ERROR_DATA_INPUT, Constants.MESSAGE_ERROR_USER_NOT_VALID, Optional.empty());
            }
        } catch (Exception e) {
            return new ResponseBase(Constants.CODE_ERROR_GENERIC, Constants.MESSAGE_ERROR_USER_NOT_VALID, Optional.empty());
        }
    }

    @Override
    public List<UsersEntity> findAllUsers() {
        try {
            return usersRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ResponseBase updateUser(int id, RequestUsers requestUsers) {
        return null;
    }

    @Override
    public ResponseBase deleteUser(int id) {
        return null;
    }

    private UsersEntity getUsersEntity(RequestUsers requestUsers) {
        UsersEntity usersEntity = new UsersEntity();
        usersEntity.setUsername(requestUsers.getUsername());
        usersEntity.setPassword(requestUsers.getPassword());
        usersEntity.setRole(Constants.ROLE_USER);
        usersEntity.setStatus(Constants.STATUS_ACTIVE);
        usersEntity.setDateCreated(Util.getActualTimestamp());
        usersEntity.setUserCreated(Constants.AUDIT_ADMIN);
        usersEntity.setPersonsEntity(getPersonsEntity(requestUsers));
        return usersEntity;
    }

    private PersonsEntity getPersonsEntity(RequestUsers requestUsers) {
        return personsRepository.findPersonByEmail(requestUsers.getUsername()).orElse(null);
    }
}
