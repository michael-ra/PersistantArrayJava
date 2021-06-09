import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PersistentArrayTest {

  PersistentArray testWrite;
  PersistentArray testRead;

  @BeforeEach
  @DisplayName("╯°□°）╯")
  void init() throws IOException {
    testWrite =
        new PersistentArray(
            new int[] {1, 1, 1, 3, 9, 76876},
            "",
            true);
    testRead =
        new PersistentArray("");
  }

  int[] readArrayFromFile() {
    String filePath = "";
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
    return numbers;
  }

  @org.junit.jupiter.api.Test
  void getFilePath() {
    assertNotNull(testWrite.getFilePath());
    assertNotNull(testRead.getFilePath());
    assertEquals(
        testWrite.getFilePath(),
        "");
    assertEquals(
        testRead.getFilePath(),
        "");
  }

  @org.junit.jupiter.api.Test
  void getArray() {
    assertNotNull(testRead.getArray());
    assertNotNull(testWrite.getArray());
    assertArrayEquals(testRead.getArray(), new int[] {1, 1, 1, 3, 9, 76876});
    assertArrayEquals(testWrite.getArray(), new int[] {1, 1, 1, 3, 9, 76876});
  }

  @org.junit.jupiter.api.Test
  void appendToArray() {
    testRead.appendToArray(1);
    assertArrayEquals(testRead.getArray(), new int[] {1, 1, 1, 3, 9, 76876, 1});
    assertArrayEquals(readArrayFromFile(), new int[] {1, 1, 1, 3, 9, 76876, 1});
  }

  @org.junit.jupiter.api.Test
  void AppendToArrayArray() {
    testRead.appendToArray(new int[] {1, 1});
    assertArrayEquals(testRead.getArray(), new int[] {1, 1, 1, 3, 9, 76876, 1, 1});
    assertArrayEquals(readArrayFromFile(), new int[] {1, 1, 1, 3, 9, 76876, 1, 1});
  }

  @org.junit.jupiter.api.Test
  void getIntAt() {
    assertEquals(testRead.getIntAt(1), readArrayFromFile()[1]);
    assertEquals(testRead.getIntAt(5), 76876);
  }

  @org.junit.jupiter.api.Test
  void setIntAt() {
    assertEquals(testRead.setIntAt(1, 2), 1);
    assertArrayEquals(testRead.getArray(), new int[] {1, 2, 1, 3, 9, 76876});
    assertArrayEquals(readArrayFromFile(), new int[] {1, 2, 1, 3, 9, 76876});
  }

  @org.junit.jupiter.api.Test
  void length() {
    assertEquals(testRead.length(), 6);
    assertEquals(testWrite.length(), 6);
    assertEquals(readArrayFromFile().length, 6);
  }

  @org.junit.jupiter.api.Test
  void clear() {
    testRead.clear(true);
    assertArrayEquals(testRead.getArray(), new int[] {});
    testRead.appendToArray(1);
    assertArrayEquals(testRead.getArray(), new int[] {1});
    assertArrayEquals(readArrayFromFile(), new int[] {1});
  }

  @org.junit.jupiter.api.Test
  void getArrayStream() {
    int[] a =
        Arrays.stream(testRead.getArrayStream().toArray(Integer[]::new))
            .mapToInt(Integer::intValue)
            .toArray();
    assertArrayEquals(a, new int[] {1, 1, 1, 3, 9, 76876});
  }

  @org.junit.jupiter.api.Test
  void testFileWriter() {
    testRead.writeToFile(new int[]{1}, false);
  }
}
