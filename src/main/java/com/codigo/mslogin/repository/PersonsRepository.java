package com.codigo.mslogin.repository;

import com.codigo.mslogin.entity.PersonsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonsRepository extends JpaRepository<PersonsEntity, Integer> {
    boolean existsByNumDocument(String numDocument);
    Optional<PersonsEntity> findByNumDocument(String numDocument);
    boolean existsPersonByEmail(String email);
    Optional<PersonsEntity> findPersonByEmail(String email);
}
