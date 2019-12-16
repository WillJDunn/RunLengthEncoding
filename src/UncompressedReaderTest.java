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

class UncompressedReaderTest {
  
  static File inputFile;
  static File outputFile;
  static File expectedOutput;
  static UncompressedReader uncompressedReader;
  static int getIntByIndexIndex = 20;
  static int getIntByIndexExpectedResult = 3021;

  @BeforeAll
  static void setUpBeforeClass() throws Exception {
    String fileSeparator = System.getProperty("file.separator");
    String absoluteFilePath;

    // Get working directory 
    String workingDirectory = System.getProperty("user.dir");
    String testDataDirectory = workingDirectory + fileSeparator + "data";
    
    // Test data
    absoluteFilePath = testDataDirectory + fileSeparator + "UncompressedReader_printIntFileToPlaintext_TestInput.dat";
    inputFile = new File(absoluteFilePath);
    
    // File to to be outputed to
    absoluteFilePath = testDataDirectory + fileSeparator + "UncompressedReader_printIntFileToPlaintext_TestOutput.txt";
    outputFile = new File(absoluteFilePath);
    // Included file deletion in the method itself
    //Files.deleteIfExists(outputFile.toPath());
 
    // File with the expected test results
    absoluteFilePath = testDataDirectory + fileSeparator + "UncompressedReader_printIntFileToPlaintext_ExpectedOutput.txt";
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

  @Test
  final void UncompressedReader_Construction() {
    uncompressedReader = new UncompressedReader();
  }

  @Test
  final void getIntByIndex_ShouldSucceed() throws IOException {
    assert(uncompressedReader.getIntByIndex(inputFile, getIntByIndexIndex) == getIntByIndexExpectedResult);
  }

  @Test
  final void printIntFileToPlaintext_ShouldSucceed() throws IOException {
    uncompressedReader.printIntFileToPlaintext(inputFile, outputFile);
    assert(sameFileContents(outputFile, expectedOutput));
  }

  @Test
  final void printUncompressedIntFileToConsole_ShouldSucceed() throws IOException {
    uncompressedReader.printUncompressedIntFileToConsole(inputFile);
  }
  
  @Test
  final void getIntsFromRange_ShouldSucceed() throws IOException {
    System.out.println(uncompressedReader.getIntsFromRange(inputFile, 5, 10).toString());
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
