// package data_structures.heaps.__tests__;

// import org.junit.jupiter.api.*;
// import org.junit.jupiter.params.ParameterizedTest;
// import org.junit.jupiter.params.provider.NullAndEmptySource;
// import org.junit.jupiter.params.provider.ValueSource;

// import static org.junit.jupiter.api.Assertions.assertAll;
// import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertFalse;
// import static org.junit.jupiter.api.Assertions.assertThrows;
// import static org.junit.jupiter.api.Assertions.assertTrue;

// import data_structures.heaps.MinHeap;
// import data_structures.heaps.exceptions.*;

// @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
// public class MinHeap_Test {
//   MinHeap<Integer> heap;
//   MinHeap<String> heap2;

//   @Nested
//   class Instantiation {

//     @Test
//     void is_instantiated() {
//       heap = new MinHeap<>(10);
//     }

//     @ParameterizedTest
//     @ValueSource(ints = { -1, 0 })
//     void throws_when_size_is_invalid(int size) {
//       assertThrows(IllegalArgumentException.class, () -> new MinHeap<>(size));
//     }

//     // @Test 
//     // void throws_when_instantiated_with_empty_array() {
//     //   Integer[] arr = {};
//     //   assertThrows(IllegalArgumentException.class, () -> {
//     //     heap = new MinHeap<>(arr);
//     //   });
//     // }

//     // @Test
//     // void instantiated_with_initial_values() {
//     //   Integer[] values = { 1, 2, 3, 4, 5 };
//     //   heap = new MinHeap<>(values);

//     //   assertAll(
//     //     () -> assertEquals(5, heap.extractMin()),
//     //     () -> assertEquals(4, heap.extractMin()),
//     //     () -> assertEquals(3, heap.extractMin()),
//     //     () -> assertEquals(2, heap.extractMin()),
//     //     () -> assertEquals(1, heap.extractMin())
//     //   );
//     // }
//   }

//   @Nested
//   class When_New {
//     int size = 5;

//     @BeforeEach
//     void create_heap() {
//       heap = new MinHeap<>(size);
//     }

//     @Test 
//     void size() {
//       assertEquals(0, heap.size());
//     }

//     @Test
//     void capacity() {
//       assertEquals(5, heap.capacity());
//     }

//     @Test
//     void is_empty() {
//       assertTrue(heap.isEmpty());
//     }

//     @Test
//     void insert() {
//       try {
//         heap.insert(1);
//       } catch (HeapFullException e) {}
      
//       try {
//         assertEquals(1, heap.extractMin());
//       } catch (HeapEmptyException e) {}
//     }

//     @Test
//     void empty_heap_string() {
//       assertEquals("{}", heap.toString());
//     }

//     @Test
//     void throws_HeapEmptyException() {
//       assertThrows(HeapEmptyException.class, () -> heap.extractMin());
//     }

//     @ParameterizedTest
//     @NullAndEmptySource
//     @ValueSource(strings = { " ", "  ", "\t", "\n" })
//     void insert_throws_on_bad_values(String key) {
//       heap2 = new MinHeap<>(size);

//       assertThrows(IllegalArgumentException.class, () -> heap2.insert(key));
//     }

//   }

//   @Nested
//   class Multiple_inserts {
//     int size = 5;

//     @BeforeEach
//     void create_and_insert() {
//       heap = new MinHeap<>(size);

//       try {
//         heap.insert(4);
//         heap.insert(2);
//         heap.insert(5);
//         heap.insert(1);
//         heap.insert(3);
//       } catch (HeapFullException e) {}
//     }

//     @Test
//     void not_empty() {
//       assertFalse(heap.isEmpty());
//     }

//     @Test
//     void all_inserts_succeed() {
//       assertAll(
//         () -> assertEquals(1, heap.extractMin()),
//         () -> assertEquals(2, heap.extractMin()),
//         () -> assertEquals(3, heap.extractMin()),
//         () -> assertEquals(4, heap.extractMin()),
//         () -> assertEquals(5, heap.extractMin())
//       );
//     }

//     @Test
//     void throws_HeapFullException() {
//       assertThrows(HeapFullException.class, () -> heap.insert(6));
//     }

//     @Test
//     void empties_and_resets() {
//       assertAll(
//         () -> assertEquals(1, heap.extractMin()),
//         () -> assertEquals(2, heap.extractMin()),
//         () -> assertEquals(3, heap.extractMin()),
//         () -> assertEquals(4, heap.extractMin()),
//         () -> assertEquals(5, heap.extractMin())
//       );
      
//       assertDoesNotThrow(() -> {
//         heap.insert(1);
//         heap.insert(2);
//         heap.insert(3);
//         heap.insert(4);
//         heap.insert(5);
//       });
//     }

//     @Test
//     void heap_constructed_from_heap() {
//       MinHeap<Integer> q = new MinHeap<>(heap);

//       assertAll(
//         () -> assertEquals(1, q.extractMin()),
//         () -> assertEquals(2, q.extractMin()),
//         () -> assertEquals(3, q.extractMin()),
//         () -> assertEquals(4, q.extractMin()),
//         () -> assertEquals(5, q.extractMin())
//       );
//     }

//     @Test
//     void to_string() {
//       assertEquals("{"
//           + "\n\"1\""  
//           + "\n\"2\""  
//           + "\n\"3\""  
//           + "\n\"4\""  
//           + "\n\"5\""
//           + "\n}", 
//         heap.toString()
//       );
//     }
  
//   }
// }