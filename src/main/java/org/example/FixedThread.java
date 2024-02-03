package org.example;

import java.util.concurrent.Semaphore;

public class FixedThread extends Thread {

    private Semaphore sem;

    private Runnable runnable;

    FixedThread(Semaphore sem, Runnable runnable) {
        this.sem = sem;
        this.runnable = runnable;
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
        System.out.println("set runnable " + runnable.toString());
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public void run() {
        while(true) {
            try {
                Thread.sleep(100);
                if (runnable != null) {
//                    System.out.println("Fixed thread " + Thread.currentThread().getName());
                    sem.acquire();
                    runnable.run();
                    sem.release();
                    runnable = null;
                }
            } catch (InterruptedException e) {
                System.out.println("Что-то пошло не так!");
            }
        }
    }
}
