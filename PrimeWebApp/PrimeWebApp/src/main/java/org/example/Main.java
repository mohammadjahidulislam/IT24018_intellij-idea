package org.example;

import com.example.dao.PrimeNumberDAO;
import com.example.util.PrimeUtil;

import java.sql.SQLException;
import java.util.Scanner;

/** কনসোল থেকে চালানোর জন্য (সার্ভার ছাড়া)। */
public class Main {

    public static void main(String[] args) throws SQLException {
        Scanner sc = new Scanner(System.in);
        PrimeNumberDAO dao = new PrimeNumberDAO();

        System.out.print("n er man din: ");
        int n = sc.nextInt();

        if (n < 1 || n > PrimeUtil.MAX_N) {
            System.out.println("N must be between 1 and " + PrimeUtil.MAX_N);
            return;
        }

        int prime = PrimeUtil.nthPrime(n);
        dao.save(n, prime);
        System.out.println(n + " th prime = " + prime + " -> saved!");
    }
}
