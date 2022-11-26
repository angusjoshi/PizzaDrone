package uk.ac.ed.inf.pathing;

/**
 * A singleton class to be used for timing the pathfinding process.
 */
public class CalculationTimer {
    private long calculationStartTime;
    private static CalculationTimer instance;

    /**
     * Start the calculation timer. To be used at the start of the pathfinding process.
     */
    public static void startCalculationTimer() {
        if(instance == null) {
            instance = new CalculationTimer();
        }
        instance.calculationStartTime = System.currentTimeMillis();
    }
    private CalculationTimer() {
        calculationStartTime = System.currentTimeMillis();
    }

    /**
     * Gets the number of milliseconds passed since the timer was started.
     * @return the number of milliseconds passed
     */
    public static int getTicksSinceCalculationStarted() {
        if(instance == null) {
            instance = new CalculationTimer();
        }
        return (int) (System.currentTimeMillis() - instance.calculationStartTime);
    }
}
