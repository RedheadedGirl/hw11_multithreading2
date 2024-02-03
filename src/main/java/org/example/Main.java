package org.example;

public class Main {
    public static void main(String[] args) {
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