package buildingoccupationanalyzer;

/**
 * Java class for keeping all program constants in one place for easy modification
 */
public final class Constants {
    /**
     * Default constructor
     */
    private Constants() { }

    public static final String CSV_TOKEN = ",";
    public static final String DATA_FILE_SEPARATOR = "_";
    public static final String SENSOR_SEPARATOR_TOKEN = " ";
    public static final String CONFIG_FILE_NAME = "buildingSensorMap.csv";
    public static final String DATA_FILE_NAME = "DataFile_2020-02-08.csv";
    public static final String ADMIN_PASSWORD = "password123";
    public static final String BUILDING_AREA_NAME_SEPARATOR = "~";
    public static final String CURRENT_MOCK_DATE = "2020-02-08";
    public static final String DOOR_SENSOR_ID = "DS";
    public static final String CEILING_SENSOR_ID = "CS";
    public static final double ERROR_DOUBLE_VALUE = 200.0;
    public static final int TOTAL_MINUTES_IN_A_DAY = 1440;
    public static final int TOTAL_MINUTES_IN_HOUR = 60;
    public static final int TOTAL_MILLISECONDS_IN_A_MINUTE = 60000;
    public static final double PERCENT_COVERT_NUMBER = 100.0;
    public static final int TIME_BETWEEN_READINGS = 1;
}
