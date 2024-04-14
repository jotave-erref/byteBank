package com.jvrskt.bytebank.ByteBank.domain.conta;

import com.jvrskt.bytebank.ByteBank.domain.RegraDeNegocioException;
import com.jvrskt.bytebank.ByteBank.domain.cliente.Cliente;
import com.jvrskt.bytebank.ByteBank.domain.cliente.DadosCadastroCliente;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
//Classe responsável por encapsular o acesso e a manipulação dos dados do banco de dados
public class ContaDAO {

    Connection conn;
    ContaDAO(Connection connection){
        this.conn = connection;
    }

    public void salvar(DadosAberturaConta dadosConta){
        var cliente = new Cliente(dadosConta.cliente());
        var conta = new Conta(dadosConta.numero(), BigDecimal.ZERO, cliente, true);
        String sql = "INSERT INTO conta (numero, saldo, cliente_nome, cliente_cpf, cliente_email, conta_ativa)" +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try {
            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, conta.getNumero());
            ps.setBigDecimal(2, BigDecimal.ZERO);
            ps.setString(3, cliente.getNome());
            ps.setString(4, cliente.getCpf());
            ps.setString(5, cliente.getEmail());
            ps.setBoolean(6, true);
            ps.execute();
            conn.commit();
            ps.close();
            conn.close();

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    public Set<Conta> listar(){
        PreparedStatement ps;
        ResultSet rs;
        String sql = "SELECT * FROM conta where conta_ativa = true";
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
                Boolean estaAtiva = rs.getBoolean(6);

                var dadosCliente = new DadosCadastroCliente(nome, cpf, email);
                var cliente = new Cliente(dadosCliente);
                Conta conta = new Conta(numero, saldo, cliente, estaAtiva);
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

    public Set<Conta> listarContasInativas(){
        PreparedStatement ps;
        ResultSet rs;
        String sql = "SELECT * FROM conta where conta_ativa = 0";
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
                boolean estaAtiva = rs.getBoolean(6);

                var dadosCliente = new DadosCadastroCliente(nome, cpf, email);
                var cliente = new Cliente(dadosCliente);
                Conta conta = new Conta(numero, saldo, cliente, estaAtiva);
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

    public Conta buscarConta(Integer numero) {
        String sql = "SELECT * FROM conta WHERE numero = ? and conta_ativa = true";

        PreparedStatement ps;
        ResultSet resultSet;
        Conta conta = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, numero);
            resultSet = ps.executeQuery();

            while (resultSet.next()) {
                Integer numeroRecuperado = resultSet.getInt(1);
                BigDecimal saldo = resultSet.getBigDecimal(2);
                String nome = resultSet.getString(3);
                String cpf = resultSet.getString(4);
                String email = resultSet.getString(5);
                Boolean estaAtiva = resultSet.getBoolean(6);

                DadosCadastroCliente dadosCadastroCliente =
                        new DadosCadastroCliente(nome, cpf, email);
                Cliente cliente = new Cliente(dadosCadastroCliente);

                conta = new Conta(numeroRecuperado, saldo, cliente, estaAtiva);
            }
            resultSet.close();
            ps.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return conta;
    }

    public void updateValue(Integer numero, BigDecimal saldo){
        String sql = "Update conta set saldo = saldo + ? where numero = ? and conta_ativa = true";
        PreparedStatement ps;
        try{
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(sql);
            ps.setBigDecimal(1, saldo);
            ps.setInt(2, numero);
            ps.execute();
            conn.commit();
            ps.close();
            conn.close();
        }catch (SQLException e){
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }

    }

    public void subtractValue(Integer numero, BigDecimal saldo){
        String sql = "update conta set saldo = saldo - ? where numero = ? and conta_ativa = true";
        PreparedStatement ps;
        try{
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(sql);
            ps.setBigDecimal(1, saldo);
            ps.setInt(2, numero);
            ps.execute();
            conn.commit();
            ps.close();
            conn.close();
        }catch(SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }

    }
    public void excluir(Integer numero) {
        String sql = "Delete from conta where numero = ?";
        PreparedStatement ps;

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, numero);
            ps.execute();
            ps.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void exclusaoLogica(Integer numero) {
        String sql = "update conta set conta_ativa = false where numero = ?;";
        PreparedStatement ps;

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, numero);
            ps.execute();
            ps.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
