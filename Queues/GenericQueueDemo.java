package Queues;

public class GenericQueueDemo {
  public static void main(String[] args) {
    Integer[] arr = new Integer[10];
    GenericQueue<Integer> q = new GenericQueue<>(arr);

    try {
      q.enqueue(10);
      q.enqueue(40);
      
      System.out.println("dequeued: " + q.dequeue());
      System.out.println("dequeued: " + q.dequeue());
    } catch (QueueFullException | QueueEmptyException err) {
      System.out.println(err);
    }
  }
}
