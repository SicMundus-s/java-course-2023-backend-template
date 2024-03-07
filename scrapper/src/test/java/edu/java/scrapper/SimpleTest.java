package edu.java.scrapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertTrue;

class SimpleTest extends IntegrationTest {

    @Test
    void testMigration() throws Exception {

        try (
            Connection connection = DriverManager.getConnection(
                POSTGRES.getJdbcUrl(),
                POSTGRES.getUsername(),
                POSTGRES.getPassword()
            );
            Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT EXISTS (" +
                "SELECT 1 FROM information_schema.tables " +
                "WHERE table_schema = 'public' " +
                "AND table_name = 'chats'" +
                ");");

            assertTrue(resultSet.next());
        }
    }
}
