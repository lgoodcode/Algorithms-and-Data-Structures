package Stacks;

class GenericStackDemo {
  public static void main(String args[]) {
    Integer[] intArr = new Integer[10];
    var intStack = new GenericStack<Integer>(intArr);
    String[] strArr = new String[10];
    var strStack = new GenericStack<String>(strArr);

    try {
      intStack.push(10);
      intStack.push(20);
      strStack.push("one");
      strStack.push("two");
  
      System.out.println("pop " + intStack.pop());
      System.out.println("pop " + intStack.pop());
    } catch (StackEmptyException | StackFullException err) {
      System.out.println(err);
    }
  }
}