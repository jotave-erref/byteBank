package com.jvrskt.bytebank.ByteBank.domain.conta;

import com.jvrskt.bytebank.ByteBank.ConnectionFactory;
import com.jvrskt.bytebank.ByteBank.domain.RegraDeNegocioException;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashSet;
import java.util.Set;

public class ContaService {

    private ConnectionFactory connection;

    Set<Conta> contas = new HashSet<>();

    public ContaService(){
       this.connection =  new ConnectionFactory();
    }

    public Set<Conta> listarContas(){
        Connection conn = connection.conectar();
        return new ContaDAO(conn).listar();
    }

    public BigDecimal consultarSaldo(Integer numero){
        var conta = buscarContaPorNumero(numero);
        return conta.getSaldo();
    }

    public void abrirConta(DadosAberturaConta dadosConta){
        Connection conn = connection.conectar();
        new ContaDAO(conn).salvar(dadosConta);
    }

    public void sacar(Integer numero, BigDecimal valor){
        var conta = buscarContaPorNumero(numero);
        if(valor.compareTo(BigDecimal.ZERO) <= 0){
            throw new RegraDeNegocioException("Valor do saque deve ser superior a zero");
        }
        if(valor.compareTo(conta.getSaldo()) > 0){
            throw new RegraDeNegocioException("Saldo insuficiente");
        }
        conta.sacar(valor);
    }

    public void depositar(Integer numero, BigDecimal valor){
        var conta = buscarContaPorNumero(numero);
        if(valor.compareTo(BigDecimal.ZERO) <= 0){
            throw  new RegraDeNegocioException("Valor do deposito deve ser maior do que zero.");
        }
        conta.depositar(valor);

    }

    public void encerrarConta(Integer numero){
        var conta = buscarContaPorNumero(numero);
        if(conta.possuiSaldo()){
            throw new RegraDeNegocioException("Conta não pode ser encerrada pois possui saldo");
        }
        contas.remove(conta);
    }
    public Conta buscarContaPorNumero(Integer numeroConta){
        Connection conn = connection.conectar();
        Conta conta = new ContaDAO(conn).buscarConta(numeroConta);
        if(conta != null){
            return conta;
        }else{
            throw new RegraDeNegocioException("Conta inexistente");
        }
    }
}
