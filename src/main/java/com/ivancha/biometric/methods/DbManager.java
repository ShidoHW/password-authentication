package com.ivancha.biometric.methods;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DbManager
{
    private static final String PASSWORD = "pass";
    private static final String USERNAME = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5433/keyboard_handwriting";

    private final Connection CONNECTION;
    private static final DbManager MANAGER = new DbManager();


    private DbManager()
    {
        if (MANAGER != null)
            throw new RuntimeException("This class must only be instantiated once.");
        else {

            try {
                CONNECTION = DriverManager.getConnection(URL,
                                                        USERNAME,
                                                        PASSWORD);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Connection getConn() { return CONNECTION; }

    public static DbManager getManager() { return MANAGER; }

    public void closeConnection() {
        try {
            CONNECTION.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
