package com.codigo.mslogin.entity;

import com.codigo.mslogin.aggregates.model.Audit;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@NamedQuery(name = "UsersEntity.findByUsername", query = "select u from UsersEntity u where u.username=:username")
@NamedQuery(name = "UsersEntity.existsByUsername", query = "select case when count(u)> 0 then true else false end from UsersEntity u where u.username=:username")
@Entity
@Getter
@Setter
@Table(name = "users")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsersEntity extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_users")
    private int idUsers;
    @Column(name = "username", length = 100, nullable = false)
    private String username;
    @Column(name = "password", length = 250, nullable = false)
    private String password;
    @Column(name = "status", nullable = false)
    private int status;
    @Column(name = "role", nullable = false)
    private String role;
    @OneToOne
    @JoinColumn(name = "persons_id_persons", nullable = false)
    private PersonsEntity personsEntity;
}
