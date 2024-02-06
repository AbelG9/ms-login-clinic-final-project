package com.codigo.mslogin.util;

import com.codigo.appointmentslibrary.util.Util;
import com.codigo.mslogin.aggregates.request.RequestUsers;
import com.codigo.mslogin.repository.PersonsRepository;
import com.codigo.mslogin.repository.UsersRepository;
import org.springframework.stereotype.Component;

@Component
public class UsersValidation {
    private final UsersRepository usersRepository;
    private final PersonsRepository personsRepository;

    public UsersValidation(UsersRepository usersRepository, PersonsRepository personsRepository) {
        this.usersRepository = usersRepository;
        this.personsRepository = personsRepository;
    }

    public boolean validateInput(RequestUsers requestUsers, boolean isUpdate) {
        if (requestUsers == null) {
            return false;
        }
        if (Util.isNullOrEmpty(requestUsers.getUsername()) || Util.isNullOrEmpty(requestUsers.getPassword())) {
            return false;
        }
        if (!isUpdate) {
            if (existsUser(requestUsers.getUsername())) {
                return false;
            }
        }
        return existsPersonByEmail(requestUsers.getUsername());
    }

    public boolean existsUser(String username) {
        return usersRepository.existsByUsername(username);
    }

    public boolean existsPersonByEmail(String email) {
        return personsRepository.existsPersonByEmail(email);
    }
}
