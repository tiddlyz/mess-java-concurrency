package sk.java.concurrency.senderandreceiver;

import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Sender implements Runnable {

    private static final Logger logger = Logger.getLogger(Sender.class.getName());

    private Data data;
    // standard constructors


    public Sender(Data data) {
        this.data = data;
    }

    public void run() {
        String packets[] = {
                "First packet",
                "Second packet",
                "Third packet",
                "Fourth packet",
                "End"
        };

        for (String packet : packets) {
            System.out.println("Sending packet: " + packet);
            data.send3(packet);

            // Thread.sleep() to mimic heavy server-side processing
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 5000));
            } catch (InterruptedException e)  {
                Thread.currentThread().interrupt();
                logger.log(Level.WARNING, "Thread interrupted", e);
            }
        }
    }
}
