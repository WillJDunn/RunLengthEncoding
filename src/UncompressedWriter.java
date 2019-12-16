import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 
 */

/**
 * @author willj
 *
 */
public class UncompressedWriter {
  
  public UncompressedWriter() {
    // Instantiate
  }
  
  public void encodeInts(List<Integer> data, File outputFile, boolean sort) throws IOException {
    
    // Throw error if input  arraylist is empty
    if (data.size() == 0) {
      throw new IllegalStateException("Cannot input arraylist cannot be empty");
    }
    
    // Sort the list (built in method, uses modified merge-sort O(nlogn)) if requested
    if (sort)
      data.sort(Comparator.naturalOrder());
    
    // Open file with RandomAccessFile. Catch error opening the file
    RandomAccessFile randomAccessFile;
    try {
      randomAccessFile = new RandomAccessFile(outputFile, "rw");
    }
    catch (IOException e) {
      throw new IOException("Cannot open file " + outputFile.getName() + " for read/write.");
    }
    
    // Iterate through the list and write the values to the file
    for (int i = 0; i < data.size(); i++) {
      // Write the values to the file. Catch error writing to the file
      try {
        randomAccessFile.writeInt(data.get(i));
      }
      catch (IOException e) {
        randomAccessFile.close();
        throw new IOException("Cannot write to file " + outputFile.getName() + ".");
      }
    }
  
  // Close the file
  randomAccessFile.close();
  }
  
  // Default sort to false
  public void encodeInts(List<Integer> data, File outputFile) throws IOException {
    encodeInts(data, outputFile, false);
  }
  
  
  public void encodeCSVInts(File inputFile, File outputFile, boolean sort, String csvDelimeter) throws IOException {
    
    // Arraylist for storing collected values
    List<Integer> data = new ArrayList<Integer>();
    
    // Collect the values from the file and add to the Integer arraylist
    try (BufferedReader bufferReader = new BufferedReader(new FileReader(inputFile))) {
      String line;
      while ((line = bufferReader.readLine()) != null) {
          String[] values = line.split(csvDelimeter);
          for (int i=0; i<values.length; i++)
            try {
            data.add(Integer.valueOf(values[i]));
            }
            catch (NumberFormatException e){
              throw new NumberFormatException("CSV file " + inputFile.getName() + " contains data that cannot be parsed as an integer.");
            }
      }
    }
    catch (FileNotFoundException e) {
      // Although FileReader throws a FileNotFoundException, we throw an IOException for consistency
      throw new IOException("FileNotFoundException: Cannot read file " + inputFile.getName() + ".");
    }
    
    // Pass the collected values to encodeInts
    encodeInts(data, outputFile, sort);
  }


  // Default csvDelimeter = ","
  public void encodeCSVInts(File inputFile, File outputFile, boolean sort) throws IOException {
    // Default "," as the CSV delimeter
    encodeCSVInts(inputFile, outputFile, sort, ",");
  }
  
  // Default csvDelimeter = ","
  // Default sort = false
  public void encodeCSVInts(File inputFile, File outputFile) throws IOException {
    // Default "," as the CSV delimeter
    encodeCSVInts(inputFile, outputFile, false, ",");
  }
}
