import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class RLEReader {

  public RLEReader() {
    // Instantiate
  }
  
  /**
   * Performs binary search on run length encoded data
   * Presquisite: the run length encoded data must be completely sorted. This is an
   * important consideration, as run length encoded data is not necessarily sorted
   * 
   * @param target
   * @param file
   * @return
   * @throws IOException
   */
  public int[] binarySearchFile(int target, File file) throws IOException {
    
    // Since a run length encoded file entry contains 3 ints (value, start position, run length) the minimum
    // size of a valid file will be 12 bytes (3 ints * 4 bytes each)
    if (file.length()<12) {
      throw new IllegalStateException("File " + file.getName() + " does not contain run length encoded data.");
    }
    
    // Open file with RandomAccessFile. Catch error opening the file
    RandomAccessFile randomAccessFile;
    try {
      randomAccessFile = new RandomAccessFile(file, "rw");
    }
    catch (IOException e) {
      throw new IOException("Cannot open file " + file.getName() + " for read/write.");
    }
    
    // Use longs to store the byte locations in the file so we aren't limited by size (RandomAccessFile.length() returns a long)
    // Set begining to 0, end to array.length (assume we are searching the entire file)
    long begin = 0;
    
    // Each entry in the RLE encoded file contains 3 ints that are 4 bytes each, so this calculation will give the number of entries
    // the below is simplified from randomAccessFile.length()/4/3;
    long end =  randomAccessFile.length()/12;
    
    // We return an int[] triple of value, start position, run length
    int[] returnArray = new int[3];
    
    // Perform the search until begin < end (when begin<end we
    // have searched through whole list without finding it
    while (begin < end) {
      
      // Calculate middle entry of current range
      long mid = (begin+end)/2;
      
      // Get that value of that entry from disk. Catch IO error reading the file
      int value;
      try {
        // Since each entry is 12 bytes long (3 ints) we multiple the number of entry it is *12 
        // to determine how many bytes after the beginning of the file it is located. We seek to that location then read the value
        randomAccessFile.seek(mid*12);
        value = randomAccessFile.readInt();
      }
      catch (IOException e) {
        randomAccessFile.close();
        throw new IOException("Cannot read from file " + file.getName() + ".");
      }
      
      // If the middle index contains the value we are searching for collect the data into returnArray and return
      if(value == target) {
        
        // Add value to return array
        returnArray[0] = value;
        
        // Collect the start position and run length for the entry
        try {
          // Add start position to return array
          returnArray[1] = randomAccessFile.readInt();
          // Add run length to return array
          returnArray[2] = randomAccessFile.readInt();
        }
        catch (IOException e) {
          randomAccessFile.close();
          throw new IOException("Cannot read from file " + file.getName() + ".");
        }
        
        // Close file and return
        randomAccessFile.close();
        return returnArray;
      }
        
      // If the value at mid is greater than our target, search between the beginning of the list and mid (target must be below mid)
      if(value > target)
        end = mid;
      else
        // If the value at mid is less than our target, search between the mid and the end of the list and mid (target must be above mid)
        begin = mid+1;
    }
    
    // We did not find the entry, so 
    randomAccessFile.close();
    returnArray[0] = -1;
    return returnArray;
  }
  
  public List<Integer> decompressRLEIntFile(File inputFile) throws IOException {
    // Since a run length encoded file entry contains 3 ints (value, start position, run length) the minimum
    // size of a valid file will be 12 bytes (3 ints * 4 bytes each)
    if (inputFile.length()<12) {
      throw new IllegalStateException("File " + inputFile.getName() + " does not contain run length encoded data.");
    }
    
    // Open input file with RandomAccessFile. Catch error opening the file
    RandomAccessFile randomAccessFile;
    try {
      randomAccessFile = new RandomAccessFile(inputFile, "rw");
    }
    catch (IOException e) {
      throw new IOException("Cannot open file " + inputFile.getName() + " for read/write.");
    }
    
    List<Integer> returnList = new ArrayList<Integer>();
    
    
    // Iterate through input file, read the ints and print them to outputFile in the format value,startPosition,runLength + newline
    long fileRleRowCount = (randomAccessFile.length())/4/3;
    for (long i=0; i<fileRleRowCount; i++) {
      int value = randomAccessFile.readInt();
      randomAccessFile.readInt(); // startPosition not used but readInt() is required.
      int runLength = randomAccessFile.readInt();
      
      for (int j=0; j<runLength; j++)
        returnList.add(value);
    }
    
    randomAccessFile.close();
    return returnList;
  }
  
  public List<Integer> decompressRLEIntList(List<RLEIntTuple> data){
    
    // Throw error if input  arraylist is empty
    if (data.size() == 0) {
      throw new IllegalStateException("Cannot input arraylist cannot be empty");
    }
    
    List<Integer> returnList = new ArrayList<Integer>();
    
    
    // Iterate through input list, read the ints and print them to outputFile in the format value,startPosition,runLength + newline
    long rowCount = data.size();
    for (int i=0; i<rowCount; i++) {
      int value = data.get(i).getValue();
      // startPosition not used
      int runLength = data.get(i).getRunLength();
      
      for (int j=0; j<runLength; j++)
        returnList.add(value);
    }
    
    return returnList;
  }
  
  
  public void printRLEIntFileToPlaintext(File inputFile, File outputFile) throws IOException {
    
    // Since a run length encoded file entry contains 3 ints (value, start position, run length) the minimum
    // size of a valid file will be 12 bytes (3 ints * 4 bytes each)
    if (inputFile.length()<12) {
      throw new IllegalStateException("File " + inputFile.getName() + " does not contain run length encoded data.");
    }
    
    // Open input file with RandomAccessFile. Catch error opening the file
    RandomAccessFile randomAccessFile;
    try {
      randomAccessFile = new RandomAccessFile(inputFile, "rw");
    }
    catch (IOException e) {
      throw new IOException("Cannot open file " + inputFile.getName() + " for read/write.");
    }
    
    // Delete output file if exists
    Files.deleteIfExists(outputFile.toPath());
    
    // Open output file for writing
    BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile, true));
    
    // Iterate through input file, read the ints and print them to outputFile in the format value,startPosition,runLength + newline
    long fileRleRowCount = (randomAccessFile.length())/4/3;
    for (long i=0; i<fileRleRowCount; i++) {
      writer.append(String.valueOf(randomAccessFile.readInt()) + "," + String.valueOf(randomAccessFile.readInt()) + "," + String.valueOf(randomAccessFile.readInt()) + System.lineSeparator());
    }
    
    randomAccessFile.close();
    writer.close();
  }
  
  /**
   * 
   * 
   * @param file
   * @throws IOException
   */
  public void printRLEIntFileToConsole(File file) throws IOException {
    
    // Since a run length encoded file entry contains 3 ints (value, start position, run length) the minimum
    // size of a valid file will be 12 bytes (3 ints * 4 bytes each)
    if (file.length()<12) {
      throw new IllegalStateException("File " + file.getName() + " does not contain run length encoded data.");
    }
    
    // Open file with RandomAccessFile. Catch error opening the file
    RandomAccessFile randomAccessFile;
    try {
      randomAccessFile = new RandomAccessFile(file, "rw");
    }
    catch (IOException e) {
      throw new IOException("Cannot open file " + file.getName() + " for read/write.");
    }
    
    // Be sure to use longs to store file byte locations, since an int would limit the size of file (number of bytes) that can be supported
    long fileRleRowCount = (randomAccessFile.length())/4/3;
    for (long i=0; i<fileRleRowCount; i++) {
      System.out.println(randomAccessFile.readInt() + "," + randomAccessFile.readInt() + "," + randomAccessFile.readInt());
    }
    
    randomAccessFile.close();
  }
}
