package com.jvrskt.bytebank.ByteBank.domain.conta;

import com.jvrskt.bytebank.ByteBank.domain.cliente.Cliente;
import com.jvrskt.bytebank.ByteBank.domain.cliente.DadosCadastroCliente;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ContaDAO {

    Connection conn;
    ContaDAO(Connection connection){
        this.conn = connection;
    }

    public void salvar(DadosAberturaConta dadosConta){
        var cliente = new Cliente(dadosConta.cliente());
        var conta = new Conta(dadosConta.numero(), BigDecimal.ZERO, cliente);
        String sql = "INSERT INTO conta (numero, saldo, cliente_nome, cliente_cpf, cliente_email)" +
                "VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, conta.getNumero());
            ps.setBigDecimal(2, BigDecimal.ZERO);
            ps.setString(3, cliente.getNome());
            ps.setString(4, cliente.getCpf());
            ps.setString(5, cliente.getEmail());
            ps.execute();
            ps.close();
            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Set<Conta> listar(){
        PreparedStatement ps;
        ResultSet rs;
        String sql = "SELECT * FROM conta;";
        Set<Conta> contas = new HashSet<>();

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                Integer numero = rs.getInt(1);
                BigDecimal saldo = rs.getBigDecimal(2);
                String nome = rs.getString(3);
                String cpf = rs.getString(4);
                String email = rs.getString(5);

                var dadosCliente = new DadosCadastroCliente(nome, cpf, email);
                var cliente = new Cliente(dadosCliente);
                Conta conta = new Conta(numero, saldo, cliente);
                contas.add(conta);
            }
            ps.close();
            rs.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return contas;
    }

    public Conta buscarConta(Integer numeroConta){
        String sql = "SELECT * FROM conta WHERE numero = ?;";
        PreparedStatement ps;
        ResultSet rs;
        Conta conta = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, numeroConta);
            rs = ps.executeQuery();
            while(rs.next()){
                Integer numeroRecuperado = rs.getInt(1);
                BigDecimal saldo = rs.getBigDecimal(2);
                String nome = rs.getString(3);
                String cpf = rs.getString(4);
                String email = rs.getString(5);

                var dadosCliente = new DadosCadastroCliente(nome, cpf, email);
                var cliente = new Cliente(dadosCliente);
                conta = new Conta(numeroRecuperado, saldo, cliente);
            }
            rs.close();
            ps.close();
            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return conta;
    }

}
