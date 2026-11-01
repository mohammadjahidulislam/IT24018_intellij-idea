package com.example.util;

public class PrimeUtil {

    public static final int MAX_N = 200000;

    private PrimeUtil() { }

    public static boolean isPrime(int x) {
        if (x < 2) return false;
        for (int i = 2; (long) i * i <= x; i++) {
            if (x % i == 0) return false;
        }
        return true;
    }

    /** N-তম মৌলিক সংখ্যা (1-তম = 2) */
    public static int nthPrime(int n) {
        if (n < 1) throw new IllegalArgumentException("N must be >= 1");
        int count = 0, num = 1;
        while (count < n) {
            num++;
            if (isPrime(num)) count++;
        }
        return num;
    }
}
