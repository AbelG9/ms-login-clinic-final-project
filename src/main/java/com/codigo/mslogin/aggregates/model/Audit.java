package com.codigo.mslogin.aggregates.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.sql.Timestamp;

@Getter
@Setter
@MappedSuperclass
public class Audit {
    @Column(name="user_created", length = 45, nullable = false)
    private String userCreated;
    @Column(name="date_created", nullable = false)
    private Timestamp dateCreated;
    @Column(name="user_modified",length = 45, nullable = true)
    private String userModified;
    @Column(name="date_modified", nullable = true)
    private Timestamp dateModified;
    @Column(name="user_deleted",length = 45, nullable = true)
    private String userDeleted;
    @Column(name="date_deleted", nullable = true)
    private Timestamp dateDeleted;
}
