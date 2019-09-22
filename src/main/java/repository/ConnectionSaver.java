package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

public class ConnectionSaver {
    private static Connection conn = null;

    static Connection getConnection() {
        if (conn != null) {
            return conn;
        }

        try {
            // db parameters
            String url = "jdbc:sqlite:test.db";
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        createTableInDb("Loads", Arrays.asList("id", "name", "Loc_id"));
        createTableInDb("Location", Arrays.asList("id", "name"));

        return conn;
    }

    private static void createTableInDb(String tableName, List<String> columns) {
        Statement statement  = null;
        String sql = "create table if not exists " + tableName + " (";
        for (int i = 0; i < columns.size(); i++) {
            sql = sql + columns.get(i);
            if (i < columns.size() - 1) {
                sql = sql + ", ";
            }
        }
        sql = sql + ");";

        try {
            statement = conn.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeConnection() {
        try {
            if (conn != null) {
                conn.close();
                conn = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
