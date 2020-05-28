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
 * Class for testing the CSVSensorLocationFileImporter class
 */
public class CSVSensorLocationFileImporterTest {
    private CSVSensorLocationFileImporter dataImporter;

    /**
     * Method for setting up test session
     */
    @Before
    public void setup() { dataImporter = new CSVSensorLocationFileImporter(); }

    /**
     * Test the importData method
     */
    @Test
    public void testDataImport() {
        try {
            assertEquals("{Reynolds~001=[DS106], McLaughlin~FirstFloor=[DS108, CS104, CS105, CS106], McLaughlin~GroupStudy4=[DS101], McLaughlin~GroupStudy3=[DS102], McLaughlin~GroupStudy2=[DS103], McLaughlin~GroupStudy1=[DS104], Reynolds~002=[DS105], McLaughlin~SecondFloor=[DS109, CS101, CS102, CS103], Reynolds~003=[DS107, CS107, CS108]}", dataImporter.importData(Constants.CONFIG_FILE_NAME).toString());
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
     * Main method for CSVSensorLocationFileImporterTest class
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(CSVSensorLocationFileImporterTest.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        System.out.println(result.wasSuccessful());
    }
}
