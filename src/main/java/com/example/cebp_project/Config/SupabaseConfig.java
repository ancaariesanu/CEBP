package com.example.cebp_project.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SupabaseConfig {
    private static final String URL = "jdbc:postgresql://aws-0-eu-central-1.pooler.supabase.com:6543/postgres?user=postgres.dhehoatnzlbcxtlovckc&password=CEBP_Pas1*c";
    private static final String USER = "postgres.dhehoatnzlbcxtlovckc";
    private static final String PASSWORD = "CEBP_Pas1*c";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}