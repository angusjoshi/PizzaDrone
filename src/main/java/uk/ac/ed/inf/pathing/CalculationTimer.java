package uk.ac.ed.inf.pathing;

public class CalculationTimer {
    private long calculationStartTime;
    private static CalculationTimer instance;

    public static void startCalculationTimer() {
        if(instance == null) {
            instance = new CalculationTimer();
        }
        instance.calculationStartTime = System.currentTimeMillis();
    }
    private CalculationTimer() {
        calculationStartTime = System.currentTimeMillis();
    }
    public static int getTicksSinceCalculationStarted() {
        if(instance == null) {
            instance = new CalculationTimer();
        }
        return (int) (System.currentTimeMillis() - instance.calculationStartTime);
    }
}
