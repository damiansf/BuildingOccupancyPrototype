package buildingoccupationanalyzer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.io.PrintStream;
import java.io.ByteArrayOutputStream;

import static org.junit.Assert.assertEquals;

/**
 * Class for testing the Utils class
 */
public class UtilsTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.err;

    /**
     * Method for setting up test session
     */
    @Before
    public void setup() { System.setErr(new PrintStream(outContent)); }

    /**
     * Method for restoring items modified by test session
     */
    @After
    public void restoreStreams() { System.setErr(originalOut); }

    /**
     * Tests the handleException method
     */
    @Test
    public void testHandleException() {
        Exception testException = new IndexOutOfBoundsException("test");
        Utils.handleException(testException, false);
        assertEquals("EXCEPTION OCCURRED: \n"
                + "test\n"
                + "null\n"
                + "test\n"
                + "class java.lang.IndexOutOfBoundsException\n", outContent.toString());
    }

    /**
     * Main method for UtilsTest class
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(UtilsTest.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        System.out.println(result.wasSuccessful());
    }
}
