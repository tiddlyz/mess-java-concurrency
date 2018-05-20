package sk.java.concurrency;

import org.junit.Test;


import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ProducerConsumerTest {
    CountDownLatch countDownLatch = new CountDownLatch(2);
    static class ProducerConsumer{
        private String value = "";
        private boolean hasValue = false;
        public void produce(String value) {
            System.out.println("producing .............");
            while (hasValue) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Producing " + value + " as the next consumable");
            this.value = value;
            hasValue = true;
        }
        public String consume() {
            System.out.println("consuming .............");
            while (!hasValue) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            String value = this.value;
            hasValue = false;
            System.out.println("Consumed " + value);
            return value;
        }
    }

    @Test
    public void testProduceConsume() throws InterruptedException {
        ProducerConsumer producerConsumer = new ProducerConsumer();
        List<String> values = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8",
                "9", "10", "11", "12", "13");
        Thread writerThread = new Thread(() -> values.stream()
                .forEach(producerConsumer::produce));
        Thread readerThread = new Thread(() -> {
            for (int i = 0; i < values.size(); i++) {
                producerConsumer.consume();
            }
        });

        writerThread.start();
        readerThread.start();

        writerThread.join();
        readerThread.join();

    }
}
