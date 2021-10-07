package HashTables.DoubleHashing;

public class DoubleHashingDemo {
  public static void main(String[] args) {
    int m = 1117;
    String[] arr = new String[m];
    DoubleHashing<String> hash = new DoubleHashing<>(arr, m);

    try {
      hash.insert(1, "test");
      hash.insert(14, "secret");
      hash.insert(934, "superb");

      String one = hash.get(1);
      String two = hash.get(14);
      String three = hash.get(943);

      System.out.println("Item 1: " + one);
      System.out.println("Item 2: " + two);
      System.out.println("Item 3: " + three);

    } catch (HashTableFullExpection | HashTableItemNotFound err) {
      System.out.println(err);
    }

  }
}
