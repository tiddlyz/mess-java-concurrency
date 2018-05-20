package sk.java.concurrency.countdownlatch;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

public class StartAndEndSignalWorker implements Runnable {
    List<String> outputScraper;
    private CountDownLatch readyThreadCounter;
    private CountDownLatch callThreadBlocker;
    private CountDownLatch completeThreadCounter;

    public StartAndEndSignalWorker(List<String> outputScraper, CountDownLatch readyThreadCounter, CountDownLatch callThreadBlocker, CountDownLatch completeThreadCounter) {
        this.outputScraper = outputScraper;
        this.readyThreadCounter = readyThreadCounter;
        this.callThreadBlocker = callThreadBlocker;
        this.completeThreadCounter = completeThreadCounter;
    }

    @Override
    public void run() {
        // send ready signal
        readyThreadCounter.countDown();
        try {
            // waiting for starting signal
            callThreadBlocker.await();
            // start
            doSomeWork();
            outputScraper.add("Counted down");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // send complete signal
            completeThreadCounter.countDown();
        }
    }

    private void doSomeWork(){
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(10, 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
