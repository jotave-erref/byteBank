package com.jvrskt.bytebank.ByteBank.domain.conta;

import com.jvrskt.bytebank.ByteBank.domain.cliente.Cliente;

import java.math.BigDecimal;
import java.util.Objects;

public class Conta {

    private Integer numero;
    private BigDecimal saldo;
    private Cliente titular;

    private boolean contaAtiva;


    public Conta(Integer numero, BigDecimal saldo, Cliente titular, boolean contaAtiva){
        this.numero = numero;
        this.saldo = saldo;
        this.titular = titular;
        this.contaAtiva = contaAtiva;
    }
    public boolean possuiSaldo(){
        return this.saldo.compareTo(BigDecimal.ZERO) != 0;
    }
    public Integer getNumero() {
        return numero;
    }
    public BigDecimal getSaldo() {
        return saldo;
    }

    public boolean isContaAtiva() {
        return contaAtiva;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conta conta = (Conta) o;
        return Objects.equals(numero, conta.numero);
    }
    @Override
    public int hashCode() {
        return Objects.hash(numero);
    }

    @Override
    public String toString() {
        return "{ " +
                "numero " + numero +
                ", saldo " + saldo +
                ", titular " + titular +
                "}";
    }
}