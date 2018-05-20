package sk.java.thread;

import org.junit.Test;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class JoinExampleTest {
    static class RunnableImpl implements Runnable{
        private final String name;
        private final CountDownLatch countDownLatch;

        public RunnableImpl(String name, CountDownLatch countDownLatch) {
            this.name = name;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            System.out.println(name + ": Begin sleep()");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }finally {
                System.out.println(name + ": End sleep()");
                countDownLatch.countDown();
            }
        }
    }

    static class ThreadWithSyncRun extends Thread {
        private final String name;
        private Thread thread;
        private final CountDownLatch countDownLatch;

        public ThreadWithSyncRun(String name, Thread thread, CountDownLatch countDownLatch) {
            this.name = name;
            this.thread = thread;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            synchronized (thread) {
                System.out.println(name + ": getObjectLock");
                try {
                    Thread.sleep(5000);
                    System.out.println(name + ": releaseObjectLock");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("finally release lock ");

                    countDownLatch.countDown();
                }
            }
        }
    }

    static class ThreadWithoutSyncRun extends Thread{
        private final String name;
        private final CountDownLatch countDownLatch;

        public ThreadWithoutSyncRun(String name, CountDownLatch countDownLatch) {
            this.name = name;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            System.out.println(name + ": getObjectLock");
            try {
                Thread.sleep(5000);
                System.out.println(name + ": releaseObjectLock");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println(name + ": finally releaseObjectLock");
                countDownLatch.countDown();
            }
        }
    }

    @Test
    public void whenParallelProcessing_thenMainThreadWillBlockUntilCompletion(){
        final CountDownLatch countDownLatch = new CountDownLatch(2);
        Thread runnableThread = new Thread(new RunnableImpl("RunnableImpl", countDownLatch));
        Thread syncRunThread = new ThreadWithSyncRun("ThreadWithSyncRun", runnableThread, countDownLatch);

        syncRunThread.start();
        runnableThread.start();

        try {
            runnableThread.join();
            System.out.println("Main Thread join finished.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("------ end ---------");
    }

    @Test
    public void whenParallelProcessing_thenMainThreadWillBlockUntilCompletionX() {
        final CountDownLatch countDownLatch = new CountDownLatch(2);
        //final List<String> outputScraper = Collections.synchronizedList(new ArrayList<>());

        Thread runnableThread = new Thread(new RunnableImpl("RunnableImpl", countDownLatch));
        Thread syncRunThread = new ThreadWithoutSyncRun("ThreadWithSyncRun", countDownLatch);

        syncRunThread.start();
        runnableThread.start();

        try {
            runnableThread.join();
            System.out.println("Main Thread join finished.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        System.out.println("before sync runnable thread");
//        synchronized (runnableThread) {
//            try {
//                System.out.println("waiting runnable thread");
//                runnableThread.wait(2000);
//                System.out.println("end waiting runnable thread");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        System.out.println("before sync runTread");
//        synchronized (syncRunThread) {
//            try {
//                System.out.println("waiting sync  runThread");
//                syncRunThread.wait();
//                System.out.println("end waiting sync runThread");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("--------- end of without ---------");

    }

//    public static void main(String[] args) {
//
//        Thread runnableThread = new Thread(new RunnableImpl("RunnableImpl"));
//        Thread syncRunThread = new ThreadWithoutSyncRun("ThreadWithSyncRun");
//
//        syncRunThread.start();
//        runnableThread.start();
//
//        try {
//            runnableThread.join();
//            System.out.println("Main Thread join finished.");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }


    // Display a message, preceded by
    // the name of the current thread
    static void threadMessage(String message) {
        String threadName =
                Thread.currentThread().getName();
        System.out.format("%s: %s%n",
                threadName,
                message);
    }

    private static class MessageLoop
            implements Runnable {
        public void run() {
            String importantInfo[] = {
                    "Mares eat oats",
                    "Does eat oats",
                    "Little lambs eat ivy",
                    "A kid will eat ivy too"
            };
            try {
                for (int i = 0;
                     i < importantInfo.length;
                     i++) {
                    // Pause for 4 seconds
                    Thread.sleep(4000);
                    // Print a message
                    threadMessage(importantInfo[i]);
                }
            } catch (InterruptedException e) {
                threadMessage("I wasn't done!");
            }
        }
    }

    public static void main(String args[])
            throws InterruptedException {

        // Delay, in milliseconds before
        // we interrupt MessageLoop
        // thread (default one hour).
        long patience = 1000 * 60 * 60;

        // If command line argument
        // present, gives patience
        // in seconds.
        if (args.length > 0) {
            try {
                patience = Long.parseLong(args[0]) * 1000;
            } catch (NumberFormatException e) {
                System.err.println("Argument must be an integer.");
                System.exit(1);
            }
        }

        threadMessage("Starting MessageLoop thread");
        long startTime = System.currentTimeMillis();
        Thread t = new Thread(new MessageLoop());
        t.start();

        threadMessage("Waiting for MessageLoop thread to finish");
        // loop until MessageLoop
        // thread exits
        while (t.isAlive()) {
            threadMessage("Still waiting...");
            // Wait maximum of 1 second
            // for MessageLoop thread
            // to finish.
            t.join(1000);
            if (((System.currentTimeMillis() - startTime) > patience)
                    && t.isAlive()) {
                threadMessage("Tired of waiting!");
                t.interrupt();
                // Shouldn't be long now
                // -- wait indefinitely
                t.join();
            }
        }
        threadMessage("Finally!");
    }
}