
public class TransactionRow {
  public int date;
  public int transactionID;

  public TransactionRow(int date, int transactionID) {
    this.date = date;
    this.transactionID = transactionID;
  }
  
  public TransactionRow() {
    // Initialize with no data
  }
  
  public int getDate() {
    return this.date;
  }
  
  public void setDate(int date) {
    this.date = date;
  }
  
  public int getTransactionID() {
    return this.transactionID;
  }
  
  public void setTransactionID(int transactionID) {
    this.transactionID = transactionID;
  }
}
