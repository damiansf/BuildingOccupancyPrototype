package buildingoccupationanalyzer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Class for testing the DataStore class
 */
public class DataStoreTest {
    private DataStore dataStore;
    private CSVSensorDataImporter dataImporter;
    private CSVSensorLocationFileImporter dataMapImporter;

    /**
     * Method for setting up test session
     */
    @Before
    public void setup() {
        dataImporter = new CSVSensorDataImporter();
        dataMapImporter = new CSVSensorLocationFileImporter();
        dataStore = new DataStore(MainApplication.initializeSensorLocationMap(), MainApplication.initializeSensorData());
    }

    /**
     * Tests the getSensorLocationMap method
     */
    @Test
    public void testGetSensorLocationMap() {
        try {
            assertEquals(dataMapImporter.importData(Constants.CONFIG_FILE_NAME), dataStore.getSensorLocationMap());
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tests the GetSensorDataMap method
     */
    @Test
    public void testGetSensorDataMap() {
        try {
            assertEquals(dataImporter.importData(Constants.DATA_FILE_NAME), dataStore.getSensorDataMap());
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tests the setSensorLocationMap method
     */
    @Test
    public void testSetSensorLocationMap() {
        dataStore.setSensorLocationMap(MainApplication.initializeSensorLocationMap());
        assertEquals(MainApplication.initializeSensorLocationMap().toString(), dataStore.getSensorLocationMap().toString());
    }

    /**
     * Tests the setSensorDataMap method
     */
    @Test
    public void testSetSensorDataMap() {
        dataStore.setSensorDataMap(MainApplication.initializeSensorData());
        assertEquals(MainApplication.initializeSensorData().toString(), dataStore.getSensorDataMap().toString());
    }

    /**
     * Tests the getBuildings method
     */
    @Test
    public void testGetBuildings() {
        assertEquals("\nReynolds\n" + "McLaughlin", dataStore.getBuildings().toString());
    }

    /**
     * Tests the getAreas method
     */
    @Test
    public void testGetAreas() {
        assertEquals("\n\nMcLaughlin:\n"
                + "FirstFloor\n"
                + "GroupStudy4\n"
                + "GroupStudy3\n"
                + "GroupStudy2\n"
                + "GroupStudy1\n"
                + "SecondFloor\n\n"
                + "Reynolds:\n"
                + "001\n"
                + "002\n"
                + "003", dataStore.getAreas().toString());
    }

    /**
     * Tests the getBuildingsAsList method
     */
    @Test
    public void testGetBuildingsAsList() {
        List<String> expectedList = new ArrayList<>();
        expectedList.add("Reynolds");
        expectedList.add("McLaughlin");
        assertEquals(expectedList.toString(), dataStore.getBuildingsAsList().toString());
    }

    /**
     * Tests the getAreasAsList method
     */
    @Test
    public void testGetAreasAsList() {
        List<String> expectedList = new ArrayList<>();
        expectedList.add("001");
        expectedList.add("002");
        expectedList.add("003");
        assertEquals(expectedList.toString(), dataStore.getAreasAsList("Reynolds").toString());
    }

    /**
     * Tests the getSensors method
     */
    @Test
    public void testGetSensors() {
        assertEquals("\nListing sensors in building:\n"
                + "Reynolds\n"
                + "And in the area:\n"
                + "001\n"
                + "[DS106]\n"
                + "\n"
                + "Listing sensors in building:\n"
                + "McLaughlin\n"
                + "And in the area:\n"
                + "FirstFloor\n"
                + "[DS108, CS104, CS105, CS106]\n"
                + "\n"
                + "Listing sensors in building:\n"
                + "McLaughlin\n"
                + "And in the area:\n"
                + "GroupStudy4\n"
                + "[DS101]\n"
                + "\n"
                + "Listing sensors in building:\n"
                + "McLaughlin\n"
                + "And in the area:\n"
                + "GroupStudy3\n"
                + "[DS102]\n"
                + "\n"
                + "Listing sensors in building:\n"
                + "McLaughlin\n"
                + "And in the area:\n"
                + "GroupStudy2\n"
                + "[DS103]\n"
                + "\n"
                + "Listing sensors in building:\n"
                + "McLaughlin\n"
                + "And in the area:\n"
                + "GroupStudy1\n"
                + "[DS104]\n"
                + "\n"
                + "Listing sensors in building:\n"
                + "Reynolds\n"
                + "And in the area:\n"
                + "002\n"
                + "[DS105]\n"
                + "\n"
                + "Listing sensors in building:\n"
                + "McLaughlin\n"
                + "And in the area:\n"
                + "SecondFloor\n"
                + "[DS109, CS101, CS102, CS103]\n"
                + "\n"
                + "Listing sensors in building:\n"
                + "Reynolds\n"
                + "And in the area:\n"
                + "003\n"
                + "[DS107, CS107, CS108]\n", dataStore.getSensors().toString());
    }

    /**
     * Tests the addData method in a error scenario
     *
     * @throws IOException Should be thrown by the test to pass
     */
    @Test(expected = IOException.class)
    public void testAddDataErrorFile() throws IOException {
        dataStore.addData("InvalidFileName");
    }

    /**
     * Tests the removeSensor method
     */
    @Test
    public void testRemoveSensor() {
        assertEquals("Sensor Removed", dataStore.removeSensor("CS104"));
    }

    /**
     * Tests the removeSensor method in a error scenario
     */
    @Test
    public void testRemoveSensorError() {
        assertEquals("Sensor Not Found", dataStore.removeSensor("I Do Not Exist"));
    }

    /**
     * Tests the addData method
     */
    @Test
    public void testAddData() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        try {
            HashMap<String, ArrayList<Date>> newSensorDataMap = new HashMap<>();
            dataStore.setSensorDataMap(newSensorDataMap);
            dataStore.addData("DataFile_2020-02-09.csv");
            assertEquals("{DS101=[Sun Feb 09 10:00:00 UTC 2020, Sun Feb 09 11:00:00 UTC 2020, Sun Feb 09 12:00:00 UTC 2020, Sun Feb 09 13:00:00 UTC 2020]}", dataStore.getSensorDataMap().toString());
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Main method for DataStoreTest class
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(DataStoreTest.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        System.out.println(result.wasSuccessful());
    }
}
