package com.example.servlet;

import com.example.dao.PrimeNumberDAO;
import com.example.util.PrimeUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;

@WebServlet("/prime")
public class PrimeServlet extends HttpServlet {

    private final PrimeNumberDAO dao = new PrimeNumberDAO();

    // পেজ দেখানো: ফর্ম + সেভ করা তালিকা
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        loadRecords(req);
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }

    // N নিয়ে হিসাব করে ডাটাবেসে সেভ করা
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String message;
        try {
            int n = Integer.parseInt(req.getParameter("n").trim());
            if (n < 1 || n > PrimeUtil.MAX_N) {
                message = "N must be between 1 and " + PrimeUtil.MAX_N;
            } else {
                int prime = PrimeUtil.nthPrime(n);
                dao.save(n, prime);
                message = n + "-th prime = " + prime + "  (saved to database)";
            }
        } catch (NumberFormatException | NullPointerException e) {
            message = "Please enter a valid number.";
        } catch (SQLException e) {
            message = "Database error: " + e.getMessage();
        }
        req.setAttribute("message", message);
        loadRecords(req);
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }

    private void loadRecords(HttpServletRequest req) {
        try {
            req.setAttribute("records", dao.findAll());
        } catch (SQLException e) {
            req.setAttribute("records", Collections.<Integer, Integer>emptyMap());
            req.setAttribute("dbError", e.getMessage());
        }
    }
}
