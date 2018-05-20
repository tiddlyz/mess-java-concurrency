package sk.java.concurrency;

import org.junit.Test;

public class WaitNotifyExampleTest {
    static class SyncThread extends Thread{
        int total;

        @Override
        public void run() {
            System.out.println("Thread T is running...");
            synchronized(this){
                System.out.println("Thread T started calculating...");
                for (int i = 0; i <= 100; i++) {
                    total += i;
                }
                notifyAll();
                System.out.println("Thread T notified...");
            }
        }
    }

    static class NonSyncThread extends Thread{
        int total;

        @Override
        public void run() {
            System.out.println("Thread T is running...");
            System.out.println("Thread T started calculating...");
            for (int i = 0; i <= 100; i++) {
                total += i;
            }
            System.out.println("Thread T completed calculating...");
        }
    }

    /**
     * Possible output 1:
     * --- begin --
     * Thread T is running...
     * Waiting for T to complete...
     * Thread T started calculating...
     * Thread T notified...
     * Total is:5050
     * --- end ---
     *
     *
     * Possible output 2:
     * --- begin ---
     * Thread B is running...
     * Thread B started calculating...
     * Thread B notified...
     * Waiting for b to complete...
     * (running for ever)
     * --- end ---
     *
     *
     */
    @Test
    public void testWaitAndNotify(){
        SyncThread t = new SyncThread();
        t.start();
        // The following makes possible output 1 happen more often
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        synchronized (t){
            System.out.println("Waiting for T to complete...");
            try {
                t.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Total is:" + t.total);
        }
    }

    /**
     * Possible output :
     * -- begin ---
     * Thread B is running...
     * Thread B started calculating...
     * Thread B completed calculating...
     * Total is:0
     * --- end ---
     * [note]: The result would be 0, 10, etc. Because sum is not finished before it is used.
     */

    @Test
    public void testWithoutWaitAndNotify(){
        NonSyncThread t = new NonSyncThread();
        t.start();
        System.out.println("Total is:" + t.total);
    }

}
