package buildingoccupationanalyzer;

import java.io.IOException;

/**
 * Generic interface for data importer classes
 *
 * @param <T> generic data object
 */
public interface DataImporter<T> {
    /**
     * Generic function that parses files and returns a object
     *
     * @param fileName name of the file to be imported
     * @return returns a object containing the parsed data
     * @throws IOException thrown if a input exception occurs during parsing
     */
    T importData(String fileName) throws IOException;
}
