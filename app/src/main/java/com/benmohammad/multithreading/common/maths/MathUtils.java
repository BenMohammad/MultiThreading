package com.benmohammad.multithreading.common.maths;

public class MathUtils {

    public static long multiplyExact(long x, long y) {
        long r = x * y;
        long ax = Math.abs(x);
        long ay = Math.abs(y);

        if(((ax | ay)>>> 31 != 0)) {
            if(((y != 0) && (r / y != x)) ||
                    (x == Long.MIN_VALUE && y == -1)) {
                throw new ArithmeticException("long overflow");
            }
        }

        return r;

    }
}
