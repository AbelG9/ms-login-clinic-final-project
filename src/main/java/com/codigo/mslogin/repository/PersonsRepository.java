package com.codigo.mslogin.repository;

import com.codigo.mslogin.entity.PersonsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonsRepository extends JpaRepository<PersonsEntity, Integer> {
    boolean existsByNumDocument(String numDocument);
    Optional<PersonsEntity> findByNumDocument(String numDocument);
}
