package org.example;

import java.util.ArrayList;
import java.util.LinkedList;

public class FixedThreadPool implements ThreadPool {

    private final int amountOfThreads;
    private ArrayList<Thread> threads = new ArrayList<>();
    private final LinkedList<Runnable> list = new LinkedList<>();

    public FixedThreadPool(int amountOfThreads) {
        this.amountOfThreads = amountOfThreads;
        this.threads = new ArrayList<>(amountOfThreads);
    }

    @Override
    public void start() {
        while (!list.isEmpty()) {
            threads.removeIf(thread -> thread == null || !thread.isAlive());

            if (threads.size() == amountOfThreads) {
                continue;
            }
            Runnable runnable;
            synchronized(list) {
                runnable = list.peek();
                if (runnable != null) {
                    runnable = list.pop();
                }
            }
            if (runnable != null) {
                Thread thread = new Thread(runnable);
                threads.add(thread);
                thread.start();
            }
        }
    }

    @Override
    public void execute(Runnable runnable) {
        list.add(runnable);
    }
}
