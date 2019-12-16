import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TransactionData {
  
  static List<TransactionRow> data;

  public TransactionData() {
    // TODO Auto-generated constructor stub
    data = new ArrayList<TransactionRow>();
  }
  
  
  
  public void importCSV(File inputFile, String csvDelimeter) throws IOException {
    
    // Arraylist for storing collected values
    data = new ArrayList<TransactionRow>();
    
    // Collect the values from the file and add to the Integer arraylist
    try (BufferedReader bufferReader = new BufferedReader(new FileReader(inputFile))) {
      String line;
      while ((line = bufferReader.readLine()) != null) {
          String[] values = line.split(csvDelimeter);
          for (int i=0; i<values.length; i = i+2)
            try {
              TransactionRow transaction = new TransactionRow(Integer.valueOf(values[i]),Integer.valueOf(values[i+1]));
              data.add(transaction);
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
  }


  // Default csvDelimeter = ","
  public void importCSV(File inputFile, boolean sort) throws IOException {
    // Default "," as the CSV delimeter
    importCSV(inputFile, ",");
  }
  
  public void printData() {
    System.out.println("Date,TransactionID");
    
    for (int i=0; i<data.size(); i++) {
      System.out.println(data.get(i).date + "," + data.get(i).transactionID);
    }
  }
  
  public List<Integer> getDates(){
    List<Integer> returnList = new ArrayList<Integer>();
    
    for (int i=0; i<data.size(); i++) {
      returnList.add(data.get(i).date);
    }
    
    return returnList;
  }
  
  public List<Integer> getTransactionIDs(){
    List<Integer> returnList = new ArrayList<Integer>();
    
    for (int i=0; i<data.size(); i++) {
      returnList.add(data.get(i).transactionID);
    }
    
    return returnList;
  }
  
  // Default csvDelimeter = ","
  public void exportDatesToCSV(File outputFile, String csvDelimeter) throws IOException {
    
    // Throw error if input arraylist is empty
    if (data.size() == 0) {
      throw new IllegalStateException("Transactions dataset is empty");
    }
    
    Files.deleteIfExists(outputFile.toPath());
    
    // Open output file for writing
    BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile, true));
    
    
    for (int i=0; i<data.size(); i++) {
      writer.append(String.valueOf(data.get(i).date));
      
      if (i != data.size()-1)
        writer.append(csvDelimeter);
    }
    
    writer.close();
  }
  
  // Default csvDelimeter = ","
  public void exportTransactionIDsToCSV(File outputFile, String csvDelimeter) throws IOException {
    
    // Throw error if input arraylist is empty
    if (data.size() == 0) {
      throw new IllegalStateException("Transactions dataset is empty");
    }
    
    Files.deleteIfExists(outputFile.toPath());
    
    // Open output file for writing
    BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile, true));
    
    
    for (int i=0; i<data.size(); i++) {
      writer.append(String.valueOf(data.get(i).transactionID));
      
      if (i != data.size()-1)
        writer.append(csvDelimeter);
    }
    
    writer.close();
  }
  
  
  public void sortData() {

    Collections.sort(data, new Comparator<TransactionRow>() {

        public int compare(TransactionRow o1, TransactionRow o2) {

            Integer x1 = ((TransactionRow) o1).date;
            Integer x2 = ((TransactionRow) o2).date;
            int dateComparison = x1.compareTo(x2);

            if (dateComparison != 0) {
               return dateComparison;
            } 

            x1 = ((TransactionRow) o1).transactionID;
            x2 = ((TransactionRow) o2).transactionID;
            return x1.compareTo(x2);
    }});
  }
}
