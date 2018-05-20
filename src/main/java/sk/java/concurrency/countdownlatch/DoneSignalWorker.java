package sk.java.concurrency.countdownlatch;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

public class DoneSignalWorker implements Runnable {
    private List<String> outputScraper;
    private CountDownLatch doneSignal;

    public DoneSignalWorker(List<String> output, CountDownLatch doneSignal) {
        this.outputScraper = output;
        this.doneSignal = doneSignal;
    }

    @Override
    public void run() {
        // do some work
        try {
            //* For concurrent access, using ThreadLocalRandom instead of Math.random() results in
            //* less contention and, ultimately, better performance.
            Thread.sleep(ThreadLocalRandom.current().nextInt(0, 5000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        outputScraper.add("Counted down");
        doneSignal.countDown();
    }
}
