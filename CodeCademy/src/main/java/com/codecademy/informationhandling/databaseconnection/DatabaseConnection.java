package com.codecademy.informationhandling.databaseconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseConnection {

    private final String connectionUrl = "jdbc:sqlserver://localhost\\MSSQLSERVER;databaseName=CodeCademyDB;user=sa;password=LucasKokSQL";
    private Connection con;
    private Statement stmt;
    private ResultSet rs;

    public DatabaseConnection() {
    }

    public void CloseResultSet() {
        if (rs != null) try {
            rs.close();
        } catch (Exception ignored) {
        }
        if (stmt != null) try {
            stmt.close();
        } catch (Exception ignored) {
        }
        if (con != null) try {
            con.close();
        } catch (Exception ignored) {
        }
    }

    public ResultSet getQuery(String query) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(connectionUrl);
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    public void setQuery(String query) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(connectionUrl);
            stmt = con.createStatement();
            stmt.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception ignored) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception ignored) {
                }
            }
            if (con != null) try {
                con.close();
            } catch (Exception ignored) {
            }
        }
    }

}
