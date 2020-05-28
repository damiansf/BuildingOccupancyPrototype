package buildingoccupationanalyzer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Class for importing CSV sensor data files
 */
public class CSVSensorDataImporter implements DataImporter<HashMap<String, ArrayList<Date>>> {
    /**
     * Default constructor
     */
    public CSVSensorDataImporter() { }

    /**
     * Implementation of the generic parse function from the interface, imports data from sensor data files
     *
     * @param fileName path to the file to be imported
     * @return returns a map of sensors with lists sensor readings
     * @throws IOException thrown if a input exception occurs during parsing
     */
    @Override
    public HashMap<String, ArrayList<Date>> importData(String fileName) throws IOException {
        InputStream resources = this.getClass().getResourceAsStream("/sensorData/" + fileName); //Get file from resources

        if (resources == null) {
            throw new FileNotFoundException("FILE NOT FOUND IN RESOURCES");
        }

        BufferedReader myReader = new BufferedReader(new InputStreamReader(resources, "UTF-8"));
        HashMap<String, ArrayList<Date>> sensorDataMap = new HashMap<>();
        String date = fileName.split(Constants.DATA_FILE_SEPARATOR)[1].replace(".csv", ""); //Get date from file name

        String line = myReader.readLine();
        line = myReader.readLine(); //Skip first line of CSV as it is the headers

        while (line != null) { //Loop till EOF
            parseLine(line, sensorDataMap, date);
            line = myReader.readLine();
        }

        myReader.close();

        return sensorDataMap;
    }

    /**
     * Private helper method that parses lines from a sensor data file
     *
     * @param line          a line from a file
     * @param sensorDataMap map of sensors to sensor responses
     * @param date          date from input file
     * @throws IOException throws this exception with a custom message when the file has a invalid date/time
     */
    private void parseLine(String line, HashMap<String, ArrayList<Date>> sensorDataMap, String date) throws IOException {
        ArrayList<Date> sensorResponses;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String[] splitLine = line.split(Constants.CSV_TOKEN);
        String sensorName = splitLine[0];

        if (sensorDataMap.get(sensorName) != null) { //Checks if the sensor already has data entries or not
            sensorResponses = sensorDataMap.get(sensorName);
        } else {
            sensorResponses = new ArrayList<>();
        }

        try { //Attempt to convert string to date and store the entry
            Date parsedDate = null;
            if (splitLine[1].split(":")[0].equals("24")) {
                parsedDate = dateFormat.parse(date + " " + "23:59:59");
            } else {
                parsedDate = dateFormat.parse(date + " " + splitLine[1]);
            }
            sensorResponses.add(parsedDate);
            sensorDataMap.put(sensorName, sensorResponses);
        } catch (ParseException e) {
            throw new IOException("DATA FILE HAS INVALID DATES/TIMES");
        }
    }
}
