package sk.java.tutorial;

import java.lang.management.RuntimeMXBean;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.function.Consumer;

public class ProducerConsumerExample {
    static class Producer implements Runnable {
        BlockingQueue<String> drop;

        public Producer(BlockingQueue<String> drop) {
            this.drop = drop;
        }

        @Override
        public void run() {
            String importantInfo[] = {
                    "Mares eat oats",
                    "Does eat oats",
                    "Little lambs eat ivy",
                    "A kid will eat ivy too"
            };

            Random random = new Random();
            try {
                for (int i = 0; i < importantInfo.length; i++) {
                    drop.put(importantInfo[i]);
                    Thread.sleep(random.nextInt(5000));

                }
                drop.put("DONE");
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    static class Consumer implements Runnable{
        BlockingQueue<String> drop;

        public Consumer(BlockingQueue<String> drop) {
            this.drop = drop;
        }

        @Override
        public void run() {
            Random random = new Random();
            try {
                for (String message = drop.take(); message != "DONE"; message = drop.take()) {
                    System.out.format("MESSAGE RECEIVED: %s%n",
                            message);
                    Thread.sleep(random.nextInt(5000));
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        BlockingQueue<String> drop =
                new SynchronousQueue<String>();
        try {
            drop.put("first");
            String s = drop.take();
            drop.put("second");
        }catch (InterruptedException e){e.printStackTrace();}

        (new Thread(new Producer(drop))).start();
        (new Thread(new Consumer(drop))).start();
    }
}
