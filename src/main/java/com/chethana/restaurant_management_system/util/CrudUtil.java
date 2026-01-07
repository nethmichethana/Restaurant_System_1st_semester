package com.chethana.restaurant_management_system.util;

import com.chethana.restaurant_management_system.db.DBConnection;

import java.sql.*;

public class CrudUtil {
    public static boolean execute(String sql, Object... params) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            pstm.setObject(i + 1, params[i]);
        }
        return pstm.executeUpdate() > 0;
    }

    public static ResultSet executeQuery(String sql, Object... params) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            pstm.setObject(i + 1, params[i]);
        }
        return pstm.executeQuery();
    }
}