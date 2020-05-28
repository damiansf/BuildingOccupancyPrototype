package buildingoccupationanalyzer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * Data storage  class that allows for modification and viewing of the programs data
 */
public class DataStore {
    private HashMap<String, ArrayList<String>> sensorLocationMap;
    private HashMap<String, ArrayList<Date>> sensorDataMap;

    /**
     * Constructor for data store class
     *
     * @param incomingSensorLocationMap map of sensors to sensor locations
     * @param incomingSensorDataMap     map of sensors to sensor data entries
     */
    public DataStore(HashMap<String, ArrayList<String>> incomingSensorLocationMap, HashMap<String, ArrayList<Date>> incomingSensorDataMap) {
        this.sensorDataMap = incomingSensorDataMap;
        this.sensorLocationMap = incomingSensorLocationMap;
    }

    /**
     * Getter for sensor location map
     *
     * @return returns sensor location map
     */
    public HashMap<String, ArrayList<String>> getSensorLocationMap() { return this.sensorLocationMap; }

    /**
     * Getter for sensor data map
     *
     * @return returns sensor data map
     */
    public HashMap<String, ArrayList<Date>> getSensorDataMap() { return this.sensorDataMap; }

    /**
     * Setter for sensor location map
     *
     * @param incomingSensorLocationMap sensor location map object to be set
     */
    public void setSensorLocationMap(HashMap<String, ArrayList<String>> incomingSensorLocationMap) { this.sensorLocationMap = incomingSensorLocationMap; }

    /**
     * Setter for sensor data map
     *
     * @param incomingSensorDataMap sensor data map object to be set
     */
    public void setSensorDataMap(HashMap<String, ArrayList<Date>> incomingSensorDataMap) { this.sensorDataMap = incomingSensorDataMap; }

    /**
     * Method that searches the data set and gathers a list of all buildings
     *
     * @return returns a StringBuilder object containing all buildings
     */
    public StringBuilder getBuildings() {
        List<String> keys = new ArrayList<>(this.sensorLocationMap.keySet());
        StringBuilder returnStr = new StringBuilder();
        List<String> buildings = new ArrayList<>(); //Keep a list to avoid duplicates

        for (String tmp : keys) {
            String buildingName = tmp.split(Constants.BUILDING_AREA_NAME_SEPARATOR)[0]; //Building name is the first portion
            if (!buildings.contains(buildingName)) {
                buildings.add(buildingName);
                returnStr.append("\n");
                returnStr.append(buildingName);
            }
        }
        return returnStr;
    }

    /**
     * Method that searches the data set and gathers a list of all buildings and returns the list
     *
     * @return returns a List containing all buildings
     */
    public List<String> getBuildingsAsList() {
        List<String> keys = new ArrayList<>(this.sensorLocationMap.keySet());
        List<String> buildings = new ArrayList<>(); //Keep a list to avoid duplicates

        for (String tmp : keys) {
            String buildingName = tmp.split(Constants.BUILDING_AREA_NAME_SEPARATOR)[0]; //Building name is the first portion
            if (!buildings.contains(buildingName)) {
                buildings.add(buildingName);
            }
        }
        return buildings;
    }

    /**
     * Method that searches the data set and gathers a list of all areas/rooms for a specific building
     *
     * @param buildingName Name of building to get areas/rooms for
     * @return returns a List object containing all areas/rooms
     */
    public List<String> getAreasAsList(String buildingName) {
        List<String> keys = new ArrayList<>(this.sensorLocationMap.keySet());
        List<String> areas = new ArrayList<>();

        for (String tmp : keys) {
            if (tmp.split(Constants.BUILDING_AREA_NAME_SEPARATOR)[0].equals(buildingName)) {
                areas.add(tmp.split(Constants.BUILDING_AREA_NAME_SEPARATOR)[1]);
            }
        }

        return areas;
    }

    /**
     * Method that searches the data set and gathers a list of all areas/rooms
     *
     * @return returns a StringBuilder object containing all areas/rooms
     */
    public StringBuilder getAreas() {
        StringBuilder returnStr = new StringBuilder();
        List<String> keys = new ArrayList<>(this.sensorLocationMap.keySet());
        Map<String, StringBuilder> buildingAreas = new HashMap<>(); //Generate a list of areas/rooms per building for nice output sake
        List<String> buildingKeys;

        for (String tmp : keys) {
            String buildingName = tmp.split(Constants.BUILDING_AREA_NAME_SEPARATOR)[0]; //Building name is the first portion
            String areaName = tmp.split(Constants.BUILDING_AREA_NAME_SEPARATOR)[1]; //Building area/room is the second portion

            if (!buildingAreas.containsKey(buildingName)) { //Check if building is already stored so that we don't overwrite data
                StringBuilder newBuilder = new StringBuilder();

                newBuilder.append("\n");
                newBuilder.append(areaName);
                buildingAreas.put(buildingName, newBuilder);
            } else {
                StringBuilder oldBuilder = buildingAreas.get(buildingName);

                oldBuilder.append("\n");
                oldBuilder.append(areaName);
                buildingAreas.put(buildingName, oldBuilder);
            }
        }

        buildingKeys = new ArrayList<>(buildingAreas.keySet());

        for (String tmp : buildingKeys) { //Combine all building area strings into one
            returnStr.append("\n\n");
            returnStr.append(tmp);
            returnStr.append(":");
            returnStr.append(buildingAreas.get(tmp).toString());
        }

        return returnStr;
    }

    /**
     * This method gathers a list of all sensors currently stored in the system
     *
     * @return returns a StringBuilder object containing all sensors
     */
    public StringBuilder getSensors() {
        StringBuilder returnStr = new StringBuilder();
        List<String> keys = new ArrayList<>(this.sensorLocationMap.keySet());

        for (String tmp : keys) {
            String buildingName = tmp.split(Constants.BUILDING_AREA_NAME_SEPARATOR)[0]; //Building name is the first portion
            String areaName = tmp.split(Constants.BUILDING_AREA_NAME_SEPARATOR)[1]; //Building area/room is the first portion

            returnStr.append("\n");
            returnStr.append("Listing sensors in building:");
            returnStr.append("\n");
            returnStr.append(buildingName);
            returnStr.append("\n");
            returnStr.append("And in the area:\n");
            returnStr.append(areaName);
            returnStr.append("\n");
            returnStr.append(sensorLocationMap.get(tmp).toString());
            returnStr.append("\n");
        }

        return returnStr;
    }

    /**
     * This method removes a specified sensor from the systems data
     *
     * @param sensorName name of sensor to remove
     * @return string stating if the sensor was removed or not
     */
    public String removeSensor(String sensorName) {
        String returnStr = "Sensor Not Found";
        List<String> buildingKeys = new ArrayList<>(this.sensorLocationMap.keySet());

        if (this.sensorDataMap.keySet().contains(sensorName)) {
            this.sensorDataMap.remove(sensorName); //Remove from sensor data 

            for (String tmp : buildingKeys) {
                if (sensorLocationMap.get(tmp).contains(sensorName)) {
                    sensorLocationMap.get(tmp).remove(sensorName); //remove from sensor map
                }
            }
            returnStr = "Sensor Removed";
        }

        return returnStr;
    }

    /**
     * This method adds data from a new file to the sensor data map
     *
     * @param fileName The import file
     * @throws IOException If a error occurs during import
     */
    public void addData(String fileName) throws IOException {
        CSVSensorDataImporter sensorImporter = new CSVSensorDataImporter();
        HashMap<String, ArrayList<Date>> newData = sensorImporter.importData(fileName);

        for (String key : newData.keySet()) {
            if (this.sensorDataMap.get(key) != null) {
                ArrayList<Date> currData = this.sensorDataMap.get(key);
                currData.addAll(newData.get(key));
                this.sensorDataMap.put(key, currData);
            } else {
                this.sensorDataMap.put(key, newData.get(key));
            }
        }
    }
}
