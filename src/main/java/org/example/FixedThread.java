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
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public void run() {
        while(true) {
            try {
                if (runnable != null) {
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
