package uk.ac.ed.inf;

/**
 * Entry point for our application
 */
public class App {
    /**
     * Runs the system
     * @param args for system to run succesfully, we require args[0] is the date, args[1] is the api string
     *              and args[2] is the random seed.
     */
    public static void main(String[] args) {
        if(args.length != 3) {
            System.err.println("Program must receive 3 arguments! exiting...");
            System.exit(2);
        }

        String dayString = args[0];
        String apiBase = args[1];

        Controller controller = new Controller(apiBase, dayString);
        controller.processDay();
        System.out.println( "Done!" );
    }
}
