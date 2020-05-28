package buildingoccupationanalyzer;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Class for testing the AdminInterface class
 */
public class AdminInterfaceTest {
    private AdminInterface adminInterface;
    private DataStore dataStore;
    private BufferedReader reader;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outContentError = new ByteArrayOutputStream();
    private final PrintStream originalOutError = System.err;

    /**
     * Method for setting up test session
     */
    @Before
    public void setup() {
        dataStore = new DataStore(MainApplication.initializeSensorLocationMap(), MainApplication.initializeSensorData());
        reader = new BufferedReader(new InputStreamReader(System.in));
        adminInterface = new AdminInterface(dataStore, reader);
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
        try {
            reader.close();
        } catch (IOException e) {
        }
    }

    /**
     * Tests the getDataStore method
     */
    @Test
    public void testGetDataStore() {
        assertEquals(dataStore, adminInterface.getDataStore());
    }

    /**
     * Tests the getReader method
     */
    @Test
    public void testGetReader() {
        assertEquals(reader, adminInterface.getReader());
    }

    /**
     * Tests the setDataStore method
     */
    @Test
    public void testSetDataStore() {
        adminInterface.setDataStore(dataStore);
        assertEquals(dataStore, adminInterface.getDataStore());
    }

    /**
     * Tests the setReader method
     */
    @Test
    public void testSetReader() {
        adminInterface.setReader(reader);
        assertEquals(reader, adminInterface.getReader());
    }

    /**
     * Tests the handleAdminInput method for a logout scenario
     */
    @Test
    public void testLogout() {
        try {
            assertEquals(-1, adminInterface.handleAdminInput("l"));
        } catch (IOException e) {
            fail();
        }
        assertEquals("Logging out...\n", outContent.toString());
    }

    /**
     * Tests the handleAdminInput method for a list sensor scenario
     */
    @Test
    public void testList() {
        try {
            assertEquals(0, adminInterface.handleAdminInput("ls"));
        } catch (IOException e) {
            fail();
        }
        assertEquals("All Sensors:\n\n" + dataStore.getSensors() + "\n", outContent.toString());
    }

    /**
     * Tests the handleAdminInput method for a invalid input scenario
     */
    @Test
    public void testInvalidInput() {
        try {
            assertEquals(1, adminInterface.handleAdminInput("fffff"));
        } catch (IOException e) {
            fail();
        }
        assertEquals("Invalid input\n", outContent.toString());
    }

    /**
     * Tests the handleAdminInput method for a overall utilization call
     */
    @Test
    public void testOverallUtilization() {
        try {
            assertEquals(0, adminInterface.handleAdminInput("uta 1"));
        } catch (IOException e) {
            fail();
        }
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
                + "Overall utilization for building McLaughlin is: 32.38%\n"
                + "\n"
                + "Overall utilization over the past 1 days: 29.65%\n", outContent.toString());

    }

    /**
     * Tests the handleAdminInput method for a overall utilization call error case
     */
    @Test
    public void testOverallUtilizationError() {
        try {
            assertEquals(1, adminInterface.handleAdminInput("uta"));
        } catch (IOException e) {
            fail();
        }
        assertEquals("Invalid input, missing number of days\n", outContent.toString());

    }

    /**
     * Tests the handleAdminInput method for a overall utilization call error case invalid days
     */
    @Test
    public void testOverallUtilizationErrorDays() {
        try {
            assertEquals(1, adminInterface.handleAdminInput("uta 0"));
        } catch (IOException e) {
            fail();
        }
        assertEquals("Error, days must not be 0\n", outContentError.toString());

    }

    /**
     * Tests the handleAdminInput method for a utilization call
     */
    @Test
    public void testUtilization() {
        try {
            assertEquals(0, adminInterface.handleAdminInput("ut McLaughlin GroupStudy4 1"));
        } catch (IOException e) {
            fail();
        }
        assertEquals("Utilization of room/area GroupStudy4 in building McLaughlin is 30.14%\n", outContent.toString());

    }

    /**
     * Tests the handleAdminInput method for a invalid utilization call
     */
    @Test
    public void testInvalidUtilization() {
        try {
            assertEquals(1, adminInterface.handleAdminInput("ut "));
        } catch (IOException e) {
            fail();
        }
        assertEquals("Invalid input, missing building, days, and or room/area name\n", outContent.toString());
    }

    /**
     * Tests the handleAdminInput method for a error utilization call
     */
    @Test
    public void testErrorUtilization() {
        try {
            assertEquals(1, adminInterface.handleAdminInput("ut notreal notreal 1"));
        } catch (IOException e) {
            fail();
        }
        assertEquals("Building name and Room name combination was not found\n", outContentError.toString());
    }

    /**
     * Tests the handleAdminInput method for a error utilization call
     */
    @Test
    public void testErrorUtilizationInvalidCount() {
        try {
            assertEquals(1, adminInterface.handleAdminInput("ut McLaughlin GroupStudy4 0"));
        } catch (IOException e) {
            fail();
        }
        assertEquals("Number of days must not be 0\n", outContentError.toString());
    }

    /**
     * Tests the handleAdminInput method for a import data call
     */
    @Test
    public void testImportData() {
        try {
            assertEquals(0, adminInterface.handleAdminInput("import DataFile_2020-02-09.csv"));
        } catch (IOException e) {
            fail();
        }
        assertEquals("Attempting to import data from ../resources/sensorData/DataFile_2020-02-09.csv\nData uploaded...\n", outContent.toString());
    }

    /**
     * Tests the handleAdminInput method for a import data call without a filename
     */
    @Test
    public void testImportDataMissingName() {
        try {
            assertEquals(1, adminInterface.handleAdminInput("import "));
        } catch (IOException e) {
            fail();
        }
        assertEquals("Invalid input, missing file name\n", outContent.toString());
    }

    /**
     * Tests the handleAdminInput method for a import data call with a invalid a filename
     */
    @Test
    public void testImportDataInvalidName() {
        try {
            assertEquals(1, adminInterface.handleAdminInput("import DataFile_2020-02.csv"));
        } catch (IOException e) {
            fail();
        }
        assertEquals("Attempting to import data from ../resources/sensorData/DataFile_2020-02.csv\nError occurred while importing new data, invalid file name\n", outContent.toString());
    }

    /**
     * Tests the handleAdminInput method for a delete scenario
     */
    @Test
    public void testDelete() {
        try {
            assertEquals(0, adminInterface.handleAdminInput("del CS104"));
        } catch (IOException e) {
            fail();
        }
        assertEquals("Sensor Removed\n", outContent.toString());
    }

    /**
     * Tests the handleAdminInput method for a invalid delete scenario
     */
    @Test
    public void testInvalidDelete() {
        try {
            assertEquals(0, adminInterface.handleAdminInput("del helloworld"));
        } catch (IOException e) {
            fail();
        }
        assertEquals("Sensor Not Found\n", outContent.toString());
    }

    /**
     * Tests the handleAdminInput method for a error in delete command scenario
     */
    @Test
    public void testErrorDelete() {
        try {
            assertEquals(1, adminInterface.handleAdminInput("del"));
        } catch (IOException e) {
            fail();
        }
        assertEquals("Invalid input, missing sensor name\n", outContent.toString());
    }

    /**
     * Main method for AdminInterfaceTest class
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(AdminInterfaceTest.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        System.out.println(result.wasSuccessful());
    }


}
