package com.jvrskt.bytebank.ByteBank.domain.cliente;

import java.util.Objects;

public class Cliente {
    private String nome;
    private String cpf;
    private String email;

    public Cliente(DadosCadastroCliente dados){
        this.nome = dados.nome();
        this.cpf = dados.cpf();
        this.email = dados.email();
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(cpf, cliente.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpf);
    }

    @Override
    public String toString() {
        return "Cliente " +
                "nome: " + nome +
                ", cpf: " + cpf +
                ", email: " + email +
                '}';
    }
}
