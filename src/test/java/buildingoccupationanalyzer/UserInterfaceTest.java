package buildingoccupationanalyzer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Class for testing the UserInterface class
 */
public class UserInterfaceTest {
    private DataStore dataStore;
    private UserInterface userInterface;
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
        userInterface = new UserInterface(dataStore);
        reader = new BufferedReader(new InputStreamReader(System.in));
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
    public void testGetDataStore() { assertEquals(dataStore, userInterface.getDataStore()); }

    /**
     * Tests the handleUserInput method for a list rooms command
     */
    @Test
    public void testListRooms() {
        try {
            userInterface.handleUserInput("da", reader);
        } catch (IOException e) {
            fail();
        }
        assertEquals("Rooms/Areas:\n\n\n"
                + "McLaughlin:\n"
                + "FirstFloor\n"
                + "GroupStudy4\n"
                + "GroupStudy3\n"
                + "GroupStudy2\n"
                + "GroupStudy1\n"
                + "SecondFloor\n\n"
                + "Reynolds:\n"
                + "001\n"
                + "002\n"
                + "003\n\n", outContent.toString());
    }

    /**
     * Tests the handleUserInput method for a list buildings command
     */
    @Test
    public void testListBuildings() {
        try {
            userInterface.handleUserInput("db", reader);
        } catch (IOException e) {
            fail();
        }
        assertEquals("Buildings:\n\n"
                + "Reynolds\n"
                + "McLaughlin\n\n", outContent.toString());
    }

    /**
     * Tests the handleUserInput method for a invalid input scenario
     */
    @Test
    public void testInvalidInput() {
        try {
            userInterface.handleUserInput("fffff", reader);
        } catch (IOException e) {
            fail();
        }
        assertEquals("Invalid input\n", outContent.toString());
    }

    /**
     * Tests the setDataStore method
     */
    @Test
    public void testSetDataStore() {
        userInterface.setDataStore(dataStore);
        assertEquals(dataStore, userInterface.getDataStore());
    }

    /**
     * Tests the setDataAnalyzer method
     */
    @Test
    public void testSetDataAnalyzer() {
        DataAnalyzer analyzer = userInterface.getDataAnalyzer();
        userInterface.setDataAnalyzer(analyzer);
        assertEquals(analyzer, userInterface.getDataAnalyzer());
    }

    /**
     * Tests the getDataAnalyzer method
     */
    @Test
    public void testGetDataAnalyzer() { assertEquals(userInterface.getDataAnalyzer(), userInterface.getDataAnalyzer()); }

    /**
     * Tests the occupancy command
     */
    @Test
    public void testOccupancy() {
        try {
            userInterface.handleUserInput("oc Reynolds 001", reader);
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tests the occupancy command invalid room path
     */
    @Test
    public void testOccupancyInvalidRoom() {
        try {
            userInterface.handleUserInput("oc Reynolds 01", reader);
        } catch (IOException e) {
            fail();
        }
        assertEquals("Building name and Room name combination was not found\n", outContentError.toString());
        assertEquals("Error while getting occupancy, refer to above\n", outContent.toString());
    }

    /**
     * Tests the occupancy command Invalid input path
     */
    @Test
    public void testOccupancyInvalidInput() {
        try {
            userInterface.handleUserInput("oc", reader);
        } catch (IOException e) {
            fail();
        }
        assertEquals("Invalid input, missing building and or room/area name\n", outContent.toString());
    }

    /**
     * Main method for UserInterfaceTest class
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(UserInterfaceTest.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        System.out.println(result.wasSuccessful());
    }
}
