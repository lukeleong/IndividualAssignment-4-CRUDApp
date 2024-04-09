package com.lukel.ia4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Ia4Application {

    public static void main(String[] args) {
        SpringApplication.run(Ia4Application.class, args);
        Connection conn = null;
        try {

            // Connect to xampp sql
            String jdbcUrl = "jdbc:mysql://localhost/crudapp";
            String username = "root";
            String password = "";

            // Driver manager
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(jdbcUrl, username, password);

            // Create table sql
            String createTableSQL = "CREATE TABLE IF NOT EXISTS users ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "first_name VARCHAR(50),"
                    + "last_name VARCHAR(50),"
                    + "is_admin BOOLEAN,"
                    + "num_points INT)";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(createTableSQL);
            System.out.println("Table created successfully.");
            String insertQuery = "INSERT INTO users (first_name, last_name, is_admin, num_points)"
                    + "VALUES (?, ?, ?, ?)";

            //Basic entry
            PreparedStatement preparedStmt = conn.prepareStatement(insertQuery);
            preparedStmt.setString(1, "Random");
            preparedStmt.setString(2, "NPC");
            preparedStmt.setBoolean(3, false);
            preparedStmt.setInt(4, 1000);
            preparedStmt.executeUpdate();
            System.out.println("NPC Inserted.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("PROGRAM ERROR KABOOM: " + e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("SQL ERROR KABOOM: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
