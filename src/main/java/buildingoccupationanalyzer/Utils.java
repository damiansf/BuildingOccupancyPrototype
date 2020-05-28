package buildingoccupationanalyzer;

/**
 * Class for defining reusable utility methods
 */
public final class Utils {
    /**
     * Default constructor
     */
    private Utils() { }

    /**
     * Utility method for handling exceptions
     *
     * @param e    an exception
     * @param exit if the program should exit or not
     */
    public static void handleException(Exception e, Boolean exit) {
        System.err.println("EXCEPTION OCCURRED: ");
        System.err.println(e.getMessage());
        System.err.println(e.getCause());
        System.err.println(e.getLocalizedMessage());
        System.err.println(e.getClass());

        if (exit) {
            System.err.println("ERROR WAS FATAL, PROGRAM EXITING");
            System.exit(0);
        }
    }
}

