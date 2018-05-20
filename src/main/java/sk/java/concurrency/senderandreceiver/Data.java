package sk.java.concurrency.senderandreceiver;

import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Data {
    private static final Logger logger = Logger.getLogger(Data.class.getName());

    private String packet;

    // True if receiver should wait
    // False if sender should wait
    private boolean transfer = true;

    public synchronized void send(String packet) {
        while (!transfer) {
            try {
                System.out.println("start waiting in send()");
                wait();
                System.out.println("end of waiting in send()");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.log(Level.WARNING, "Thread interrupted", e);
            }
        }
        transfer = false;

        this.packet = packet;
        System.out.println("start notifying all in send()");
        notifyAll();
        System.out.println("end notifying all in send()");
    }

    public synchronized void send1(String packet){
        if(transfer==false){
//            try {
//                wait();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }

        System.out.println("sending " + packet);
        this.packet = packet;
        transfer = false;
        notifyAll();
    }

    public synchronized String receive() {
        while (transfer) {
            try {
                System.out.println("start waiting in receive()");
                wait();
                System.out.println("end of waiting in receive()");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.log(Level.WARNING, "Thread interrupted", e);
            }
        }
        transfer = true;

        System.out.println("start notifying all in receive()");
        notifyAll();
        System.out.println("end notifying all in receive()");
        return packet;
    }

    public synchronized String receive1(){
        if(transfer){
//            try {
//                wait();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }

        transfer=true;
        notifyAll();
        System.out.println("receiving " + packet);
        return packet;
    }

    public synchronized void send2(String packet){
        System.out.println("beginning of send()");
        System.out.println("send transfer: " + transfer);
        while (!transfer){
            System.out.println("in send's while loop");
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("out send's while loop");

        }
        System.out.println("sending " + packet);
        this.packet = packet;
        transfer = false;
        System.out.println("end of send()");
    }

    public synchronized String receive2(){
        System.out.println("beginning of receive()");
        System.out.println("receive transfer:" + transfer);
        while(transfer){
            try {
                System.out.println("in receive's while loop");
                Thread.sleep(5000);
                System.out.println("out receive's while loop");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        System.out.println("receiving " + packet);
        transfer = true;
        System.out.println("end of receive()");
        return packet;
    }


    public  void send3(String packet){
        System.out.println("beginning of send()");
        System.out.println("send transfer: " + transfer);
        while (!transfer){
            System.out.println("in send's while loop");
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("out send's while loop");

        }
        System.out.println("sending " + packet);
        this.packet = packet;
        transfer = false;
        System.out.println("end of send()");
    }

    public  String receive3(){
        System.out.println("beginning of receive()");
        System.out.println("receive transfer:" + transfer);
        while(transfer){
            try {
                System.out.println("in receive's while loop");
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 5000));
                System.out.println("out receive's while loop");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        System.out.println("receiving " + packet);
        transfer = true;
        System.out.println("end of receive()");
        return packet;
    }

    public void send4(String packet){
        while(!transfer){
        }

        this.packet = packet;
        transfer = false;
    }

    public String receive4(){
        while(transfer){
        }
        transfer = true;
        return packet;
    }
}
