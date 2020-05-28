package buildingoccupationanalyzer;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Class for data analysis
 */
public class DataAnalyzer {
    private DataStore dataStore;

    /**
     * Default constructor
     *
     * @param incomingDataStore Incoming data store to set
     */
    public DataAnalyzer(DataStore incomingDataStore) {
        this.dataStore = incomingDataStore;
    }

    /**
     * Getter for datastore instance
     *
     * @return Datastore instance
     */
    public DataStore getDataStore() {
        return this.dataStore;
    }

    /**
     * Setter for datasore instance
     *
     * @param incomingDataStore Datastore to set
     */
    public void setDataStore(DataStore incomingDataStore) {
        this.dataStore = incomingDataStore;
    }

    /**
     * Method for getting overall building utilization
     *
     * @param numberOfDays Number of days to perform the calculation on
     * @param buildingName Building name to look into
     * @return A percentage representing the buildings utilization
     */
    double getOverallBuildingUtilization(int numberOfDays, String buildingName) {
        if (numberOfDays == 0) {
            System.err.println("Error, days must not be 0");
            return Constants.ERROR_DOUBLE_VALUE;
        }

        if (buildingName == null) {
            System.err.println("Error, building name must not be null");
            return Constants.ERROR_DOUBLE_VALUE;
        }

        double overallPercentage = 0.0;
        double returnPercentage = 0.0;
        List<String> areas = this.dataStore.getAreasAsList(buildingName);
        int roomCount = areas.size();
        DecimalFormat df = new DecimalFormat("0.00");

        for (String area : areas) {
            returnPercentage = getUtilization(buildingName, area, numberOfDays);

            if (returnPercentage == Constants.ERROR_DOUBLE_VALUE) {
                return Constants.ERROR_DOUBLE_VALUE;
            }

            System.out.println("Overall utilization for room/area " + area + " in building " + buildingName + " is: " + df.format(returnPercentage) + "%");
            overallPercentage += returnPercentage;
        }

        if (roomCount == 0.0) {
            return 0.0;
        }

        return (overallPercentage / roomCount);
    }

    /**
     * Method for getting overall utilization
     *
     * @param numberOfDays Number of days to perform the calculation on
     * @return A percentage representing overall utilization
     */
    public double getOverallUtilization(int numberOfDays) {
        if (numberOfDays == 0) {
            System.err.println("Error, days must not be 0");
            return Constants.ERROR_DOUBLE_VALUE;
        }

        double overallPercentage = 0.0;
        double returnPercentage = 0.0;
        List<String> buildings = this.dataStore.getBuildingsAsList();
        int buildingCount = buildings.size();
        DecimalFormat df = new DecimalFormat("0.00");

        for (String building : buildings) {
            returnPercentage = getOverallBuildingUtilization(numberOfDays, building);

            if (returnPercentage == Constants.ERROR_DOUBLE_VALUE) {
                return Constants.ERROR_DOUBLE_VALUE;
            }

            System.out.println("Overall utilization for building " + building + " is: " + df.format(returnPercentage) + "%\n");
            overallPercentage += returnPercentage;
        }

        if (buildingCount == 0.0) {
            return 0.0;
        }

        return (overallPercentage / buildingCount);
    }

    /**
     * Method for generating a list of valid dates
     *
     * @param numberOfDays Number of dates to make valid
     * @return List of valid dates
     */
    ArrayList<String> getValidDates(int numberOfDays) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currDate;
        String currDateKey;
        Calendar cal = Calendar.getInstance();
        ArrayList<String> validDates = new ArrayList<>();

        try {
            currDate = dateFormat.parse(Constants.CURRENT_MOCK_DATE);
            currDateKey = currDate.getDate() + "~" + currDate.getMonth() + "~" + currDate.getYear();
            validDates.add(currDateKey);
            cal.setTime(currDate);

            for (int i = 1; i < numberOfDays; i++) {
                cal.add(Calendar.DATE, -1);
                Date newDate = cal.getTime();
                currDateKey = newDate.getDate() + "~" + newDate.getMonth() + "~" + newDate.getYear();
                validDates.add(currDateKey);
            }
        } catch (ParseException e) {
            Utils.handleException(e, false);
            return null;
        }
        return validDates;
    }

    /**
     * Method for getting the utilization of a room/area
     *
     * @param buildingName The building name to look for
     * @param roomName     The room name to look for
     * @param numberOfDays Number of days to perform the calculation on
     * @return Returns a double representing the utilization
     */
    public double getUtilization(String buildingName, String roomName, int numberOfDays) {
        ArrayList<String> sensorList = this.dataStore.getSensorLocationMap().get(buildingName + Constants.BUILDING_AREA_NAME_SEPARATOR + roomName);
        HashMap<String, ArrayList<Date>> sensorDataMap = this.dataStore.getSensorDataMap();
        HashMap<String, HashMap<Integer, String>> utilization = new HashMap<>();
        double totalMinutes = numberOfDays * Constants.TOTAL_MINUTES_IN_A_DAY;
        double totalCountedMinutes = 0.0;

        if (sensorList == null) {
            System.err.println("Building name and Room name combination was not found");
            return Constants.ERROR_DOUBLE_VALUE;
        } else if (totalMinutes == 0) {
            System.err.println("Number of days must not be 0");
            return Constants.ERROR_DOUBLE_VALUE;
        } else {
            Date startTime = null;
            Date endTime = null;
            for (String sensor : sensorList) {
                for (Date date : sensorDataMap.get(sensor)) {
                    if (!sensor.contains(Constants.DOOR_SENSOR_ID)) {
                        String key = date.getDate() + "~" + date.getMonth() + "~" + date.getYear();
                        HashMap<Integer, String> minutesMap;
                        if (utilization.get(key) == null) {
                            minutesMap = new HashMap<Integer, String>();
                        } else {
                            minutesMap = utilization.get(key);
                        }
                        minutesMap.put((date.getHours() * Constants.TOTAL_MINUTES_IN_HOUR) + date.getMinutes(), null);
                        utilization.put(key, minutesMap);
                    } else {
                        if (startTime == null) {
                            startTime = date;
                        } else if (endTime == null) {
                            endTime = date;
                            String key = date.getDate() + "~" + date.getMonth() + "~" + date.getYear();
                            HashMap<Integer, String> minutesMap;
                            if (utilization.get(key) == null) {
                                minutesMap = new HashMap<Integer, String>();
                            } else {
                                minutesMap = utilization.get(key);
                            }
                            minutesMap.put((endTime.getHours() * Constants.TOTAL_MINUTES_IN_HOUR) + endTime.getMinutes(), null);
                            minutesMap.put((startTime.getHours() * Constants.TOTAL_MINUTES_IN_HOUR) + startTime.getMinutes(), null);
                            while (endTime.getTime() >= startTime.getTime()) {
                                endTime = new Date(endTime.getTime() - Constants.TOTAL_MILLISECONDS_IN_A_MINUTE);
                                minutesMap.put((endTime.getHours() * Constants.TOTAL_MINUTES_IN_HOUR) + endTime.getMinutes(), null);
                            }
                            utilization.put(key, minutesMap);
                            endTime = null;
                            startTime = null;
                        }
                    }
                }
            }
        }
        ArrayList<String> validDates = getValidDates(numberOfDays);
        if (validDates == null) {
            return Constants.ERROR_DOUBLE_VALUE;
        }
        for (String key : utilization.keySet()) {
            if (validDates.indexOf(key) != -1) {
                totalCountedMinutes += utilization.get(key).keySet().size();
            }
        }
        return (totalCountedMinutes / totalMinutes) * Constants.PERCENT_COVERT_NUMBER;
    }

    /**
     * Method for create a date object bound
     *
     * @param minusMinutes If time should be subtracted or not
     * @param addMinutes   If time should be added or not
     * @return A new date bound
     */
    Date createDateBound(Boolean minusMinutes, Boolean addMinutes) {
        LocalDateTime dateTime;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (minusMinutes) {
            dateTime = LocalDateTime.now().minusMinutes(Constants.TIME_BETWEEN_READINGS);
        } else if (addMinutes) {
            dateTime = LocalDateTime.now().plusMinutes(Constants.TIME_BETWEEN_READINGS);
        } else {
            dateTime = LocalDateTime.now();
        }

        try {
            return dateFormat.parse(Constants.CURRENT_MOCK_DATE + " " + dateTime.getHour() + ":" + dateTime.getMinute() + ":" + dateTime.getSecond());
        } catch (ParseException e) {
            Utils.handleException(e, false);
            return null;
        }

    }

    /**
     * Checks if a specified room is currently occupied
     *
     * @param buildingName Name of building to search in
     * @param roomName     Name of room/area to search in
     * @return Returns a int detailing if the room is occupied or not
     */
    public int checkOccupancy(String buildingName, String roomName) {
        ArrayList<String> sensorList = this.dataStore.getSensorLocationMap().get(buildingName + Constants.BUILDING_AREA_NAME_SEPARATOR + roomName);
        HashMap<String, ArrayList<Date>> sensorDataMap = this.dataStore.getSensorDataMap();
        Date lowerBound = createDateBound(true, false); // Define time bounds
        Date upperBound = createDateBound(false, true);
        Date currentDate = createDateBound(false, false);

        if (lowerBound == null || upperBound == null || currentDate == null) {
            return -1;
        }

        if (sensorList == null) {
            System.err.println("Building name and Room name combination was not found");
            return -1;
        } else {
            Date startTime = null;
            Date endTime = null;
            for (String sensor : sensorList) {
                for (Date date : sensorDataMap.get(sensor)) {
                    if (!sensor.contains(Constants.DOOR_SENSOR_ID)) {
                        if (date.getTime() < upperBound.getTime() && date.getTime() > lowerBound.getTime()) { // Check if time is within bounds
                            return 1;
                        }
                    } else {
                        if (startTime == null) {
                            startTime = date;
                        } else if (endTime == null) {
                            endTime = date;
                            if (currentDate.getTime() < endTime.getTime() && currentDate.getTime() > startTime.getTime()) {
                                return 1;
                            }
                            endTime = null;
                            startTime = null;
                        }
                    }
                }
            }
        }
        return 0;
    }

}
