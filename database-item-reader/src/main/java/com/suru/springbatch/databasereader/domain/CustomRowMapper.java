package com.suru.springbatch.databasereader.domain;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomRowMapper implements RowMapper<Customer> {
    @Override
    public Customer mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Customer(resultSet.getInt("id"), resultSet.getString("first_name"),
                resultSet.getString("last_name"), resultSet.getString("random"));
    }
}
