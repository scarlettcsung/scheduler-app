package test;

import Scheduler.Scheduler;
import junit.framework.TestCase;

public class SchedulerTest extends TestCase {

    private Scheduler scheduler;

    protected void setUp() {
        scheduler = new Scheduler();
    }

    public void testAvailableSlot() {
        assertNotNull(scheduler.findAvailableSlot("test"));
    }

    public void testAvailableSlotEqual() {
        boolean test = scheduler.findAvailableSlot("test");
        assertFalse(test);
    }

}