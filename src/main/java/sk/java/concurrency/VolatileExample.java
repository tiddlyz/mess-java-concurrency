package sk.java.concurrency;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Sample from : https://dzone.com/articles/java-volatile-keyword-0
 */

public class VolatileExample {
    private static final Logger logger = Logger.getLogger(VolatileExample.class.getName());

    private static volatile int VOLATILE_COUNT = 0;

    private static int NON_VOLATILE_COUNT=0;

    public static void main(String[] args) {
        runNonVolatileExample();

    }

    static void runVolatileExample(){
        new VolatileChangeListener().start();
        new VolatileChangeMaker().start();

        //* output
//        Incrementing MY_INT to 1
//        Got Change for MY_INT : 1
//        Incrementing MY_INT to 2
//        Got Change for MY_INT : 2
//        Incrementing MY_INT to 3
//        Got Change for MY_INT : 3
//        Incrementing MY_INT to 4
//        Got Change for MY_INT : 4
//        Incrementing MY_INT to 5
//        Got Change for MY_INT : 5
    }

    static void runNonVolatileExample(){
        new NonVolatileChangeListener().start();
        new NonVolatileChangeMaker().start();
        //* output
//        Incrementing MY_INT to 1
//        Incrementing MY_INT to 2
//        Incrementing MY_INT to 3
//        Incrementing MY_INT to 4
//        Incrementing MY_INT to 5
//.....And the change listener loop infinitely...
    }


    static class VolatileChangeListener extends Thread {

        @Override
        public void run() {
            int local_value = VOLATILE_COUNT;
            while ( local_value < 5){
                //* When a volatile1 variable is read, volatile1 variable is always read from main memory.
                //* So it can always read the latest(fresh) value in memory
                if( local_value!= VOLATILE_COUNT){
                    logger.log(Level.INFO,"Got Change for Volatile count : {0}", VOLATILE_COUNT);
                    local_value= VOLATILE_COUNT;
                }
            }
        }
    }

    static class VolatileChangeMaker extends Thread{
        @Override
        public void run() {
            int local_value = VOLATILE_COUNT;
            while (VOLATILE_COUNT <5){
                logger.log(Level.INFO, "Incrementing VOLATILE_COUNT to {0}", local_value+1);
                VOLATILE_COUNT = ++local_value;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) { e.printStackTrace(); }
            }
        }
    }

    static class NonVolatileChangeListener extends Thread {
        @Override
        public void run() {
            int local_value = NON_VOLATILE_COUNT;   //* NON_VOLATILE_COUNT will be cached in the thread local memory
            while ( local_value < 5){

                //* non-volatile1 variable may be read from cache or local memory which may hold old value of the variable.
                if( local_value!= NON_VOLATILE_COUNT){  //* NON_VOLATILE_COUNT is cached in the thread local memory
                    logger.log(Level.INFO,">>>Non Volatile<<< Got Change for NON_VOLATILE_COUNT : {0}", NON_VOLATILE_COUNT);
                    local_value= NON_VOLATILE_COUNT;
                }
            }
        }
    }

    static class NonVolatileChangeMaker extends Thread{
        @Override
        public void run() {
            int local_value = NON_VOLATILE_COUNT;
            while (NON_VOLATILE_COUNT <5){
                logger.log(Level.INFO, ">>>Non Volatile<<< Incrementing NON_VOLATILE_COUNT to {0}", local_value+1);
                NON_VOLATILE_COUNT = ++local_value;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) { e.printStackTrace(); }
            }
        }
    }
}
