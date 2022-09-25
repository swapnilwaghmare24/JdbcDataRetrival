package com.bridgelabz.jdbcconnection;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

public class JdbcConnection {

        private static JdbcConnection dbConnection;

        private Connection con;

        private JdbcConnection() {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                System.out.println("Driver loaded successfully");
            } catch (ClassNotFoundException e) {
                System.out.println("Unable to load driver");
                e.printStackTrace();
            }
        }

        public static JdbcConnection init() {
            if (dbConnection == null)
                dbConnection = new JdbcConnection();
            return dbConnection;
        }

        public Connection getConnection() {
            String jdbcStr = "jdbc:mysql://localhost:3306/payroll_service";
            String userName = "root";
            String password = "root";
            try {
                con = DriverManager.getConnection(jdbcStr, userName, password);
            } catch (SQLException e) {
                System.out.println("Database connection failed");
                e.printStackTrace();
            }
            return con;
        }
}
