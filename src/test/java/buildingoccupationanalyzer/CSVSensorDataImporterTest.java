package buildingoccupationanalyzer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.TimeZone;

/**
 * Class for testing the CSVSensorDataImporter class
 */
public class CSVSensorDataImporterTest {
    private CSVSensorDataImporter dataImporter;

    /**
     * Method for setting up test session
     */
    @Before
    public void setup() { dataImporter = new CSVSensorDataImporter(); }

    /**
     * Tests the importData method
     */
    @Test
    public void testDataImport() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        try {
            assertEquals("{DS101=[Sun Feb 09 10:00:00 UTC 2020, Sun Feb 09 11:00:00 UTC 2020, Sun Feb 09 12:00:00 UTC 2020, Sun Feb 09 13:00:00 UTC 2020]}", dataImporter.importData("DataFile_2020-02-09.csv").toString());
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tests the importData method with a invalid file
     *
     * @throws IOException Should be thrown since a invalid file is passed
     */
    @Test(expected = IOException.class)
    public void testDataImportInvalidFile() throws IOException { dataImporter.importData("notAFile.txt"); }

    /**
     * Main method for CSVSensorDataImporterTest class
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(CSVSensorDataImporterTest.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        System.out.println(result.wasSuccessful());
    }
}
