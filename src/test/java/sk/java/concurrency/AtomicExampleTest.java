package sk.java.concurrency;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Origin: https://garygregory.wordpress.com/2011/09/09/multi-threaded-unit-testing/
 */

public class AtomicExampleTest {

    interface IdGenerator{
        long nextId();
    }

    /**
     * Generates sequential unique IDs starting with 1, 2, 3, and so on.
     * <p>
     * This class is NOT thread-safe.
     * </p>
     */
    static class BrokenUniqueIdGenerator implements IdGenerator{
        private long counter = 0;  // volatile1 will NOT solve the issue

        public long nextId() {
            try {
                // simulate a time-consuming work which can increase the possibility of creating thread issues
                Thread.sleep(ThreadLocalRandom.current().nextInt(0,200));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return ++counter;
        }
    }

    /**
     * Generates sequential unique IDs starting with 1, 2, 3, and so on.
     * <p>
     * This class is thread-safe.
     * </p>
     */
    static class UniqueIdGenerator implements IdGenerator{
        private final AtomicLong counter = new AtomicLong();

        public long nextId() {
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(0,200));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return counter.incrementAndGet();
        }
    }

    private void test(final int threadCount, final IdGenerator idGenerator) throws InterruptedException, ExecutionException {
        Callable<Long> task = new Callable<>() {
            @Override
            public Long call() {
                return idGenerator.nextId();
            }
        };
        List<Callable<Long>> tasks = Collections.nCopies(threadCount, task);

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        List<Future<Long>> futures = executorService.invokeAll(tasks);
        List<Long> resultList = new ArrayList<>(futures.size());

        // Check for exceptions
        for (Future<Long> future : futures) {
            // Throws an exception if an exception was thrown by the task.
            // No exception will be captured if future.get() is not called.
            resultList.add(future.get());
        }
        // Validate the IDs
        Assert.assertEquals(threadCount, futures.size());

        List<Long> expectedList = new ArrayList<>(threadCount);
        for (long i = 1; i <= threadCount; i++) {
            expectedList.add(i);
        }

        // sort result in ascending order
        Collections.sort(resultList);

        Assert.assertEquals(expectedList, resultList);

    }

    @Test
    public void testBrokenUniqueIdGenerator() throws ExecutionException, InterruptedException {
        int count = 5;

        for (int i = 0; i < count; i++) {
            int numOfThreads = ThreadLocalRandom.current().nextInt(1,50);
            test(numOfThreads, new BrokenUniqueIdGenerator());
        }
    }

    @Test
    public void testUniqueIdGenerator() throws ExecutionException, InterruptedException {
        int count = 5;

        for (int i = 0; i < count; i++) {
            int numOfThreads = ThreadLocalRandom.current().nextInt(1,50);
            test(numOfThreads, new UniqueIdGenerator());
        }
    }
}
