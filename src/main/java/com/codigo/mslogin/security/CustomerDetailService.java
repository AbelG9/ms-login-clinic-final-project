package com.codigo.mslogin.security;

import com.codigo.mslogin.entity.UsersEntity;
import com.codigo.mslogin.repository.UsersRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomerDetailService implements UserDetailsService {
    @Autowired
    private UsersRepository usersRepository;
    @Getter
    private UsersEntity usersEntity;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        usersEntity = usersRepository.findByUsername(username).orElse(null);
        if(usersEntity != null)
        {
            return new User(usersEntity.getUsername(), usersEntity.getPassword(), new ArrayList<>());
        }
        else{
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
    }
}
