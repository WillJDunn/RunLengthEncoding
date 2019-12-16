import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QueryDemo {

  public QueryDemo() {
    // Auto-generated constructor stub
  }

  public static void main(String[] args) throws IOException {
    // TODO Auto-generated method stub
    
    String fileSeparator = System.getProperty("file.separator");
    String absoluteFilePath;
    
    // Get working directory 
    String workingDirectory = System.getProperty("user.dir");
    String testDataDirectory = workingDirectory + fileSeparator + "data";
    
    // Test data
    absoluteFilePath = testDataDirectory + fileSeparator + "SourceData.csv";
    File SourceData = new File(absoluteFilePath);
    
    // newline separated csv file containing test data
    absoluteFilePath = testDataDirectory + fileSeparator + "ExecuteQuery_Column1_Dates_Input.csv";
    File RLE_Column_Input = new File(absoluteFilePath);
    
    // newline separated csv file containing test data
    absoluteFilePath = testDataDirectory + fileSeparator + "ExecuteQuery_Column2_TransactionIDs_Input.csv";
    File Uncompressed_Column_Input = new File(absoluteFilePath);
    
    // RLE encoded file containing data of column1 (dates)
    absoluteFilePath = testDataDirectory + fileSeparator + "ExecuteQuery_Column1_RLE.dat";
    File Column1_Dates_RLE_Encoded_File = new File(absoluteFilePath);
    
    // Delete the file if it exists
    Files.deleteIfExists(Column1_Dates_RLE_Encoded_File.toPath());
    
    // Plaintext copy of the RLE encoded file containing data of column1 (dates) for validation purposes
    String RLEPlaintextFilePath = testDataDirectory + fileSeparator + "ExecuteQuery_Column1_RLE.txt";
    File Column1_Dates_RLE_Plaintext = new File(RLEPlaintextFilePath);
    
    // Uncompressed file containing data of column2 (transactionID)
    absoluteFilePath = testDataDirectory + fileSeparator + "ExecuteQuery_Column2_Uncompressed.dat";
    File Column2_TransactionIDs_Uncompressed_File = new File(absoluteFilePath);
    
    // Delete the file if it exists
    Files.deleteIfExists(Column2_TransactionIDs_Uncompressed_File.toPath());
    
    // Plaintext copy of the uncompressed file containing data of column2 (transactionID) for validation purposes
    String uncompressedPlaintextFilePath = testDataDirectory + fileSeparator + "ExecuteQuery_Column2_Uncompressed.txt";
    File Column2_TransactionIDs_Uncompressed_Plaintext = new File(uncompressedPlaintextFilePath);
    
    // Take the source data (SourceData.csv), sort it, export the date column to
    // RLE_Column_Input file and the transactionID column to Uncompressed_Column_Input file
    // Sort uses Java's built in Merge Sort variant which is O(nLogn)
    TransactionData transactionData = new TransactionData();
    transactionData.importCSV(SourceData, ",");
    transactionData.sortData();
    transactionData.exportDatesToCSV(RLE_Column_Input,System.lineSeparator());
    transactionData.exportTransactionIDsToCSV(Uncompressed_Column_Input,System.lineSeparator());
    
    // Encode RLE_Column_Input CSV file as RLE encoded binary file
    // DO NOT sort, we need to maintain the relationship between column1 (dates) and column2 (transactionIDs). The files are already sorted by the Date column
    // This is equivalent to SQL syntax like "INSERT INTO 'ExecuteQuery_Column1' VALUES `get CSV data`" with encoding RLE
    RLEWriter rleWriter = new RLEWriter();
    rleWriter.encodeCSVInts(RLE_Column_Input, Column1_Dates_RLE_Encoded_File, false);
    
    // Encode the Uncompressed_Column_Input CSV file to uncompressed binary file
    // DO NOT sort, we need to maintain the relationship between column1 (dates) and column2 (transactionIDs). The files are already sorted by the Date column 
    // This is equivalent to SQL syntax like "INSERT INTO 'ExecuteQuery_Column2' VALUES `get CSV data`" with encoding Uncompressed
    UncompressedWriter uncompressedWriter = new UncompressedWriter();
    uncompressedWriter.encodeCSVInts(Uncompressed_Column_Input, Column2_TransactionIDs_Uncompressed_File, false);
    
    // We now have 2 binary files:
    // ExecuteQuery_Column1_RLE.dat contains the date column (column1) data RLE encoded (run length encoded)
    // ExecuteQuery_Column2_Uncompressed.dat contains the transactionID (column2) data uncompressed binary
    
    // Lets get the data where date = 20190230 (2/30/2019)
    // Equivalent to SQL syntax like: "SELECT date, transactionID FROM ExecuteQuery WHERE date = 20190230"
    RLEReader rleReader = new RLEReader();
    UncompressedReader uncompressedReader = new UncompressedReader();
    int rleRow[] = rleReader.binarySearchFile(20190230, Column1_Dates_RLE_Encoded_File);
    
    // List results will store the result of the lookup
    List<List<Integer>> results = new ArrayList<List<Integer>>();
    // rleRow[1] contains the start position, rleRow[2] contains the run length. The value in rleRow[0] contains the data 
    for (int i=rleRow[1]; i<rleRow[1]+rleRow[2]; i++) {
      List<Integer> rowData = new ArrayList<Integer>();
      rowData.add(rleRow[0]);
      rowData.add(uncompressedReader.getIntByIndex(Column2_TransactionIDs_Uncompressed_File, i));
      results.add(rowData);
    }
    
    // Print the results to the console
    System.out.println("Output of binary search for rows with date = 20190230 (Feb 30th 2019");
    System.out.println(Arrays.deepToString(results.toArray()));
    
    // Print to plaintext files for test validation purposes
    rleReader.printRLEIntFileToPlaintext(Column1_Dates_RLE_Encoded_File, Column1_Dates_RLE_Plaintext);
    System.out.println("Plaintext version of the RLE encoded dates data file can be viewed at: " + RLEPlaintextFilePath);
    uncompressedReader.printIntFileToPlaintext(Column2_TransactionIDs_Uncompressed_File, Column2_TransactionIDs_Uncompressed_Plaintext);
    System.out.println("Plaintext version of the uncompressed transactionID data file can be viewed at: " + uncompressedPlaintextFilePath);
  }
}
