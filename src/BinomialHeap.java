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
	
	public BinomialHeap(int size,HeapNode last,HeapNode min) {
		this.size=size;
		this.last=last;
		this.min=min;
		HeapNode x= this.min;
		if(size!=0) {
		hasTree=new boolean [last.rank+1]; 
		while(x.next!=last)
		{
			hasTree[x.rank]=true;
		}
		hasTree[last.rank]=true;}
		else {hasTree=new boolean [1]; }
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
		HeapNode newNode = new HeapNode(null, null, null, null, 1);
		HeapItem newItem = new HeapItem(newNode, key, info);
		BinomialHeap newHeap = new BinomialHeap(1, newNode, newNode);
		this.meld(newHeap);
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
		if(heap2==null)
			return;
		if(this==null)
			{
			this.hasTree=heap2.hasTree;
			this.last=heap2.last;
			this.min=heap2.min;
			this.size=heap2.size;
			heap2=null;
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
		
			HeapNode x = this.min;
			HeapNode y = heap2.min;
			
			if(this.hasTree[i]&&heap2.hasTree[i])
			{
				for(int j=0;j<=i;j++) {
					x=x.next;
					y=y.next;
				}
				int addToSize = (int) Math.pow(2,y.rank );
				HeapNode.link(x, y);
				hasTree[i]=false;
				heap2.hasTree[i]=false;
				hasTree[x.rank]=true;
				size+=addToSize;
			}
			else {
				if(heap2.hasTree[i])
					for(int j=0;j<i;j++) {
						x=x.next;
						y=y.next;
					}
					y=y.next;
					int addToSize = (int) Math.pow(2,y.rank );
					HeapNode xNext= x.next;
					x.next=y;
					y.next=xNext;
					hasTree[i]=true;
					heap2.hasTree[i]=false;
					size+=addToSize;
			}
				
				
				
		}
	}
	
	/**
	 * 
	 * Return the number of elements in the heap
	 *   
	 */
	public int size()
	{
		if (this!=null)
			return this.size;
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
		public HeapItem(HeapNode node,int key,String info) {
			this.info=info;
			this.key=key;
			this.node=node;
		}
	}

}
