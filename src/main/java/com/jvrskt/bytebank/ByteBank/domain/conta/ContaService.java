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
    Set<Conta> contasInativas = new HashSet<>();

    public ContaService(){
       this.connection =  new ConnectionFactory();
    }

    public Set<Conta> listarContas(){
        Connection conn = connection.conectar();
        return contas = new ContaDAO(conn).listar();
    }

    public Set<Conta> contasInativadas(){
        var conn = connection.conectar();
        return contasInativas = new ContaDAO(conn).listarContasInativas();
    }

    public BigDecimal consultarSaldo(Integer numero) {
        var contaNumero = buscarContaPorNumero(numero);
        return contaNumero.getSaldo();
    }

    public void abrirConta(DadosAberturaConta dadosConta){
        Connection conn = connection.conectar();
        new ContaDAO(conn).salvar(dadosConta);


    }

    public void sacar(Integer numero, BigDecimal valor){
        var conta = buscarContaPorNumero(numero);
        if(valor.compareTo(BigDecimal.ZERO) <= 0 && conta.isContaAtiva()){
            throw new RegraDeNegocioException("Valor do saque deve ser superior a zero");
        }
        if(valor.compareTo(conta.getSaldo()) > 0){
            throw new RegraDeNegocioException("Saldo insuficiente");
        }
        if(!conta.isContaAtiva()){
            throw new RegraDeNegocioException("Conta inativa");
        }
        var conn = connection.conectar();
        new ContaDAO(conn).subtractValue(numero, valor);
    }

    public void depositar(Integer numero, BigDecimal valor){
        var conta = buscarContaPorNumero(numero);
        if(valor.compareTo(BigDecimal.ZERO) <= 0 && conta.isContaAtiva()){
            throw  new RegraDeNegocioException("Valor do deposito deve ser maior do que zero.");
        }
        if(!conta.isContaAtiva()){
            throw new RegraDeNegocioException("Conta inativa");
        }

        Connection conn = connection.conectar();
        new ContaDAO(conn).updateValue(conta.getNumero(), valor);
    }

    public void tranferencia(Conta conta1, Conta conta2, BigDecimal valor){
        if(conta1.isContaAtiva() && conta2.isContaAtiva()){
            sacar(conta1.getNumero(), valor);
            depositar(conta2.getNumero(), valor);
        }

        System.out.println("Transação realizada com sucesso!");
    }

    public void encerrarConta(Integer numero){
        var conta = buscarContaPorNumero(numero);
        if(conta.possuiSaldo()){
            throw new RegraDeNegocioException("Conta não pode ser encerrada pois possui saldo");
        }
        var conn = connection.conectar();
        new ContaDAO(conn).excluir(numero);
    }
    public void exclusaoLogica(Integer numero){
        var conta = buscarContaPorNumero(numero);
        if(conta.possuiSaldo()){
            throw new RegraDeNegocioException("Conta não pode ser encerrada pois possue saldo");
        }
        var conn = connection.conectar();
        new ContaDAO(conn).exclusaoLogica(numero);
        contasInativas.add(conta);
    }

    public Conta buscarContaPorNumero(Integer numero) {
        Connection conn = connection.conectar();
        Conta conta = new ContaDAO(conn).buscarConta(numero);
        if(conta != null) {
            return conta;
        } else {
            throw new RegraDeNegocioException("Não existe conta cadastrada com esse número!");
        }
    }
}
