package sk.java.concurrency;

import java.util.logging.Logger;
import java.util.logging.Level;

public class VolatileVisibility{
    private static final Logger logger = Logger.getLogger(VolatileVisibility.class.getName());

    private volatile int VOLATILE_COUNT = 0;

    private int countA = 0;
    private int countB = 0;

    public void update(int a, int b, int v){
        this.countA = a;
        this.countB = b;
        this.VOLATILE_COUNT = v;
        log();
    }

    private void log(){
        logger.log(Level.INFO,"countA: {0}; countB: {1}; VOLATILE_COUNT: {2}", new Object[] {countA, countB, VOLATILE_COUNT});
    }

    static class ChangeListener extends Thread {
        VolatileVisibility vv;

        public ChangeListener(VolatileVisibility vv){
            this.vv = vv;
        }

        @Override
        public void run() {
            int local_a = vv.countA;
            int local_b = vv.countB;
            int local_c = vv.VOLATILE_COUNT;
            logger.log(Level.INFO, "local_a: {0}; local_b: {1}; local_c:{c}: {2}", new Object[]{local_a, local_b, local_c});

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) { e.printStackTrace(); }

            local_c = vv.VOLATILE_COUNT; 
            local_a = vv.countA;
            local_b = vv.countB;
            logger.log(Level.INFO, "After >>> local_a: {0}; local_b: {1}; local_c:{c}: {2}", new Object[]{local_a, local_b, local_c});
        }
    }
    
    static class ChangeMaker extends Thread{
        VolatileVisibility vv;

        public ChangeMaker(VolatileVisibility vv){
            this.vv = vv;
        }
        @Override
        public void run() {
            vv.update(1, 2, 3);  
            
//            int local_value = VOLATILE_COUNT;
//            while (VOLATILE_COUNT <5){
//                logger.log(Level.INFO, "Incrementing VOLATILE_COUNT to {0}", local_value+1);
//                VOLATILE_COUNT = ++local_value;
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) { e.printStackTrace(); }
//            }
        }
    }
}
