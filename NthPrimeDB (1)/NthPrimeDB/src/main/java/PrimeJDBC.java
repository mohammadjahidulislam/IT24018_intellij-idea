package org.example;

import java.sql.*;
import java.util.Scanner;

public class PrimeJDBC {

    static final String URL  = "jdbc:mysql://localhost:3306/primedb";
    static final String USER = "root";
    static final String PASS = "Jahid@ict21";
    public static void main(String[] args) throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.print("n er man din: ");
        int n = sc.nextInt();

        int count = 0, num = 1;
        while (count < n) {
            num++;
            boolean prime = true;
            for (int i = 2; i * i <= num; i++) {
                if (num % i == 0) { prime = false; break; }
            }
            if (prime) count++;
        }

        // database এ save
        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO prime_store (n, nth_prime) VALUES (?, ?)")) {
            ps.setInt(1, n);
            ps.setInt(2, num);
            ps.executeUpdate();
            System.out.println(n + " th prime = " + num + " -> saved!");
        }
    }
}