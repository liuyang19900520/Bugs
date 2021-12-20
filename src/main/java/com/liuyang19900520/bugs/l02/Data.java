package com.liuyang19900520.bugs.l02;

import lombok.Getter;

/**
 * <p>
 *
 * </p>
 *
 * @author Max Liu
 * @since 2021/12/21
 */
public class Data {

    @Getter
    private static int counter = 0;
    private static Object locker = new Object();

    public static int reset() {
        counter = 0;
        return counter;
    }

    public void right() {
        synchronized (locker) {
            counter++;
        }
    }

    public synchronized void wrong() {
        counter++;
    }
}
