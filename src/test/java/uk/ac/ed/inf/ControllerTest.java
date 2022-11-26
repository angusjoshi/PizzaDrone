package uk.ac.ed.inf;

import org.junit.Test;

public class ControllerTest {
    @Test
    public void testController() {
        Controller controller = new Controller(Constants.API_BASE, "2023-04-15");
    }
    @Test
    public void testProcessDay() {
        Controller controller = new Controller(Constants.API_BASE, "2023-04-15");
        controller.processDay();
    }
}
