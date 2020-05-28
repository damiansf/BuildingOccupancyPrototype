package buildingoccupationanalyzer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Class for importing CSV sensor location files and parsing them
 */
public class CSVSensorLocationFileImporter implements DataImporter<HashMap<String, ArrayList<String>>> {
    /**
     * Default constructor
     */
    public CSVSensorLocationFileImporter() { }

    /**
     * Implementation of the generic import function from the interface, imports data from CSV sensor location files
     *
     * @param fileName path to the file to be imported
     * @return returns a map of buildings to a list of sensors
     * @throws IOException thrown if a input exception occurs during parsing
     */
    @Override
    public HashMap<String, ArrayList<String>> importData(String fileName) throws IOException {
        InputStream resources = this.getClass().getResourceAsStream("/" + fileName); //Get file from resources

        if (resources == null) {
            throw new FileNotFoundException("FILE NOT FOUND IN RESOURCES");
        }

        BufferedReader myReader = new BufferedReader(new InputStreamReader(resources, "UTF-8"));
        HashMap<String, ArrayList<String>> sensorLocationMap = new HashMap<>();
        String line = myReader.readLine();
        line = myReader.readLine(); //Skip first line of CSV as it is the headers

        while (line != null) { //Loop through the file till EOF
            parseLine(line, sensorLocationMap);
            line = myReader.readLine();
        }

        myReader.close();

        return sensorLocationMap;
    }

    /**
     * Private helper function for parsing CSV sensor location file lines
     *
     * @param line              a line from a file
     * @param sensorLocationMap map of buildings/floors to sensors
     */
    private void parseLine(String line, HashMap<String, ArrayList<String>> sensorLocationMap) {
        ArrayList<String> sensors = new ArrayList<>();

        String[] splitLine = line.split(Constants.CSV_TOKEN);
        String name = splitLine[0] + Constants.BUILDING_AREA_NAME_SEPARATOR + splitLine[1];
        Collections.addAll(sensors, splitLine[2].split(Constants.SENSOR_SEPARATOR_TOKEN));

        sensorLocationMap.put(name, sensors);
    }
}
