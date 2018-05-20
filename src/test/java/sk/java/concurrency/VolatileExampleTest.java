package sk.java.concurrency;

import org.junit.Test;

import static org.junit.Assert.*;

public class VolatileExampleTest {

    @Test
    public void runVolatileExample() {
        VolatileExample.runVolatileExample();
    }

    @Test
    public void runNonVolatileExample() {
        VolatileExample.runNonVolatileExample();
    }
}