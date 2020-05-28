package buildingoccupationanalyzer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Main class for the program
 */
public final class MainApplication {
    /**
     * Default constructor
     */
    private MainApplication() { }

    /**
     * Main method for the program, the programs entry point. Initializes the program and starts up the interactive component
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        DataStore dataStore = new DataStore(initializeSensorLocationMap(), initializeSensorData());

        UserInterface applicationInterface = new UserInterface(dataStore);

        applicationInterface.displayInterface();
    }

    /**
     * Method for initializing a sensor data map
     *
     * @return returns map of sensor data
     */
    static HashMap<String, ArrayList<Date>> initializeSensorData() {
        CSVSensorDataImporter sensorImporter = new CSVSensorDataImporter();
        HashMap<String, ArrayList<Date>> sensorDataMap = new HashMap<>();

        try {
            sensorDataMap = sensorImporter.importData(Constants.DATA_FILE_NAME);
        } catch (IOException e) {
            Utils.handleException(e, true);
        }

        return sensorDataMap;
    }

    /**
     * Method for initializing a sensor to location map
     *
     * @return a map of sensors to locations
     */
    static HashMap<String, ArrayList<String>> initializeSensorLocationMap() {
        CSVSensorLocationFileImporter configImporter = new CSVSensorLocationFileImporter();
        HashMap<String, ArrayList<String>> sensorLocationMap = new HashMap<>();

        try {
            sensorLocationMap = configImporter.importData(Constants.CONFIG_FILE_NAME);
        } catch (IOException e) {
            Utils.handleException(e, true);
        }

        return sensorLocationMap;
    }
}
