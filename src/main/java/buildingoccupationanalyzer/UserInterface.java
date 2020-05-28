package buildingoccupationanalyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Class for user interface, implements abstract TerminalInterface class
 */
public class UserInterface implements TerminalInterface {
    private DataStore dataStore;
    private DataAnalyzer dataAnalyzer;

    /**
     * Constructor for UserInterface class
     *
     * @param incomingDataStore dataStore to use
     */
    public UserInterface(DataStore incomingDataStore) {
        this.dataStore = incomingDataStore;
        this.dataAnalyzer = new DataAnalyzer(this.dataStore);
    }

    /**
     * Getter for data analyzer
     *
     * @return Instances analyzer
     */
    public DataAnalyzer getDataAnalyzer() { return this.dataAnalyzer; }

    /**
     * Setter for data analyzer
     *
     * @param incomingAnalyzer Analyzer to be set
     */
    public void setDataAnalyzer(DataAnalyzer incomingAnalyzer) {
        this.dataAnalyzer = incomingAnalyzer;
    }

    /**
     * Getter for dataStore
     *
     * @return class dataStore instance
     */
    public DataStore getDataStore() { return this.dataStore; }

    /**
     * Setter for dataStore
     *
     * @param incomingDataStore dataStore to set
     */
    public void setDataStore(DataStore incomingDataStore) { this.dataStore = incomingDataStore; }

    /**
     * Method for displaying the user interface
     */
    public void displayInterface() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String userInput = "";

        System.out.println("\nWelcome to the building occupation analyzer");

        while (true) {
            try {
                System.out.println("\n\nPlease enter (l) to login as a administrator, "
                        + "(db) to display all buildings, (da) to display all rooms and areas, "
                        + "(oc) followed by a building and room/area to check if the room/area is occupied (oc Reynolds 003), or (q)uit to exit the program\n\n");
                System.out.print(">");

                userInput = reader.readLine();

                handleUserInput(userInput, reader);
            } catch (IOException e) {
                Utils.handleException(e, false);
            }
        }
    }

    /**
     * Implementing abstract method to handle user input
     *
     * @param userInput user input to parse
     * @param reader    input reader
     * @throws IOException thrown when reader throws error
     */
    void handleUserInput(String userInput, BufferedReader reader) throws IOException {
        if (userInput == null) {
            return;
        }
        if (userInput.equals("l")) {
            System.out.println("Please enter the admin password (Hint, password123)...");
            System.out.print(">");

            userInput = reader.readLine();

            if (userInput.equals(Constants.ADMIN_PASSWORD)) {
                System.out.println("Logging in...");
                AdminInterface adminInterface = new AdminInterface(this.dataStore, reader);
                adminInterface.displayInterface();
            } else {
                System.out.println("INVALID PASSWORD");
            }
        } else if (userInput.equals("db")) {
            System.out.println("Buildings:");
            System.out.println(dataStore.getBuildings().toString() + "\n");
        } else if (userInput.equals("da")) {
            System.out.println("Rooms/Areas:");
            System.out.println(dataStore.getAreas().toString() + "\n");
        } else if (userInput.split(" ")[0].equals("oc")) {
            try {
                int response = this.dataAnalyzer.checkOccupancy(userInput.split(" ")[1], userInput.split(" ")[2]);
                if (response == -1) {
                    System.out.println("Error while getting occupancy, refer to above");
                } else if (response == 1) {
                    System.out.println("Room/Area is occupied");
                } else {
                    System.out.println("Room/Area is not occupied");
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Invalid input, missing building and or room/area name");
            }
        } else if (userInput.equals("q")) {
            System.out.println("Bye Bye!");
            reader.close();
            System.exit(0);
        } else {
            System.out.println("Invalid input");
        }
    }
}
