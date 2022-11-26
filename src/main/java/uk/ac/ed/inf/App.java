package uk.ac.ed.inf;

/**
 * Entry point for our application
 */
public class App 
{
    public static void main( String[] args )
    {
        String dayString = args[0];
        String apiBase = args[1];
        String randomSeed = args[2];
        Controller controller = new Controller(apiBase, dayString);
        controller.processDay();
        System.out.println( "Done!" );
    }
}
