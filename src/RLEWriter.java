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
 * @author willj
 *
 */
public class RLEWriter {
  
  public RLEWriter() {
    // Instantiate
  }
  
  /**
   * 
   * @param data
   * @param outputFile
   * @param sort
   * @throws IOException
   */
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
    
    
    for (int i = 0; i < data.size(); i++) {
      int startPosition = i;
      int runLength = 1;
      
      
      // Get the run length for the data = i
      while (i+1 < data.size() && data.get(i).compareTo(data.get(i+1)) == 0) {
          runLength++;
          i++;
      }
      
      // Write the values to the file. Catch error writing to the file
      try {
        randomAccessFile.writeInt(data.get(i));
        randomAccessFile.writeInt(startPosition);
        randomAccessFile.writeInt(runLength);
      }
      catch (IOException e) {
        randomAccessFile.close();
        throw new IOException("Cannot write to file " + outputFile.getName() + ".");
      }
    }
    
    // Close the file
    randomAccessFile.close();
  }
  
  // Default sort =  true
  public void encodeInts(List<Integer> data, File outputFile) throws IOException {
    encodeInts(data, outputFile, true);
  }
  
  
  public List<RLEIntTuple> encodeInts(List<Integer> data) {
    // Throw error if input  arraylist is empty
    if (data.size() == 0) {
      throw new IllegalStateException("Cannot input arraylist cannot be empty");
    }
    
    List<RLEIntTuple> returnList = new ArrayList<RLEIntTuple>();
    
    for (int i = 0; i < data.size(); i++) {
      int startPosition = i;
      int runLength = 1;
      
      
      // Get the run length for the data = i
      while (i+1 < data.size() && data.get(i).compareTo(data.get(i+1)) == 0) {
          runLength++;
          i++;
      }
      
      RLEIntTuple tuple = new RLEIntTuple(data.get(i), startPosition, runLength);
      returnList.add(tuple);
    }
    
    return returnList;
  }
  
  
  /**
   * TODO: validation for input files containing no valid data
   * @param inputFile
   * @param outputFile
   * @param sort
   * @param csvDelimeter
   * @throws IOException
   */
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
}
