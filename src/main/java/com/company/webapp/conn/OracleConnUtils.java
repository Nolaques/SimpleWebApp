package com.company.webapp.conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OracleConnUtils {



    public static Connection getOracleConnection()
                throws ClassNotFoundException, SQLException{

        String hostName = "localhost";
        String sid = "sid";
        String userName = "userName";
        String password = "password";

        return getOracleConnection(hostName, sid, userName, password);
    }

    public static Connection getOracleConnection(String hostName, String sid
      , String userName, String password) throws ClassNotFoundException,
            SQLException{

        Class.forName("oracle.jdbc.driver.OracleDriver");

        String connectionURL = "jdbc:oracle:thin:@"+hostName+":1521:"+ sid;

        Connection conn = DriverManager.getConnection(connectionURL, userName,
                password);

        if (conn != null){
            System.out.println("Connected to database");
        }

        return conn;


    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException{
        getOracleConnection();

    }
}
