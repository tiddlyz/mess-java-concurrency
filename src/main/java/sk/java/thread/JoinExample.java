package sk.java.thread;

import java.util.List;

public class JoinExample {
    public static void main(String[] args) {
        String threadName = Thread.currentThread().getName();
        Thread t = new Thread(new RunnableImplX());

        new ThreadTest(t).start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(t.toString());
        t.start();
        try {
            t.join();
            System.out.println("Main Thread ("+ threadName + "): joinFinish.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    static class RunnableImpl implements Runnable{
        private final String name;
        private List<String> outputScraper;

        public RunnableImpl(String name, List<String> outputScraper) {
            this.name = name;
            this.outputScraper = outputScraper;
        }

        @Override
        public void run() {
            outputScraper.add(name + ": Begin sleep()");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }finally {
                outputScraper.add(name + ": End sleep()");
            }
        }
    }

    static class ThreadWithSyncRun extends Thread {
        private final String name;
        private List<String> outputScraper;
        private Thread thread;

        public ThreadWithSyncRun(String name, List<String>outputScraper, Thread thread) {
            this.name = name;
            this.outputScraper = outputScraper;
            this.thread = thread;
        }

        @Override
        public void run() {
            synchronized (thread) {
                outputScraper.add(name + ": getObjectLock");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    outputScraper.add(name + ": releaseObjectLock");
                }
            }
        }
    }

    static class ThreadWithoutSyncRun extends Thread{
        private final String name;
        private List<String> outputScraper;
        private Thread thread;

        public ThreadWithoutSyncRun(String name, List<String>outputScraper, Thread thread) {
            this.name = name;
            this.outputScraper = outputScraper;
            this.thread = thread;
        }

        @Override
        public void run() {
            outputScraper.add(name + ": getObjectLock");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                outputScraper.add(name + ": releaseObjectLock");
            }
        }
    }
    static class RunnableImplX implements Runnable {
        public void run() {
            try {
                String name = Thread.currentThread().getName();
                System.out.println("RunnableImpl Thread (" +  name + "): Begin sleep.");
                Thread.sleep(2000);
                System.out.println("RunnableImpl Thread (" +  name + "): End sleep.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    static class ThreadTest extends Thread {
        Thread thread;

        public ThreadTest(Thread thread) {
            this.thread = thread;
        }

        @Override
        public void run() {
           synchronized (thread) {
                String name = Thread.currentThread().getName();
                System.out.println("ThreadTest Thread (" + name + "): getObjectLock ");
                try {
                    Thread.sleep(9000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                System.out.println("ThreadTest Thread (" + name + "): ReleaseObjectLock");
            }
        }
    }
}
