package com.DevSprint.voluntrix_backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DatabaseConnectionTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void databaseConnectionShouldBeEstablished() throws SQLException {
        /*
          Purpose: This test verifies that the application can establish a connection
          to the database.
        */

        // Given - application context is loaded with DataSource

        // When - we try to get a connection
        Connection connection = dataSource.getConnection();
        
        // Then - connection should be valid
        assertTrue(connection.isValid(1000), "Database connection should be valid");
        
        connection.close();
    }

    @Test
    void databaseShouldExecuteQueries() {
        /*
          Purpose: This test verifies that the database can execute simple queries.
        */

        // Given - a simple query
        String query = "SELECT 1";
        
        // When - we execute the query
        Integer result = jdbcTemplate.queryForObject(query, Integer.class);
        
        // Then - we should get the expected result
        assertEquals(1, result, "Database should execute a simple query successfully");
    }
}
