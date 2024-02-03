package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FixedThreadPoolTest {

    @Test
    void givenFixedPool_whenStart_thenWork() throws InterruptedException {
        FixedThreadPool fixedThreadPool = new FixedThreadPool(3);
        fixedThreadPool.execute(() -> System.out.println(1));
        fixedThreadPool.execute(() -> System.out.println(2));
        fixedThreadPool.start();
        Thread.sleep(1000);
        assertTrue(fixedThreadPool.getTasks().isEmpty());
    }


    @Test
    void givenFixedPool_whenStart_thenWaitAndSeeTasksRunAfterStart() throws InterruptedException {
        FixedThreadPool fixedThreadPool = new FixedThreadPool(2);
        fixedThreadPool.execute(createSleepingRunnable("1"));
        fixedThreadPool.execute(createSleepingRunnable("1"));
        fixedThreadPool.start();
        fixedThreadPool.execute(createSleepingRunnable("1"));
        Thread.sleep(1000);
        assertEquals(1, fixedThreadPool.getTasks().size());
    }

    @Test
    void givenFixedPool_whenStart_thenWaitAndSeeAllTasksRunAfterStart() throws InterruptedException {
        FixedThreadPool fixedThreadPool = new FixedThreadPool(2);
        fixedThreadPool.execute(createSleepingRunnable("1"));
        fixedThreadPool.execute(createSleepingRunnable("1"));
        fixedThreadPool.start();
        fixedThreadPool.execute(createSleepingRunnable("1"));
        Thread.sleep(2000);
        assertEquals(0, fixedThreadPool.getTasks().size());
    }

    private Runnable createSleepingRunnable(String name) {
        return () -> {
            try {
                System.out.println(name);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
    }

    @Test
    void givenFixedPool_whenExecute_thenAddRunnablesToList() throws InterruptedException {
        FixedThreadPool fixedThreadPool = new FixedThreadPool(3);
        fixedThreadPool.execute(() -> System.out.println(1));
        fixedThreadPool.execute(() -> System.out.println(2));
        Thread.sleep(200);
        assertEquals(2, fixedThreadPool.getTasks().size());
    }

    @Test
    void givenFixedPool_whenCreated_thenCorrectAmountOfThreads() {
        FixedThreadPool fixedThreadPool = new FixedThreadPool(3);
        assertEquals(3, fixedThreadPool.getAmountOfThreads());
    }
}