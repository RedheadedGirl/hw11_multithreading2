package org.example;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Semaphore;

public class FixedThreadPool implements ThreadPool {

    private final int amountOfThreads;
    private final LinkedList<Runnable> tasks = new LinkedList<>();

    public FixedThreadPool(int amountOfThreads) {
        this.amountOfThreads = amountOfThreads;
    }

    @Override
    public void start() {
        new Thread( () -> {
            Semaphore semaphore = new Semaphore(amountOfThreads);
            List<FixedThread> fixedThreads = new ArrayList<>();
            for (int i = 0; i < amountOfThreads; i++) {
                FixedThread one = new FixedThread(semaphore, null);
                fixedThreads.add(one);
                one.start();
            }

            while (true) {
                Runnable runnable;
                synchronized (tasks) {
                    runnable = tasks.peek();
                    if (runnable != null) {
                        Optional<FixedThread> first = fixedThreads.stream().filter(thread -> thread.getRunnable() == null).findFirst();
                        if (first.isPresent()) {
                            first.get().setRunnable(runnable);
                            tasks.pop();
                        }
                    }
                }
                if (tasks.isEmpty()) {
                    synchronized (this) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }).start();
    }

    @Override
    public void execute(Runnable runnable) {
        if (tasks.isEmpty()) {
            tasks.add(runnable);
            new Thread(() -> {
                synchronized (this) {
                    System.out.println("notified");
                    notify();
                }
            }).start();
        } else {
            tasks.add(runnable);
        }
    }
}
