
public class RLEIntTuple {
  public int value;
  public int startPosition;
  public int runLength;
  
  public RLEIntTuple(int value, int startPosition, int runLength) {
    this.value = value;
    this.startPosition = startPosition;
    this.runLength = runLength;
  }
  
  public int getValue() {
    return this.value;
  }
  
  public int getStartPosition() {
    return this.startPosition;
  }
  
  public int getRunLength() {
    return this.runLength;
  }
  
  public void setValue(int value) {
    this.value = value;
  }
  
  public void setStartPosition(int startPosition) {
    this.startPosition = startPosition;
  }
  
  public void setRunLength(int runLength) {
    this.runLength = runLength;
  }
}
