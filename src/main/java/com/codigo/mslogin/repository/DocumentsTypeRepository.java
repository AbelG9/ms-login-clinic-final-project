package com.codigo.mslogin.repository;

import com.codigo.mslogin.entity.DocumentsTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentsTypeRepository extends JpaRepository<DocumentsTypeEntity, Integer> {
    DocumentsTypeEntity findByCode(@Param("code") String codType);
}
