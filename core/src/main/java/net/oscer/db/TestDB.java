package net.oscer.db;

import net.oscer.dao.UserDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据库测试
 */
public class TestDB {

    public static void main(String[] args) throws SQLException {
        for(int i=2;i<=100;i++) {
            try (Connection conn = DBManager.getConnection()) {
                try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE id = ?")) {
                    ps.setLong(1, i);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            System.out.printf("%d => %s\n", rs.getLong("id"), rs.getString("name"));
                        }
                    }
                }
            }
            UserDAO.ME.getById(2);
        }
        DBManager.close();
    }

}
