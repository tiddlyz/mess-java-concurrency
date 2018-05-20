package sk.java.concurrency.senderandreceiver;

public class App {
    public static void main(String[] args) {
        Data data = new Data();
        Thread receiver = new Thread(new Receiver(data));
        Thread sender = new Thread(new Sender(data));

        sender.start();
        receiver.start();
    }
}
