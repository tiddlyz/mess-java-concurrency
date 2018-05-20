package sk.java.concurrency;

/**
 * @Origin: http://vanillajava.blogspot.co.uk/2012/01/demonstrating-when-volatile-is-required.html
 */

/**
 * The way it breaks down in the example above is that the compiler detect that the value is not changed
 * if it is the target value already and the thread is not going to set it to any other value.
 * It then assumes the check isn't required and even though both thread print the value as needing changing
 * (the opposite of the previous case) it stops changing the value.
 */
public class VolatileRequiredExample {
    static boolean value = false;

    public static void main(String[] args) {
        new Thread(new MyRunnable(true), "Sets true").start();
        new Thread(new MyRunnable(false), "Sets false").start();
    }

    private static class MyRunnable implements Runnable {
        private final boolean target;

        private MyRunnable(boolean target) {
            this.target = target;
        }

        @Override
        public void run() {
            int count = 0;
            boolean logged = false;
            while (true) {
                if (value != target) {
                    value = target;
                    count = 0;
                    if (!logged)
                        System.out.println(Thread.currentThread().getName() + ": reset value=" + value);
                } else if (++count % 1000000000 == 0) {
                    System.out.println(Thread.currentThread().getName() + ": value=" + value + " target=" + target);
                    logged = true;
                }
            }
        }
    }
}
