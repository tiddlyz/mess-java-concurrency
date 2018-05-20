package sk.java.concurrency;

import java.util.concurrent.TimeUnit;

public class NoChangeRead {
    private static boolean stopReq;
    public static void main(String[] args) throws InterruptedException {
        Thread bgw = new Thread(new Runnable(){
            public void run(){
                int i = 0;
                while(!stopReq){
                    i++;
                }
                System.out.println("Escaped from loop!");
            }
        });
        bgw.start();
        TimeUnit.SECONDS.sleep(1);
        stopReq = true;
    }
}
