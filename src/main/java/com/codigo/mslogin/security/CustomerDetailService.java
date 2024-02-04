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
import java.util.Optional;

@Service
public class CustomerDetailService implements UserDetailsService {
    @Autowired
    private UsersRepository usersRepository;
    @Getter
    private UsersEntity usersEntity;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UsersEntity> usersEntity1 = usersRepository.findByUsername(username);
        if(usersEntity1.isPresent())
        {
            return new User(usersEntity1.get().getUsername(), usersEntity1.get().getPassword(), new ArrayList<>());
        }
        else{
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
    }
}
