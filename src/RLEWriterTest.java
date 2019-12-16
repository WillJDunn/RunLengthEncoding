//import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * 
 */

/**
 * @author willj
 *
 */
class RLEWriterTest extends RLEWriter {
  
  static File inputFile;
  static File outputFile;
  static File expectedOutput;
  static RLEWriter rleWriter;
  
  /**
   * @throws java.lang.Exception
   */
  @BeforeAll
  static void setUpBeforeClass() throws Exception {
    String fileSeparator = System.getProperty("file.separator");
    String absoluteFilePath;
    
    // Get working directory 
    String workingDirectory = System.getProperty("user.dir");
    String testDataDirectory = workingDirectory + fileSeparator + "data";
    
    // CSV file containing test data
    absoluteFilePath = testDataDirectory + fileSeparator + "RLEWriter_encodeCSVInts_TestInput.csv";
    inputFile = new File(absoluteFilePath);
    
    // File to which RLE will be outputed to
    absoluteFilePath = testDataDirectory + fileSeparator + "RLEWriter_encodeCSVInts_TestOutput.dat";
    outputFile = new File(absoluteFilePath);
    
    // Delete the output file if it exists. Not included in the encodeCSVInts method because we want encodeCSVInts to support appending to an existing file
    Files.deleteIfExists(outputFile.toPath());
    
    // File with the expected test results
    absoluteFilePath = testDataDirectory + fileSeparator + "RLEWriter_encodeCSVInts_ExpectedOutput.dat";
    expectedOutput = new File(absoluteFilePath); 
  }

  /**
   * @throws java.lang.Exception
   */
  @AfterAll
  static void tearDownAfterClass() throws Exception {
  }

  /**
   * @throws java.lang.Exception
   */
  @BeforeEach
  void setUp() throws Exception {
  }

  /**
   * @throws java.lang.Exception
   */
  @AfterEach
  void tearDown() throws Exception {
  }
  
  
  /**
   * 
   */
  @Test
  void RLEWriter_Construct_ShouldSucceed() {
    // Create RLEWriter object
    rleWriter = new RLEWriter();
  }
  
  
  /**
   * Test method for {@link RLEWriter#encodeCSVInts(java.io.File, java.io.File, boolean, java.lang.String)}.
   */
  @Test
  void encodeCSVInts_emptyArraylist_ShouldThrowIllegalStateException() {
    List<Integer> testList = new ArrayList<Integer>();
    Assertions.assertThrows(IllegalStateException.class, () -> {
      rleWriter.encodeInts(testList, outputFile, true);
    });
  }
    
  
  /**
   * Test method for {@link RLEWriter#encodeInts(java.util.ArrayList, java.io.File, boolean)}.
   * @throws IOException 
   */
  @Test
  void encodeCSVInts_ShouldSucceed() throws IOException {
    
    rleWriter.encodeCSVInts(inputFile, outputFile, true);
    assert(sameFileContents(outputFile, expectedOutput));
  }


  
  /**
   * Utility used to test files are identical
   * 
   * @param file1
   * @param file2
   * @return
   * @throws IOException
   */
  boolean sameFileContents(File file1, File file2) throws IOException {
    if (file1.length() != file2.length())
        return false;

    if (file1.length() < 4096)
      return Arrays.equals(Files.readAllBytes(file1.toPath()), Files.readAllBytes(file2.toPath()));
    
    try (InputStream is1 = Files.newInputStream(file1.toPath());
         InputStream is2 = Files.newInputStream(file2.toPath())) {
        // Compare byte-by-byte.
        // Note that this can be sped up drastically by reading large chunks
        // (e.g. 16 KBs) but care must be taken as InputStream.read(byte[])
        // does not neccessarily read a whole array!
        int data;
        while ((data = is1.read()) != -1)
            if (data != is2.read())
                return false;
    }

    return true;
  }

}
