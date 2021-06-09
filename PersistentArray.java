import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PersistentArray {

  /*
  C Vars --->
   */
  private String filePath;
  private List<Integer> internArray;
  /*
  C Vars <---
   */

  /** @return File path String */
  public String getFilePath() {
    return filePath;
  }

  /** @return int[] representation of array of this wrapper */
  public int[] getArray() {
    return internArray.stream().mapToInt(i -> i).toArray();
  }

  /**
   * Creates a persistent array using a csv file
   *
   * @param tempArray integer array to input
   * @param filePath absolute file path then file name + .csv
   * @param overwrite weather the file should be overwritten in any case or it should a throw
   *     warning
   * @throws IOException
   */
  public PersistentArray(int[] tempArray, String filePath, boolean overwrite) throws IOException {
    this.filePath = filePath;

    File testFile = new File(filePath);
    if (testFile.exists() && !overwrite) {
      throw new IOException(
          "File at path already exists, cannot overwrite it if overwrite is false");
    }

    writeToFile(tempArray, false);
    this.internArray = Arrays.stream(tempArray).boxed().collect(Collectors.toList());
  }

  /**
   * Create persistent array object form existing .csv file, format: {1,2,3,4,...}
   *
   * @param filePath absolute file path then file name + .csv
   * @throws IOException
   */
  public PersistentArray(String filePath) throws IOException {
    this.filePath = filePath;

    File testFile = new File(filePath);
    if (!testFile.exists()) {
      throw new FileNotFoundException("File at path does not exist. This cannot be forged from the void.");
    }

    String line;
    int[] numbers = new int[0];

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      line = br.readLine();
      if (line != null) {
        numbers =
            Arrays.stream(line.split(","))
                .filter(i -> !i.equals(""))
                .mapToInt(Integer::parseInt)
                .toArray();
      } else {
        System.out.println(
            "Cannot read empty CSV to Array! The void is empty, thus the array is too.");
        throw new IOException("Cannot read empty CSV to Array!");
      }
    } catch (Exception e) {
      System.out.println("An error occurred while trying to read the CSV file.");
      e.printStackTrace();
    }

    this.internArray = Arrays.stream(numbers).boxed().collect(Collectors.toList());
  }

  /**
   * Append i to array, in memory and file
   *
   * @param i appended int
   */
  public void appendToArray(int i) {
    this.internArray.add(i);
    writeToFile(new int[] {i}, true);
  }

  /**
   * Append i[] to array, in memory and file
   *
   * @param ints appended int[]
   */
  public void appendToArray(int[] ints) {
    for (int i : ints) {
      this.internArray.add(i);
    }
    writeToFile(ints, true);
  }

  /**
   * @param index Index of position
   * @return Int at this position
   */
  public int getIntAt(int index) {
    return internArray.get(index);
  }

  /**
   * @param index Where the Value should be placed
   * @param value Value to place at this array index
   * @return Value previously at this array index
   */
  public int setIntAt(int index, int value) {
    int x = internArray.set(index, value);
    writeToFile(this.getArray(), false);
    return x;
  }

  /** @return length of this array */
  public int length() {
    return internArray.size();
  }

  /**
   * Clearsa this array, on file and memory
   *
   * @param deleteEverythingIncludingInternAndFile Making sure you wanna do this
   */
  public void clear(boolean deleteEverythingIncludingInternAndFile) {
    internArray.clear();
    writeToFile(new int[] {}, false);
  }

  /**
   * Writes array to file or appends
   *
   * @param iArr appended int [] or int[] to overwrite exisitng
   * @param append weather it should append or overwrite
   */
  public void writeToFile(int[] iArr, boolean append) {
    try {
      Writer writer = new FileWriter(filePath, append);
      for (int j = 0; j < iArr.length; j++) {
        if (iArr.length == 1 && !append) {
          writer.append(String.valueOf(iArr[j]));
        } else {
          if (j == 0 && append) {
            writer.append(",");
          }
          writer.append(String.valueOf(iArr[j]));
          if (!(j == iArr.length - 1)) {
            writer.append(",");
          }
        }
      }
      writer.close();
    } catch (IOException e) {
      System.out.println(
          "An error occurred while trying to instantiate the FileWriter to this csv.");
      e.printStackTrace();
    }
  }

  /** Prints the Array line by line */
  public void printLine() {
    internArray.stream().forEach(System.out::println);
  }

  /** Prints the array as an Array [x,y...] */
  public void print() {
    System.out.println(internArray.toString());
  }

  /** @return Integer Stream, containing all values from this persistent Array */
  public Stream<Integer> getArrayStream() {
    return internArray.stream();
  }
}
