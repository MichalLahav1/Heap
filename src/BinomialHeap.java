import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * BinomialHeap
 *
 * An implementation of binomial heap over non-negative integers.
 * Based on exercise from previous semester.
 */
public class BinomialHeap
{
	public int size;
	public HeapNode last;
	public HeapNode min;
	
	public BinomialHeap() {
		this(0, null, null);
	}
	
	public BinomialHeap(int size, HeapNode last, HeapNode min) {
		this.size = size;
		this.last = last;
		this.min = min;
	}

	/**
	 * 
	 * pre: key > 0
	 *
	 * Insert (key,info) into the heap and return the newly generated HeapItem.
	 *
	 */
	public HeapItem insert(int key, String info) 
	{    		
		HeapNode newNode = new HeapNode(null, null, null, null, 0);
		HeapItem newItem = new HeapItem(newNode, key, info);
		newNode.item = newItem;
		newNode.next = newNode;
		BinomialHeap newHeap = new BinomialHeap(1, newNode, newNode);
		this.meld(newHeap);
		return newItem;
	}

	/**
	 * 
	 * Delete the minimal item
	 *
	 */
	public void deleteMin()
	{
		HeapNode y = this.min;
		BinomialHeap childrenOfMin = new BinomialHeap();
		int childrenSize = 0;
		if (y.child != null) {
			childrenSize = (int) Math.pow(2, y.rank) - 1;
			HeapNode x = y.child;
			x.parent = null;
			HeapNode min = x;
			while (x.next != y.child) {
				x = x.next;
				x.parent = null;
				if (x.item.key < min.item.key) {
					min = x;
				}
			}
			childrenOfMin = new BinomialHeap(childrenSize, x.next, min);
		}
		HeapNode z = y.next;
		if (z == y) {
			this.last = childrenOfMin.last;
			this.min = childrenOfMin.min;
			this.size = childrenOfMin.size;
			return;
		}
		HeapNode min2 = z;
		while(z.next != y) {
			z = z.next;
			if (z.item.key < min2.item.key) {
				min2 = z;
			}
		}
		if (this.last == y) {
			this.last = z;
		}
		z.next = y.next;		
		this.size -= (childrenSize + 1);
		this.min = min2;
		this.meld(childrenOfMin);
	}

	/**
	 * 
	 * Return the minimal HeapItem
	 *
	 */
	public HeapItem findMin()
	{
		if (this.min == null) {
			return null;
		}
		return this.min.item; 
	} 

	/**
	 * 
	 * pre: 0<diff<item.key
	 * 
	 * Decrease the key of item by diff and fix the heap. 
	 * 
	 */
	public void decreaseKey(HeapItem item, int diff) 
	{    
		item.key -= diff;
		HeapNode x = item.node;
		HeapNode y = item.node.parent;
		HeapItem tmp;
		while (y != null) {
			if (y.item.key < item.key)
				return;
			tmp = x.item;
			x.item = y.item;
			y.item = tmp;
			x.item.node = x;
			y.item.node = y;
			x = x.parent;
			y = y.parent;
		}
		if (item.key < min.item.key) {
			this.min = item.node;
		}
	}

	/**
	 * 
	 * Delete the item from the heap.
	 *
	 */
	public void delete(HeapItem item) 
	{    
		decreaseKey(item, Integer.MIN_VALUE);
		deleteMin();
	}

	/**
	 * 
	 * Meld the heap with heap2
	 *
	 */
	public void meld(BinomialHeap heap2){
		this.merge(heap2);
		if (this.last == null) {
			return;
		}
		HeapNode curr = this.last.next;
		if (curr == null) {
			return;
		}
		HeapNode next = curr.next;
		if (next == curr) {
			return;
		}
		HeapNode nextnext = next.next;
		HeapNode prev = this.last;
		HeapNode head = this.last.next;
		HeapNode x;
		while (nextnext != head) {
			if (curr.rank == next.rank) {
				x = HeapNode.link(curr, next);
				if (x != curr) {
					HeapNode tmp = next;
					next = curr;
					curr = tmp;
					if (prev != next) {
						prev.next = curr;
					}
					if (next == head) {
						head = curr;
						this.last.next = head;
					}
				}
				if (this.min == next) {
					this.min = curr;
				}
				curr.next = nextnext;
				next = nextnext;
				nextnext = nextnext.next;
			}
			else if (curr.rank > next.rank) {				
				prev.next = next;
				next.next = curr;
				curr.next = nextnext;
				HeapNode tmp = curr;
				curr = next;
				next = tmp;
				if (head == next) {
					head = curr;
				}
			}
			else {
				prev = curr;
				curr = next;
				next = nextnext;
				nextnext = nextnext.next;
			}
		}
		if (curr.rank == next.rank) {
			x = HeapNode.link(curr, next);
			if (x != curr) {
				HeapNode tmp = next;
				next = curr;
				curr = tmp;
			}
			if (this.min == next) {
				this.min = curr;
			}
			if (nextnext == next || prev == next) {
				curr.next = curr;
			}
			else {
				curr.next = nextnext;
				prev.next = curr;
			}
			this.last = curr;

		}
		else if (curr.rank > next.rank) {
			if (prev != next) {
				prev.next = next;
				next.next = curr;
				curr.next = nextnext;
			}
			this.last = curr;	
		}
	}

	
	private void merge(BinomialHeap heap2) {
		HeapNode x = this.last;
		HeapNode y = heap2.last;
		if (y == null) {  // if heap2 is empty
			return;
		}
		if (x == null) {  // if this is empty
			swap(this, heap2);
			return;
		}
		x = x.next;
		y = y.next;
		if (x.rank > y.rank) {  // making sure x.rank is smaller
			swap(this, heap2);
			x = this.last.next;
			y = heap2.last.next;
		}
		if (heap2.min.item.key < this.min.item.key) {  // maintain min
			this.min = heap2.min;
		}
		this.size += heap2.size;  // maintain size
		this.last.next = null;  // cut the circles
		heap2.last.next = null;
		if (x.next == null) {
			x.next = y;
			heap2.last.next = x;
			this.last = heap2.last;
			return;
		}
		HeapNode curr1 = x;
		HeapNode next1 = x.next;
		HeapNode curr2 = y;
		HeapNode next2 = y.next;
		while (curr2 != null) {
			if (curr2.rank >= curr1.rank && curr2.rank <= next1.rank) {  // curr2 is between curr1 and next1
				next2 = curr2.next;
				curr1.next = curr2;
				curr2.next = next1;
				curr1 = curr2;
				curr2 = next2;
			}
			else {
				if (next1.next != null) {  // there are more nodes left in this
					next1 = next1.next;
					curr1 = curr1.next;
				}
				else {
					next1.next = curr2;
					heap2.last.next = x;
					this.last = heap2.last;
					return;
				}
			}
		}
		if (next1.next == null) {
			this.last = next1;
		}
		this.last.next = x;
	}
	
	
	private static void swap(BinomialHeap heap1, BinomialHeap heap2) {
		int tempSize = heap1.size;
		HeapNode tempLast = heap1.last;
		HeapNode tempMin = heap1.min;
		heap1.last = heap2.last;
		heap1.min = heap2.min;
		heap1.size = heap2.size;
		heap2.last = tempLast;
		heap2.min = tempMin;
		heap2.size = tempSize;
	}
	
	
	/**
	 * 
	 * Return the number of elements in the heap
	 *   
	 */
	public int size()
	{
		return size;
	}

	/**
	 * 
	 * The method returns true if and only if the heap
	 * is empty.
	 *   
	 */
	public boolean empty()
	{
		return this.size() == 0;
	}

	/**
	 * 
	 * Return the number of trees in the heap.
	 * 
	 */
	public int numTrees() {
		String binaryRep = Integer.toBinaryString(size);
		int count = binaryRep.length() - binaryRep.replace("1", "").length();
		return count;
	}
	
	public String toString() {
		String s="";
		int c=0;
		if(this.last.next!=null) {
		HeapNode x= this.last.next;
		s+= "rank: " +x.rank;}
		return s;
	}
	public void print() {
		System.out.println("Binomial Heap:");
		System.out.println("Size: " + size);

		if (min != null) {
			System.out.println("Minimum Node: " + min.item.key);
		} else {
			System.out.println("No minimum node.");
		}

		System.out.println("Heap Nodes:");
		if (last != null) {
			Set<HeapNode> visited = new HashSet<>();
			printHeapNode(last, 0, visited);
		} else {
			System.out.println("No heap nodes.");
		}
	}

	private void printHeapNode(HeapNode node, int indentLevel, Set<HeapNode> visited) {
		StringBuilder indent = new StringBuilder();
		for (int i = 0; i < indentLevel; i++) {
			indent.append("    ");
		}

		System.out.println(indent + "Key: " + node.item.key);
		System.out.println(indent + "Info: " + node.item.info);
		System.out.println(indent + "Rank: " + node.rank);

		visited.add(node);

		if (node.child != null && !visited.contains(node.child)) {
			System.out.println(indent + "Child:");
			printHeapNode(node.child, indentLevel + 1, visited);
		}

		if (node.next != null && !visited.contains(node.next)) {
			System.out.println(indent + "Sibling:");
			printHeapNode(node.next, indentLevel, visited);
		}
	}


	
	/**
	 * Class implementing a node in a Binomial Heap.
	 *  
	 */
	public class HeapNode{
		public HeapItem item;
		public HeapNode child;
		public HeapNode next;
		public HeapNode parent;
		public int rank;
		
		public HeapNode() {
			this(null, null, null, null, 0);
		}
		
		public HeapNode(HeapItem item,HeapNode child,HeapNode next,HeapNode parent,int rank) {
			this.item = item;
			this.child = child;
			this.next = next;
			this.parent = parent;
			this.rank = rank;
		}
		
		public static HeapNode link(HeapNode x, HeapNode y) {
			/*
			 * linking two binomial trees of same degree
			 */
			if (x.item.key > y.item.key) {
				HeapNode tmp = x;
				x = y;
				y = tmp;
			}
			if (x.child != null) {
				HeapNode tempNext = x.child.next;
				x.child.next = y;
				y.next = tempNext;
				x.child = y;
				y.parent = x;
			}
			else {
				y.next = y;
				x.child = y;
				y.parent = x;
			}
			x.rank++;
			return x;
		} 
	}

	/**
	 * Class implementing an item in a Binomial Heap.
	 *  
	 */
	public class HeapItem{
		
		public HeapNode node;
		public int key;
		public String info;
		
		public HeapItem() {
			this(null, 0, null);
		}
		
		public HeapItem(HeapNode node, int key, String info) {
			this.info = info;
			this.key = key;
			this.node = node;
		}
	}

}
