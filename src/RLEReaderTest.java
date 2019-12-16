//import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RLEReaderTest {
  
  static File inputFile;
  static File outputFile;
  static File expectedOutput;
  static RLEReader rleReader;
  static int binarySearchTestValue = 20190230;
  static int binaraySearchExpectedOutput[] = {20190230, 90, 194};
  
  @BeforeAll
  static void setUpBeforeClass() throws Exception {
    String fileSeparator = System.getProperty("file.separator");
    String absoluteFilePath;

    // Get working directory 
    String workingDirectory = System.getProperty("user.dir");
    String testDataDirectory = workingDirectory + fileSeparator + "data";
    
    // Test data
    absoluteFilePath = testDataDirectory + fileSeparator + "RLEReader_printRLEFileToPlaintext_TestInput.dat";
    inputFile = new File(absoluteFilePath);
    
    // File to to be outputed to
    absoluteFilePath = testDataDirectory + fileSeparator + "RLEReader_printRLEFileToPlaintext_TestOutput.txt";
    outputFile = new File(absoluteFilePath);
    // Included file deletion in the method itself
    //Files.deleteIfExists(outputFile.toPath());
 
    // File with the expected test results
    absoluteFilePath = testDataDirectory + fileSeparator + "RLEReader_printRLEFileToPlaintext_ExpectedOutput.txt";
    expectedOutput = new File(absoluteFilePath); 
  }

  @AfterAll
  static void tearDownAfterClass() throws Exception {
  }

  @BeforeEach
  void setUp() throws Exception {
  }

  @AfterEach
  void tearDown() throws Exception {
  }
  
  /**
   * 
   */
  @Test
  void RLEReader_Construct_ShouldSucceed() {
    // Create RLEWriter object
    rleReader = new RLEReader();
  }
  
  @Test
  void printRLEFileToConsole_ShouldSucceed() throws IOException {
    rleReader.printRLEIntFileToConsole(inputFile);
  }
  
  @Test
  void printRLEFileToPlaintext_ShouldSucceed() throws IOException {
    rleReader.printRLEIntFileToPlaintext(inputFile, outputFile);
    assert(sameFileContents(outputFile, expectedOutput));
  }

  @Test
  void binarySearchFile_ShouldSucceed() throws IOException {
    assert(Arrays.equals(rleReader.binarySearchFile(20190230, inputFile), binaraySearchExpectedOutput));
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
