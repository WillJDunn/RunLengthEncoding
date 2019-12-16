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

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;

// Using MethodOrder in this case so we can create one TransactionData object, then
// use importCSV as the first method run so the object has data which the other
// methods can operate on (so we only need to import data from CSV once)

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TransactionDataTest {
  
  static File inputFile;
  static File outputDatesFile;
  static File outputTransactionIDsFile;
  static File expectedOutputDatesFile;
  static File expectedOutputTransactionIDsFile;
  
  // In this case, use the same class for all tests so we only need to import from CSV once, sort once
  static TransactionData transactionData = new TransactionData();;

  @BeforeAll
  static void setUpBeforeClass() throws Exception {
    String fileSeparator = System.getProperty("file.separator");
    String absoluteFilePath;
    
    // Get working directory 
    String workingDirectory = System.getProperty("user.dir");
    String testDataDirectory = workingDirectory + fileSeparator + "data";
    
    // CSV file containing test data
    absoluteFilePath = testDataDirectory + fileSeparator + "SourceData.csv";
    inputFile = new File(absoluteFilePath);
    System.out.println(inputFile.getAbsolutePath());
    
    // File to which Dates will be outputed to
    absoluteFilePath = testDataDirectory + fileSeparator + "SourceDates.csv";
    outputDatesFile = new File(absoluteFilePath);
    System.out.println(outputDatesFile.getAbsolutePath());
    
    // File to which TransactionIDs will be outputed to
    absoluteFilePath = testDataDirectory + fileSeparator + "SourceTransactionIDs.csv";
    outputTransactionIDsFile = new File(absoluteFilePath);
    
    // File with the expected test results
    absoluteFilePath = testDataDirectory + fileSeparator + "SourceDates_ExpectedOutput.csv";
    expectedOutputDatesFile = new File(absoluteFilePath); 
    
    // File with the expected test results
    absoluteFilePath = testDataDirectory + fileSeparator + "SourceTransactionIDs_ExpectedOutput.csv";
    expectedOutputTransactionIDsFile = new File(absoluteFilePath); 
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

// Moved to class variable
//  @Test
//  void transactionData_Constructor_ShouldSucceed() {
//    
//  }

  @Test
  void testImportCSVFileString() {
    //fail("Not yet implemented");
  }

  @Test
  void testImportCSVFileBoolean() {
    //fail("Not yet implemented");
  }
  
  @Test
  @Order(1)
  void importCSV_ShouldSucceed() throws IOException {
    transactionData.importCSV(inputFile, ",");
  }
  
  @Test
  @Order(2)
  void testSortData() throws Exception {
    transactionData.sortData();
  }
  
//  @Test
//  void printData_ShouldSucceed() throws Exception {
//    transactionData.printData();
//  }
  
  @Test
  void getDates_Print() throws Exception {
    System.out.println("Dates: " + transactionData.getDates().toString());
  }
  
  @Test
  void getTransactionIDs_Print() throws Exception {
    System.out.println("TransactionIDs: " + transactionData.getTransactionIDs().toString());
  }
  
  @Test
  void testExportDatesToCSV() throws Exception {
    transactionData.exportDatesToCSV(outputDatesFile, ",");
    assert(sameFileContents(outputDatesFile, expectedOutputDatesFile));
  }
  
  @Test
  void testExportTransactionIDsToCSV() throws Exception {
    transactionData.exportTransactionIDsToCSV(outputTransactionIDsFile, ",");
    assert(sameFileContents(outputTransactionIDsFile, expectedOutputTransactionIDsFile));
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
