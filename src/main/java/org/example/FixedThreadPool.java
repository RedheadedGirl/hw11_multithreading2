package org.example;

import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Semaphore;

@Getter
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
            List<FixedThread> fixedThreads = createDefaultThreads(semaphore);

            while (true) {
                Runnable runnable;
                synchronized (tasks) {
                    runnable = tasks.peek();
                    if (runnable != null && !tasks.isEmpty()) {
                        Optional<FixedThread> first = fixedThreads.stream()
                                .filter(thread -> thread.getRunnable() == null)
                                .findFirst();
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
                            System.out.println("дождался, размер списка задач: " + tasks.size());
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
            new Thread(() -> {
                synchronized (this) {
                    tasks.add(runnable);
                    notify();
                }
            }).start();
        } else {
            tasks.add(runnable);
        }
    }

    private List<FixedThread> createDefaultThreads(Semaphore semaphore) {
        List<FixedThread> fixedThreads = new ArrayList<>();
        for (int i = 0; i < amountOfThreads; i++) {
            FixedThread one = new FixedThread(semaphore, null);
            fixedThreads.add(one);
            one.start();
        }
        return fixedThreads;
    }
}
