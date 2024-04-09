package com.lukel.ia4;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

//Controller and Model
@Controller
public class UserController {

    @GetMapping("/")
    public String basePage() {
        return "userForm";
    }

    //html userForm
    @GetMapping("/userForm")
    public String showUserForm() {
        return "userForm";
    }

    //mapping for submitting a user
    @PostMapping("/submitUser")
    public String submitUser(@RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam(required = false) boolean isAdmin,
            @RequestParam int numPoints) {
        Connection conn = null;
        try {
            String jdbcUrl = "jdbc:mysql://localhost/crudapp";
            String username = "root";
            String password = "";
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(jdbcUrl, username, password);
            String insertQuery = "INSERT INTO users (first_name, last_name, is_admin, num_points) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStmt = conn.prepareStatement(insertQuery);
            preparedStmt.setString(1, firstName);
            preparedStmt.setString(2, lastName);
            preparedStmt.setBoolean(3, isAdmin);
            preparedStmt.setInt(4, numPoints);
            preparedStmt.executeUpdate();
            System.out.println("NPC Inserted.");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Exception: " + e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //Refresh
        return "redirect:/userForm";
    }

    //users html
    @GetMapping("/users")
    public ModelAndView showUsers() {
        ModelAndView modelAndView = new ModelAndView("users");
        Connection conn = null;
        try {
            String jdbcUrl = "jdbc:mysql://localhost/crudapp";
            String username = "root";
            String password = "";

            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(jdbcUrl, username, password);

            String selectQuery = "SELECT * FROM users";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(selectQuery);

            //display data
            List<UserOfApp> userList = new ArrayList<>();
            while (rs.next()) {
                UserOfApp users = new UserOfApp();
                users.setId(rs.getInt("id"));
                users.setFirstName(rs.getString("first_name"));
                users.setLastName(rs.getString("last_name"));
                users.setAdmin(rs.getBoolean("is_admin"));
                users.setNumPoints(rs.getInt("num_points"));
                userList.add(users);
            }
            modelAndView.addObject("users", userList);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Exception: " + e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return modelAndView;
    }

    //delete function
    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam int id) {
        Connection conn = null;
        try {
            String jdbcUrl = "jdbc:mysql://localhost/crudapp";
            String username = "root";
            String password = "";
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(jdbcUrl, username, password);

            String deleteQuery = "DELETE FROM users WHERE id = ?";
            PreparedStatement preparedStmt = conn.prepareStatement(deleteQuery);
            preparedStmt.setInt(1, id);
            preparedStmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error: " + e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return "redirect:/users";
    }

    //update function
    @PostMapping("/updateUser")
    public String updateUser(@RequestParam int id,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam(required = false) boolean isAdmin,
            @RequestParam int numPoints) {
        Connection conn = null;
        try {
            String jdbcUrl = "jdbc:mysql://localhost/crudapp";
            String username = "root";
            String password = "";

            Class.forName("com.mysql.cj.jdbc.Driver");

            conn = DriverManager.getConnection(jdbcUrl, username, password);

            String updateQuery = "UPDATE users SET first_name = ?, last_name = ?, is_admin = ?, num_points = ? WHERE id = ?";
            PreparedStatement preparedStmt = conn.prepareStatement(updateQuery);
            preparedStmt.setString(1, firstName);
            preparedStmt.setString(2, lastName);
            preparedStmt.setBoolean(3, isAdmin);
            preparedStmt.setInt(4, numPoints);
            preparedStmt.setInt(5, id);

            preparedStmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error: " + e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return "redirect:/users";
    }

}
