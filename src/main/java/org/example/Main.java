package org.example;

import org.example.exceptions.SettingException;

public class Main {
    public static void main(String[] args) throws SettingException {
//        fixedThreadPool();
        scalableThreadPool();
    }

    private static void fixedThreadPool() {
        FixedThreadPool fixedThreadPool = new FixedThreadPool(3);
        fixedThreadPool.execute(getRunnable(1));
        fixedThreadPool.execute(getRunnable(2));
        fixedThreadPool.execute(getRunnable(3));
        fixedThreadPool.start();

        System.out.println("Main thread is " + Thread.currentThread().getName());
        try {
            System.out.println("sleep for task 4 started");
            Thread.sleep(10000);
            System.out.println("sleep for task 4 finished");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("add one more");
            fixedThreadPool.execute(getRunnable(4));
        }
    }

    private static void scalableThreadPool() throws SettingException {
        ScalableThreadPool scalableThreadPool = new ScalableThreadPool(2, 3);
        scalableThreadPool.execute(getRunnable(1));
        scalableThreadPool.execute(getRunnable(2));
        scalableThreadPool.execute(getRunnable(3));
        scalableThreadPool.start();

        System.out.println("Main thread is " + Thread.currentThread().getName());
        try {
            System.out.println("sleep for task 4 started");
            Thread.sleep(10000);
            System.out.println("sleep for task 4 finished");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("add one more");
            scalableThreadPool.execute(getRunnable(4));
        }
    }

    private static Runnable getRunnable(int number) {
        return () -> {
            System.out.println(number + "!!!");
            System.out.println("Выполняется " + Thread.currentThread().getName());
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Заканчивается " + Thread.currentThread().getName());
        };
    }
}