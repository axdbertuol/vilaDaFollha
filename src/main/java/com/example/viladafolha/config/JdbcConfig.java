package com.example.viladafolha.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Configuration
public class JdbcConfig {

	DataSource dataSource;
	String url = "jdbc:postgresql://localhost:5432/vila_da_folha";
	String user = "postgres";
	String pass = "postgres";

	public JdbcConfig() {
		ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
		comboPooledDataSource.setJdbcUrl(url);
		comboPooledDataSource.setUser(user);
		comboPooledDataSource.setPassword(pass);

		comboPooledDataSource.setMaxPoolSize(15);
		this.dataSource = comboPooledDataSource;
	}

	@Bean
	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}
}