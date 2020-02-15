package com.dadangnh;

import java.sql.*;

public class DBDriver {

    public static Connection init() throws SQLException{
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/pelatihan_java?currentSchema=buku", "pelatihan_java", "12345");
    }
}
