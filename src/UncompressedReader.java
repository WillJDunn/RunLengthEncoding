import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class UncompressedReader {

  public UncompressedReader() {
    // TODO Auto-generated constructor stub
  }
  
  /**
   * Only supports integers
   * Returns -1 if value doesn't exist. So, doesn't really support negative integers
   * @param file
   * @param index
   * @return
   * @throws IOException
   */
  public int getIntByIndex(File file, long index) throws IOException {
    
    // File less than 4 bytes does not contain int data
    if (file.length()<4) {
      throw new IllegalStateException("File " + file.getName() + " does not contain integer data.");
    }
    
    // Index goes beyond the size of the file
    if (file.length()<index*4) {
      throw new IllegalStateException("Index " + index + " exceeds the length of file " + file.getName() + ".");
    }
    
    // Index is negative
    if (index<0) {
      throw new IllegalStateException("Index " + index + " was provided, but index cannot be negative.");
    }
    
    // Index goes beyond the size of the file
    if (file.length()<index*4) {
      throw new IllegalStateException("File " + file.getName() + " does not contain integer data.");
    }
    
    // Open file with RandomAccessFile. Catch error opening the file
    RandomAccessFile randomAccessFile;
    try {
      randomAccessFile = new RandomAccessFile(file, "rw");
    }
    catch (IOException e) {
      throw new IOException("Cannot open file " + file.getName() + " for read/write.");
    }
    
    // We initialize the value to -1, so negative values can't really be supported
    int value = -1;
    
    // Get that value of that entry from disk. Catch IO error reading the file
    try {
      // Since each entry is 4 bytes long we multiple the number of entry it is *4
      // to determine how many bytes after the beginning of the file it is located. We seek to that location then read the value
      randomAccessFile.seek(index*4);
      value = randomAccessFile.readInt();
    }
    catch (IOException e) {
      randomAccessFile.close();
      throw new IOException("Cannot read from file " + file.getName() + ".");
    }
    
    randomAccessFile.close();
    return value;
  }
  
  
  public List<Integer> getIntsFromRange(File file, long minIndex, long maxIndex) throws IOException{
    List<Integer> returnArrayList = new ArrayList<Integer>();
    
    for (long i=minIndex; i<=maxIndex; i++) {
      returnArrayList.add(getIntByIndex(file, i));
    }
    
    return returnArrayList;
  }
  
  public void printIntFileToPlaintext(File inputFile, File outputFile) throws IOException {
    
    // File less than 4 bytes does not contain int data
    if (inputFile.length()<4) {
      throw new IllegalStateException("File " + inputFile.getName() + " does not contain integer data.");
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
    
    // Iterate through input file, read the ints and print them to outputFile followed by newline
    // Divide file length (in bytes) by 4 since each int is 4 bytes
    long fileIntCount = (randomAccessFile.length()/4);
    for (long i=0; i<fileIntCount; i++) {
      writer.append(String.valueOf(randomAccessFile.readInt()) + System.lineSeparator());
    }
    
    randomAccessFile.close();
    writer.close();
  }
  
  
  public void printUncompressedIntFileToConsole(File file) throws IOException {
    
    // File less than 4 bytes does not contain int data
    if (file.length()<4) {
      throw new IllegalStateException("File " + file.getName() + " does not contain integer data.");
    }
    
    // Open file with RandomAccessFile. Catch error opening the file
    RandomAccessFile randomAccessFile;
    try {
      randomAccessFile = new RandomAccessFile(file, "rw");
    }
    catch (IOException e) {
      throw new IOException("Cannot open file " + file.getName() + " for read/write.");
    }
    
    long fileIntCount = (randomAccessFile.length()/4);
    for (long i=0; i<fileIntCount; i++) {
      System.out.println(randomAccessFile.readInt());
    }
    
    randomAccessFile.close();
  }

}
