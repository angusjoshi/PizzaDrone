package uk.ac.ed.inf;

import org.junit.Test;

public class ControllerTest {
    @Test
    public void testController() {
        Controller controller = new Controller();
        controller.processOrders();
    }
}
