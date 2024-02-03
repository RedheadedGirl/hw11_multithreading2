package org.example;

import org.example.exceptions.SettingException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Semaphore;

public class ScalableThreadPool implements ThreadPool {
    private final int minAmountOfThreads;
    private final int maxAmountOfThreads;
    private final LinkedList<Runnable> tasks = new LinkedList<>();

    public ScalableThreadPool(int minAmountOfThreads, int maxAmountOfThreads) throws SettingException {
        this.minAmountOfThreads = minAmountOfThreads;
        this.maxAmountOfThreads = maxAmountOfThreads;

        if(minAmountOfThreads > maxAmountOfThreads) {
            throw new SettingException("Неверно задано число потоков!");
        }
    }

    @Override
    public void start() {
        new Thread( () -> {
            Semaphore semaphore = new Semaphore(maxAmountOfThreads);
            List<FixedThread> fixedThreads = createDefaultThreads(semaphore);

            while (true) {
                Runnable runnable;
                synchronized (tasks) {
                    runnable = tasks.peek();
                    if (runnable != null && !tasks.isEmpty()) {
                        Optional<FixedThread> first = fixedThreads.stream().filter(thread -> thread.getRunnable() == null).findFirst();
                        if (first.isPresent()) {
                            first.get().setRunnable(runnable);
                            tasks.pop();
                        } else {
                            if (fixedThreads.size() < maxAmountOfThreads) {
                                FixedThread one = new FixedThread(semaphore, runnable);
                                tasks.pop();
                                fixedThreads.add(one);
                                one.start();
                            }
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

                cleanUnnecessaryThreads(fixedThreads);
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
        for (int i = 0; i < minAmountOfThreads; i++) {
            FixedThread one = new FixedThread(semaphore, null);
            fixedThreads.add(one);
            one.start();
        }
        return fixedThreads;
    }

    private void cleanUnnecessaryThreads(List<FixedThread> fixedThreads) {
        // зачищаем нерабочие потоки, оставляя минимальное кол-во
        List<FixedThread> notWorkingThreads = fixedThreads.stream()
                .filter(thread -> thread.getRunnable() == null)
                .limit(maxAmountOfThreads - minAmountOfThreads)
                .toList();
        if (!notWorkingThreads.isEmpty()) {
            System.out.println("found not working threads: " + notWorkingThreads.size());
        }
        fixedThreads.removeAll(notWorkingThreads);
    }
}
