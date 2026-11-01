package com.example.dao;

import com.example.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

public class PrimeNumberDAO {

    /** n ও nth_prime সেভ করে। n আগে থাকলে মান আপডেট হয়। */
    public void save(int n, int nthPrime) throws SQLException {
        String sql = "INSERT INTO prime_store (n, nth_prime) VALUES (?, ?) " +
                     "ON DUPLICATE KEY UPDATE nth_prime = VALUES(nth_prime)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, n);
            ps.setInt(2, nthPrime);
            ps.executeUpdate();
        }
    }

    /** সব সেভ করা রেকর্ড (n -> nth_prime), n অনুযায়ী সাজানো। */
    public Map<Integer, Integer> findAll() throws SQLException {
        Map<Integer, Integer> map = new LinkedHashMap<>();
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT n, nth_prime FROM prime_store ORDER BY n")) {
            while (rs.next()) {
                map.put(rs.getInt("n"), rs.getInt("nth_prime"));
            }
        }
        return map;
    }
}
