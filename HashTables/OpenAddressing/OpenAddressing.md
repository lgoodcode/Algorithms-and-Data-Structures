# Open Addressing Hash Tables

In open addressing, all elements occupy the hash table itself. That is, each table entry contains
either an element of the dynamic set or NIL. When searching for an element, we systematically 
examine table slots until either we find the desired element or we have ascertained that the
element is not in the table. No lists and no elements are stored outside the table, unlike in chaining.

Thus, open addressing can "fill up" so that no further insertions can be made; one consequence
is that the load factor can never exceed 1.

We could store the linked lists for chaining inside the hash table, in the otherwise unused hash-table
slots, but the advantage of open addressing is that it avoids pointers altogether. Instead of
following pointers we compute the sequence of slots to be examined. The extra memory freed by not
storing pointers provides the hash table with a larger number of slots for the same amount of memory,
potentially yielding fewer colloisions and faster retrieval. 

To perform insertion using open addressing, we successively examine, or 'probe', the hash table until
we find an empty slot in which to put the key. Instead of being fixed in the order, 0, 1, ..., m-1
(which requires (-)(n) search time), the sequence of positions probed depends upon the key being
inserted. To determine which slots to probe, we extend the hash function to include the probe 
number (starting from 0) as a second input. Thus, the hash function becomes:

`h: U x {0, 1, ..., m-1} -> {0, 1, ..., m-1}`

With open addressing, we require that for every key k, the 'Probe Sequence' 

`<h(k, 0), h(k, 1), ..., h(k, m-1)>`

be a permutation of `<0, 1, ..., m-1>` so that every hash-table position is eventually considered as a 
slot for a new key as the table fills up. 



## Hash-Insert(T, k) {#insert}

The insert procedure returns the slot number where it stores the key k or flags an error because
the hash table is already full.

```java
1   i = 0
2   repeat
3       j = h(k, i)
4       if T[j] == NIL
5           T[j] = k
6           return j
7       else i = i + 1
8   until i == m
9   error "hash table overflow"
```



## Hash-Search(T, k) {#search}

The algorithm for searching for key k probes the same sequence of slots that the insertion algorithm
examined when key k was inserted. Therefore, the search can terminate (unsuccessfully) when it finds
an empty slot, since k would have been inserted there and not later in its probe sequence.
(This argument assumes that keys are not deleted from the hash table.)

```java
1   i = 0 
2   repeat
3       j = h(k, i)
4       if T[j] == k
5           return j
6       i = i + 1
7   until T[j] == NIL or i == m
8   return NIL
```


## Hash-delete(T, k) {#delete}

To delete a key from slot i, we cannot mark that slot as empty by storing NIL in it. If we did, we might
not be able to retrieve any key k during whose insertion we had probed slot i and found it occupied. 

We can solve this problem by marking the slot, storing in it the special value DELETED instead of NIL.

We could then modify the insert procedure to treat such a slot as if it were empty so that we can insert 
a new key here. We don't need to modify the search because it will pass over DELETED values while
searching. However, by using DELETED, the search times no longer depend on the load factor, and for 
this reason chaining is more commonly selected as a collision resolution techniqeue when keys must
be deleted. 
