package com.example;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/prime")
public class PrimeServlet extends HttpServlet {

    private static final String URL  = "jdbc:mysql://localhost:3306/primedb";
    private static final String USER = "root";
    private static final String PASS = "Jahid@ict21";   // <-- নিজের MySQL পাসওয়ার্ড বসাও
    private static final int MAX_N = 200000;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL driver not found", e);
        }
    }

    // ফর্ম + সেভ করা তালিকা দেখায়
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        render(resp, null);
    }

    // N নিয়ে হিসাব করে MySQL এ সেভ করে
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String msg;
        try {
            int n = Integer.parseInt(req.getParameter("n").trim());
            if (n < 1 || n > MAX_N) {
                msg = "N অবশ্যই ১ থেকে " + MAX_N + " এর মধ্যে হতে হবে।";
            } else {
                int prime = nthPrime(n);
                save(n, prime);
                msg = n + "-তম মৌলিক সংখ্যা = " + prime + " (ডাটাবেসে সেভ হয়েছে)";
            }
        } catch (NumberFormatException | NullPointerException e) {
            msg = "দয়া করে একটি সঠিক সংখ্যা দিন।";
        } catch (SQLException e) {
            msg = "ডাটাবেস এরর: " + e.getMessage();
        }
        render(resp, msg);
    }

    private static int nthPrime(int n) {
        int count = 0, num = 1;
        while (count < n) {
            num++;
            boolean prime = true;
            for (int i = 2; (long) i * i <= num; i++) {
                if (num % i == 0) { prime = false; break; }
            }
            if (prime) count++;
        }
        return num;
    }

    private static void save(int n, int prime) throws SQLException {
        String sql = "INSERT INTO prime_store (n, nth_prime) VALUES (?, ?) " +
                     "ON DUPLICATE KEY UPDATE nth_prime = VALUES(nth_prime)";
        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, n);
            ps.setInt(2, prime);
            ps.executeUpdate();
        }
    }

    private void render(HttpServletResponse resp, String msg) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            out.println("<!DOCTYPE html><html><head><meta charset='UTF-8'>");
            out.println("<title>Nth Prime</title>");
            out.println("<style>body{font-family:Arial;margin:40px}table{border-collapse:collapse}"
                      + "td,th{border:1px solid #888;padding:6px 14px}.msg{margin:14px 0;font-weight:bold}</style>");
            out.println("</head><body>");
            out.println("<h2>Nth Prime Number (Servlet + MySQL)</h2>");
            out.println("<form method='post' action='prime'>");
            out.println("N: <input type='number' name='n' min='1' max='" + MAX_N + "' required> ");
            out.println("<button type='submit'>Find &amp; Save</button></form>");
            if (msg != null) out.println("<div class='msg'>" + escape(msg) + "</div>");

            out.println("<h3>Saved in database</h3>");
            try (Connection con = DriverManager.getConnection(URL, USER, PASS);
                 Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery("SELECT n, nth_prime FROM prime_store ORDER BY n")) {
                out.println("<table><tr><th>N</th><th>Nth Prime</th></tr>");
                while (rs.next()) {
                    out.println("<tr><td>" + rs.getInt(1) + "</td><td>" + rs.getInt(2) + "</td></tr>");
                }
                out.println("</table>");
            } catch (SQLException e) {
                out.println("<p style='color:red'>ডাটাবেস এরর: " + escape(e.getMessage()) + "</p>");
            }
            out.println("</body></html>");
        }
    }

    private static String escape(String s) {
        return s == null ? "" : s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
