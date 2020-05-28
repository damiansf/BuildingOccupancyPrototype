package buildingoccupationanalyzer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Class for testing the MainApplication class
 */
public class MainApplicationTest {
    private CSVSensorDataImporter dataImporter;
    private CSVSensorLocationFileImporter dataMapImporter;

    /**
     * Method for setting up test session
     */
    @Before
    public void setup() {
        dataImporter = new CSVSensorDataImporter();
        dataMapImporter = new CSVSensorLocationFileImporter();
    }

    /**
     * Tests the initializeSensorLocationMap method
     */
    @Test
    public void testInitSensorMap() {
        try {
            assertEquals(dataMapImporter.importData(Constants.CONFIG_FILE_NAME), MainApplication.initializeSensorLocationMap());
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tests the initializeSensorData method
     */
    @Test
    public void testInitSensorData() {
        try {
            assertEquals(dataImporter.importData(Constants.DATA_FILE_NAME), MainApplication.initializeSensorData());
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Main method for MainApplicationTest class
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(MainApplicationTest.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        System.out.println(result.wasSuccessful());
    }
}
