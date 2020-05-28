package buildingoccupationanalyzer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Class for testing the DataAnalyzer class
 */
public class DataAnalyzerTest {
    private DataStore dataStore;
    private DataAnalyzer dataAnalyzer;
    private final ByteArrayOutputStream outContentError = new ByteArrayOutputStream();
    private final PrintStream originalOutError = System.err;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    /**
     * Method for setting up test session
     */
    @Before
    public void setup() {
        dataStore = new DataStore(MainApplication.initializeSensorLocationMap(), MainApplication.initializeSensorData());
        dataAnalyzer = new DataAnalyzer(dataStore);
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(outContentError));
    }

    /**
     * Method for restoring items modified by test session
     */
    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalOutError);
    }

    /**
     * Method for testing the setDataStore method
     */
    @Test
    public void testSetDataStore() {
        this.dataAnalyzer.setDataStore(this.dataStore);
        assertEquals(this.dataStore, this.dataAnalyzer.getDataStore());
    }

    /**
     * Method for testing the getDataStore method
     */
    @Test
    public void testGetDataStore() {
        assertEquals(this.dataStore, this.dataAnalyzer.getDataStore());
    }

    /**
     * Test createDateBound method
     */
    @Test
    public void testCreateDateBound() {
        LocalDateTime dateTime = LocalDateTime.now();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            assertEquals(dateFormat.parse(Constants.CURRENT_MOCK_DATE + " " + dateTime.getHour() + ":" + dateTime.getMinute() + ":" + dateTime.getSecond()), this.dataAnalyzer.createDateBound(false, false));
        } catch (ParseException e) {
            fail();
        }
    }

    /**
     * Test createDateBound method
     */
    @Test
    public void testCreateDateBoundMinus() {
        LocalDateTime dateTime = LocalDateTime.now().minusMinutes(Constants.TIME_BETWEEN_READINGS);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            assertEquals(dateFormat.parse(Constants.CURRENT_MOCK_DATE + " " + dateTime.getHour() + ":" + dateTime.getMinute() + ":" + dateTime.getSecond()), this.dataAnalyzer.createDateBound(true, false));
        } catch (ParseException e) {
            fail();
        }
    }

    /**
     * Test getValidDates method
     */
    @Test
    public void testGetValidDates() {
        ArrayList<String> validDates = new ArrayList<>();
        validDates.add("8~1~120");
        validDates.add("7~1~120");

        assertEquals(validDates.toString(), this.dataAnalyzer.getValidDates(2).toString());
    }

    /**
     * Test createDateBound method
     */
    @Test
    public void testCreateDateBoundPlus() {
        LocalDateTime dateTime = LocalDateTime.now().plusMinutes(Constants.TIME_BETWEEN_READINGS);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            assertEquals(dateFormat.parse(Constants.CURRENT_MOCK_DATE + " " + dateTime.getHour() + ":" + dateTime.getMinute() + ":" + dateTime.getSecond()), this.dataAnalyzer.createDateBound(false, true));
        } catch (ParseException e) {
            fail();
        }
    }

    /**
     * Tests the checkOccupancy method happy path
     */
    @Test
    public void testCheckOccupancy() {
        dataAnalyzer.checkOccupancy("Reynolds", "003");
        assertEquals("", outContentError.toString());
    }

    /**
     * Tests the checkOccupancy method error path
     */
    @Test
    public void testCheckOccupancyInvalidRoom() {
        dataAnalyzer.checkOccupancy("Reynolds", "03");
        assertEquals("Building name and Room name combination was not found\n", outContentError.toString());
    }

    /**
     * Tests the checkOccupancy method happy path with door sensors
     */
    @Test
    public void testCheckOccupancyDoorSensorRoom() {
        dataAnalyzer.checkOccupancy("Reynolds", "002");
        assertEquals("", outContentError.toString());
    }

    /**
     * Tests the getUtilization method happy path
     */
    @Test
    public void testGetUtilization() {
        assertEquals(37.77777777777778, dataAnalyzer.getUtilization("Reynolds", "003", 1), 0);
        assertEquals("", outContentError.toString());
    }

    /**
     * Tests the getUtilization method error building name/room or area name
     */
    @Test
    public void testGetUtilizationErrorName() {
        assertEquals(Constants.ERROR_DOUBLE_VALUE, dataAnalyzer.getUtilization("Reynoolds", "003", 1), 0);
        assertEquals("Building name and Room name combination was not found\n", outContentError.toString());
    }

    /**
     * Tests the getUtilization method error in days
     */
    @Test
    public void testGetUtilizationErrorDay() {
        assertEquals(Constants.ERROR_DOUBLE_VALUE, dataAnalyzer.getUtilization("Reynolds", "003", 0), 0);
        assertEquals("Number of days must not be 0\n", outContentError.toString());
    }

    /**
     * Tests the getOverallUtilization method in a happy path
     */
    @Test
    public void testGetOverallUtilization() {
        assertEquals(29.65277777777778, dataAnalyzer.getOverallUtilization(1), 0);
        assertEquals("Overall utilization for room/area 001 in building Reynolds is: 21.46%\n"
                + "Overall utilization for room/area 002 in building Reynolds is: 21.53%\n"
                + "Overall utilization for room/area 003 in building Reynolds is: 37.78%\n"
                + "Overall utilization for building Reynolds is: 26.92%\n"
                + "\n"
                + "Overall utilization for room/area FirstFloor in building McLaughlin is: 66.74%\n"
                + "Overall utilization for room/area GroupStudy4 in building McLaughlin is: 30.14%\n"
                + "Overall utilization for room/area GroupStudy3 in building McLaughlin is: 21.53%\n"
                + "Overall utilization for room/area GroupStudy2 in building McLaughlin is: 8.61%\n"
                + "Overall utilization for room/area GroupStudy1 in building McLaughlin is: 12.92%\n"
                + "Overall utilization for room/area SecondFloor in building McLaughlin is: 54.37%\n"
                + "Overall utilization for building McLaughlin is: 32.38%\n\n", outContent.toString());
    }

    /**
     * Tests the getOverallUtilization method in a error case
     */
    @Test
    public void testGetOverallUtilizationErrorDay() {
        assertEquals(Constants.ERROR_DOUBLE_VALUE, dataAnalyzer.getOverallUtilization(0), 0);
        assertEquals("Error, days must not be 0\n", outContentError.toString());
    }

    /**
     * Tests the getBuildingUtilization method in a happy path
     */
    @Test
    public void testGetBuildingUtilization() {
        assertEquals(26.921296296296294, dataAnalyzer.getOverallBuildingUtilization(1, "Reynolds"), 0);
        assertEquals("Overall utilization for room/area 001 in building Reynolds is: 21.46%\n"
                + "Overall utilization for room/area 002 in building Reynolds is: 21.53%\n"
                + "Overall utilization for room/area 003 in building Reynolds is: 37.78%\n", outContent.toString());
    }

    /**
     * Tests the getBuildingUtilization method in a error case
     */
    @Test
    public void testGetBuildingUtilizationErrorDay() {
        assertEquals(Constants.ERROR_DOUBLE_VALUE, dataAnalyzer.getOverallBuildingUtilization(0, "Reynolds"), 0);
        assertEquals("Error, days must not be 0\n", outContentError.toString());
    }

    /**
     * Tests the getBuildingUtilization method in a error case
     */
    @Test
    public void testGetBuildingUtilizationErrorBuildingName() {
        assertEquals(Constants.ERROR_DOUBLE_VALUE, dataAnalyzer.getOverallBuildingUtilization(1, null), 0);
        assertEquals("Error, building name must not be null\n", outContentError.toString());
    }

    /**
     * Main method for DataAnalyzerTest class
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(DataAnalyzerTest.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        System.out.println(result.wasSuccessful());
    }
}
