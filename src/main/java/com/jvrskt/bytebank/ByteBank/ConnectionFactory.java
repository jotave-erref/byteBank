package com.jvrskt.bytebank.ByteBank;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
//Classe responsável pela conexão com o banco da dados
public class ConnectionFactory {

    public Connection conectar(){
        try {
            return createDataSource().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private HikariDataSource createDataSource(){
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/byte_bank");
        config.setUsername("root");
        config.setPassword("*******");
        config.setMaximumPoolSize(10);
        return new HikariDataSource(config);
    }
}
