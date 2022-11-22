package uk.ac.ed.inf;
public class Controller {
    public Controller(String apiString, String currentDayString) {
        OrderValidator orderValidator = new OrderValidator(apiString, currentDayString);
        Order[] orders = orderValidator.getValidatedOrders();
        System.out.println("hehe, xd!!!!!");
    }

}
