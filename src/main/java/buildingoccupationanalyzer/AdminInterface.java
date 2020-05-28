package buildingoccupationanalyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Class for admin interface, implements abstract TerminalInterface class
 */
public class AdminInterface implements TerminalInterface {
    private DataStore dataStore;
    private BufferedReader reader;
    private DataAnalyzer dataAnalyzer;

    /**
     * Constructor for AdminInterface class
     *
     * @param incomingDataStore dataStore to set in class
     * @param incomingReader    reader to set in class
     */
    public AdminInterface(DataStore incomingDataStore, BufferedReader incomingReader) {
        this.dataStore = incomingDataStore;
        this.dataAnalyzer = new DataAnalyzer(dataStore);
        this.reader = incomingReader;
    }

    /**
     * Getter for data analyzer
     *
     * @return Instances analyzer
     */
    public DataAnalyzer getDataAnalyzer() {
        return this.dataAnalyzer;
    }

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
     * @return class instance of dataStore
     */
    public DataStore getDataStore() {
        return this.dataStore;
    }

    /**
     * Setter for dataStore
     *
     * @param incomingDataStore dataStore to be set
     */
    public void setDataStore(DataStore incomingDataStore) {
        this.dataStore = incomingDataStore;
    }

    /**
     * Getter for reader
     *
     * @return class instance of reader
     */
    public BufferedReader getReader() {
        return this.reader;
    }

    /**
     * Setter for reader
     *
     * @param incomingReader reader to be set
     */
    public void setReader(BufferedReader incomingReader) {
        this.reader = incomingReader;
    }

    /**
     * Implementing abstract method to display the admin interface
     */
    public void displayInterface() {
        System.out.println("Logged in as admin");
        String userInput = "";

        while (true) {
            try {
                System.out.println("\n\nPlease enter (ls) to list all sensors, (uta) followed by the number of days to get a overall utilization report (uta 1), "
                        + "(ut) followed by a building name, room/area name, and the number of days to search over to get a utilization report (ut Reynolds 003 1), "
                        + "(import) followed by a filename (from the resources/sensorData folder) to import more data (import DataFile_2020-02-07.csv), (del) followed by the sensor name to delete a sensor (del 123abc) "
                        + " (l) to logout as a admin, or (q)uit to exit the program\n\n");
                System.out.print(">");

                userInput = this.reader.readLine();

                int returnVal = handleAdminInput(userInput);

                if (returnVal == -1) {
                    break;
                }
            } catch (IOException e) {
                Utils.handleException(e, false);
            }
        }
    }

    /**
     * Method for handling admin input
     *
     * @param userInput user input to handle
     * @return returns a integer to show if the interface should exit or not
     * @throws IOException if reader fails to close
     */
    @SuppressWarnings("checkstyle:MagicNumber")
    int handleAdminInput(String userInput) throws IOException {
        if (userInput == null) {
            return 0;
        }
        if (userInput.equals("l")) {
            System.out.println("Logging out...");
            return -1;
        } else if (userInput.split(" ")[0].equals("del")) {
            try {
                System.out.println(dataStore.removeSensor(userInput.split(" ")[1]));
                return 0;
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Invalid input, missing sensor name");
                return 1;
            }
        } else if (userInput.split(" ")[0].equals("ut")) {
            try {
                double returnVal = this.dataAnalyzer.getUtilization(userInput.split(" ")[1], userInput.split(" ")[2], Integer.parseInt(userInput.split(" ")[3]));
                DecimalFormat df = new DecimalFormat("0.00");
                if (returnVal == Constants.ERROR_DOUBLE_VALUE) {
                    System.out.println("Error occurred getting utilization, refer to above");
                } else {
                    System.out.println("Utilization of room/area " + userInput.split(" ")[2] + " in building " + userInput.split(" ")[1] + " is " + df.format(returnVal) + "%");
                    return 0;
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Invalid input, missing building, days, and or room/area name");
            }
            return 1;
        } else if (userInput.split(" ")[0].equals("uta")) {
            try {
                double returnVal = this.dataAnalyzer.getOverallUtilization(Integer.parseInt(userInput.split(" ")[1]));
                DecimalFormat df = new DecimalFormat("0.00");
                if (returnVal == Constants.ERROR_DOUBLE_VALUE) {
                    System.out.println("Error occurred getting utilization, refer to above");
                } else {
                    System.out.println("Overall utilization over the past " + userInput.split(" ")[1] + " days: " + df.format(returnVal) + "%");
                    return 0;
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Invalid input, missing number of days");
            }
            return 1;
        } else if (userInput.equals("ls")) {
            System.out.println("All Sensors:\n");
            System.out.println(dataStore.getSensors());
            return 0;
        } else if (userInput.split(" ")[0].equals("import")) {
            try {
                System.out.println("Attempting to import data from ../resources/sensorData/" + userInput.split(" ")[1]);

                dataStore.addData(userInput.split(" ")[1]);

                System.out.println("Data uploaded...");
                return 0;
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Invalid input, missing file name");
            } catch (IOException e) {
                System.out.println("Error occurred while importing new data, invalid file name");
            }
            return 1;
        } else if (userInput.equals("q")) {
            System.out.println("Bye Bye!");
            this.reader.close();
            System.exit(0);
            return 0;
        } else {
            System.out.println("Invalid input");
            return 1;
        }
    }
}
