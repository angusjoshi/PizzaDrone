package uk.ac.ed.inf;

import org.junit.Test;

public class ControllerTest {
    @Test
    public void testController() {
        Controller controller = new Controller("2023-04-15");
        controller.processOrders();
    }
}
