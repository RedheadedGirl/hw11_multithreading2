package org.example;

public class Main {
    public static void main(String[] args) {
        FixedThreadPool fixedThreadPool = new FixedThreadPool(2);
        fixedThreadPool.execute(() -> {
            System.out.println("1");
            System.out.println("Выполняется " + Thread.currentThread().getName());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Заканчивается " + Thread.currentThread().getName());
        });
        fixedThreadPool.execute(() -> {
            System.out.println("2");
            System.out.println("Выполняется " + Thread.currentThread().getName());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Заканчивается " + Thread.currentThread().getName());
        });
        fixedThreadPool.execute(() -> {
            System.out.println("3");
            System.out.println("Выполняется " + Thread.currentThread().getName());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Заканчивается " + Thread.currentThread().getName());
        });
        fixedThreadPool.start();
    }
}