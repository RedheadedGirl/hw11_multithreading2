package org.example;

import org.example.exceptions.SettingException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScalableThreadPoolTest {

    @Test
    void givenScalablePool_whenStart_thenWork() throws InterruptedException, SettingException {
        ScalableThreadPool scalableThreadPool = new ScalableThreadPool(1, 3);
        scalableThreadPool.execute(() -> System.out.println(1));
        scalableThreadPool.execute(() -> System.out.println(2));
        scalableThreadPool.start();
        Thread.sleep(1000);
        assertTrue(scalableThreadPool.getTasks().isEmpty());
    }

    @Test
    void givenScalablePool_whenStart_thenWaitAndSeeTasksRunAfterStart() throws InterruptedException, SettingException {
        ScalableThreadPool scalableThreadPool = new ScalableThreadPool(2, 3);
        scalableThreadPool.execute(createSleepingRunnable("1"));
        scalableThreadPool.execute(createSleepingRunnable("1"));
        scalableThreadPool.execute(createSleepingRunnable("1"));
        scalableThreadPool.execute(createSleepingRunnable("1"));
        scalableThreadPool.start();
        scalableThreadPool.execute(createSleepingRunnable("1"));
        Thread.sleep(1000);
        assertEquals(2, scalableThreadPool.getTasks().size());
    }

    @Test
    void givenScalablePool_whenStart_thenWaitAndSeeAllTasksRunAfterStart() throws InterruptedException, SettingException {
        ScalableThreadPool scalableThreadPool = new ScalableThreadPool(2, 3);
        scalableThreadPool.execute(createSleepingRunnable("1"));
        scalableThreadPool.execute(createSleepingRunnable("1"));
        scalableThreadPool.execute(createSleepingRunnable("1"));
        scalableThreadPool.start();
        scalableThreadPool.execute(createSleepingRunnable("1"));
        Thread.sleep(2000);
        assertEquals(0, scalableThreadPool.getTasks().size());
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
    void givenScalablePool_whenExecute_thenAddRunnablesToList() throws SettingException, InterruptedException {
        ScalableThreadPool scalableThreadPool = new ScalableThreadPool(2, 2);
        scalableThreadPool.execute(() -> System.out.println(1));
        scalableThreadPool.execute(() -> System.out.println(2));
        Thread.sleep(200);
        assertEquals(2, scalableThreadPool.getTasks().size());
    }

    @Test
    void givenScalablePool_whenCreateWithBadSettings_thenThrowSettingException() {
        SettingException settingException = assertThrows(SettingException.class, () -> new ScalableThreadPool(3, 2));
        assertEquals("Неверно задано число потоков!", settingException.getMessage());
    }

}