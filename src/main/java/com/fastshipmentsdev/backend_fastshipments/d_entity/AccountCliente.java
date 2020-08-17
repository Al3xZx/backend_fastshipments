package com.fastshipmentsdev.backend_fastshipments.d_entity;

import javax.persistence.*;

@Entity
@Table(name = "account_cliente")
public class AccountCliente {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idAccountCliente;

    @OneToOne
    @JoinColumn(nullable = false)
    private Cliente username;

    @Column(nullable = false)
    private String password;

    public Integer getIdAccountCliente() {
        return idAccountCliente;
    }

    public void setIdAccountCliente(Integer idAccountCliente) {
        this.idAccountCliente = idAccountCliente;
    }

    public Cliente getUsername() {
        return username;
    }

    public void setUsername(Cliente username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
