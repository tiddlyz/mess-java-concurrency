package sk.java.concurrency.senderandreceiver;

import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Receiver implements Runnable{
    private static final Logger logger = Logger.getLogger(Sender.class.getName());
    private Data load;

    public Receiver(Data load) {
        this.load = load;
    }

    // standard constructors
    public void run() {
        for(String receivedMessage = load.receive3();
            !"End".equals(receivedMessage);
            receivedMessage = load.receive3()) {


            System.out.println("received packet: " + receivedMessage);

            // ...
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 5000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.log(Level.WARNING, "Thread interrupted", e);
            }
        }
    }
}
