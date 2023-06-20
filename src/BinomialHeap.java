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
	public boolean[] hasTree;
	
	public BinomialHeap() {
		this(0,null,null);
	}
	public BinomialHeap(int size,HeapNode last,HeapNode min) {
		this.size=size;
		this.last=last;
		this.min=min;
		HeapNode x= this.min;
		//System.out.println("6");
		if(size!=0) {
		hasTree=new boolean [last.rank+1]; 
		if(size==1)
			hasTree[x.rank]=true;
		else {
		while(x.next!=last)
		{
			hasTree[x.rank]=true;
		}
		hasTree[last.rank]=true;}}
		else {hasTree=new boolean [1]; }
		//System.out.println("7");
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
		//System.out.println("5");
		HeapNode newNode = new HeapNode(null, null, null, null, 0);
		HeapItem newItem = new HeapItem(newNode, key, info);
		newNode.item=newItem;
		newNode.next=newNode;
		BinomialHeap newHeap = new BinomialHeap(1, newNode, newNode);
		//System.out.println("4");
		this.meld(newHeap);
		//System.out.println("3");
		if(key<this.min.item.key)
			this.min=newNode;
		return newItem;
	}

	/**
	 * 
	 * Delete the minimal item
	 *
	 */
	public void deleteMin()
	{
		HeapNode y=this.min;
		int childrenSize = (int) Math.pow(2,y.rank );
		HeapNode x = y.child;
		while (x.next!=x) {
			x.parent=null;
		}
		BinomialHeap childrenOfMin = new BinomialHeap(childrenSize, x, x.next);
		HeapNode z = this.min;
		while(z.next!=y)
			z=z.next;
		z.next=y.next;		
		this.size-=(childrenSize+1);
		this.meld(childrenOfMin);
	}

	/**
	 * 
	 * Return the minimal HeapItem
	 *
	 */
	public HeapItem findMin()
	{
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
		item.key-=diff;
		HeapNode x= item.node;
		HeapNode y= item.node.parent;
		while(y!=null) {
			if(item.node.parent.item.key<item.key)
				return;
			HeapItem xItem = x.item;
			HeapItem yItem = y.item;
			x.item=yItem;
			y.item=xItem;
			xItem.node=y;
			yItem.node=x;
			x=x.parent;
			y=y.parent;
		}
	}

	/**
	 * 
	 * Delete the item from the heap.
	 *
	 */
	public void delete(HeapItem item) 
	{    
		decreaseKey(item, (int)Double.NEGATIVE_INFINITY);
		deleteMin();
	}

	/**
	 * 
	 * Meld the heap with heap2
	 *
	 */
	public void meld(BinomialHeap heap2)
	{
		this.print();
		if(heap2.min==null)
			//heap2 is null
			return;
		if(this.min==null)
			{
			//this heap is null, we need to change this to heap2
			this.hasTree=heap2.hasTree;
			this.last=heap2.last;
			this.min=heap2.min;
			this.size=heap2.size;
			heap2=null;
			//System.out.println("8");
			return;
			}
		if(this.size<heap2.size)
		{
			//making sure that heap2 is the smaller one
			int tempSize=this.size;
			HeapNode tempLast=this.last;
			HeapNode tempMin=this.min;
			boolean[] tempHasTree=this.hasTree;
			this.hasTree=heap2.hasTree;
			this.last=heap2.last;
			this.min=heap2.min;
			this.size=heap2.size;
			heap2.hasTree=tempHasTree;
			heap2.last=tempLast;
			heap2.min=tempMin;
			heap2.size=tempSize;
			
		}

		for(int i=0;i<heap2.hasTree.length;i++)
		{
		
			HeapNode x = this.last.next;
			HeapNode y = heap2.last.next;
			
			if(this.hasTree[i]&&heap2.hasTree[i])
			{
				while(x.next.rank!=i)
					x=x.next;
				while(y.next.rank!=i)
					y=y.next;
				int addToSize = (int) Math.pow(2,y.rank );
				HeapNode.link(x, y);
				hasTree[i]=false;
				heap2.hasTree[i]=false;
				if(x.rank>=hasTree.length)
					hasTree=Arrays.copyOf(hasTree, hasTree.length*2);
				hasTree[x.rank]=true;
				size+=addToSize;
			}
			else {
				if(heap2.hasTree[i]) {
					HeapNode temp=y;
					while(true) {
						if(temp.next.rank<i)
							temp=temp.next;
						else
							break;}
					y=temp.next;
					while(true) {
						if(x.next.rank<i)
						x=x.next;
						else
							break;}
					
					int addToSize = (int) Math.pow(2,y.rank );
					HeapNode xNext= x.next;
					temp.next=y.next;
					x.next=y;
					y.next=xNext;					
					hasTree[i]=true;
					heap2.hasTree[i]=false;
					size+=addToSize;
			}}
				
				
				
		}
	}
	
	/**
	 * 
	 * Return the number of elements in the heap
	 *   
	 */
	public int size()
	{
		if (this.min!=null)
			return this.size;
		else 
			return 0;
	}

	/**
	 * 
	 * The method returns true if and only if the heap
	 * is empty.
	 *   
	 */
	public boolean empty()
	{
		return this.size()==0;
	}

	/**
	 * 
	 * Return the number of trees in the heap.
	 * 
	 */
	public int numTrees()
	{
		int count=0;
		for(int i=0;i<hasTree.length;i++)
			if(hasTree[i])
				count++;
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
			this(null,null,null,null,0);
		}
		public HeapNode(HeapItem item,HeapNode child,HeapNode next,HeapNode parent,int rank) {
			this.item=item;
			this.child=child;
			this.next=next;
			this.parent=parent;
			this.rank=rank;
		}
		public static HeapNode link(HeapNode x, HeapNode y) {
			/*
			 * linking two binomial trees of same degree
			 */
			if (x.item.key>y.item.key) {
				HeapNode temp=x;
				x = y;
				y=temp;
			}
			y.next=x.child;
			x.child = y;
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
			this(null,0,null);
		}
		public HeapItem(HeapNode node,int key,String info) {
			this.info=info;
			this.key=key;
			this.node=node;
		}
	}

}
