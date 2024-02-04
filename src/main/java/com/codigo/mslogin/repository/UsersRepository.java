package com.codigo.mslogin.repository;

import com.codigo.mslogin.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<UsersEntity, Integer> {
    boolean existsByUsername(String username);
    Optional<UsersEntity> findByUsername(String username);
}
