import java.util.Iterator;


/**
 * FibonacciHeap
 *
 * An implementation of fibonacci heap over non-negative integers.
 */


public class FibonacciHeap
{

	public HeapNode min;
	public int n = 0;
	public static int totalLinks = 0; 
	public static int totalCuts = 0; 
	public int treeNumber = 0;
	public int marked = 0;

	
	public FibonacciHeap() {
		this.min = null;
	}
	
   /**
    * public boolean empty()
    *
    * precondition: none
    * 
    * The method returns true if and only if the heap
    * is empty.
    *   
    */
    public boolean empty()
    {
    	return this.min == null;

    }
		
   /**
    * public HeapNode insert(int key)
    *
    * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap. 
    */
    public HeapNode insert(int key)
    {    
    	HeapNode node = new HeapNode(key);
    	return insert(node);
    }
    
    public HeapNode insert(HeapNode node)
    {    
    	if (this.min == null) {
    		this.min = node;
    	}
    	else {
    		addNodeBAfterNodeA(this.min, node); 
    		if (node.key < this.min.key) { 
    			this.min = node;
    		}
    	}
    	this.n++;
    	this.treeNumber++;
    	return node;
    }

   /**
    * public void deleteMin()
    *
    * Delete the node containing the minimum key.
    *
    */
    public void deleteMin()
    {
     	if(!empty()) {		
     		if(min.rank > 0) {		// min has at least one child
     			if(this.min.next == this.min) { // min is only child
     				treeNumber = this.min.rank;
     				this.min = this.min.child;
     				this.min.parent = null;
     				if (this.min != this.min.next) {
         				this.min = consolidate();
     				}
     			}
     			else {  
     				HeapNode child = this.min.child;
     				addAllChildrenAfterNodeA(this.min, child);
     				child.parent = null; 
     				deleteNode(this.min);
     				this.min = child;
     				this.min = consolidate();
     			}
     		}
     		else {			// min has no children
     			if(this.min.next == this.min) { //min was the only node
     				this.min = null;
     				treeNumber = 0; 
     			}
     			else {
     				HeapNode next = this.min.next;
     				deleteNode(this.min);
     				this.min = next;
     				this.min = consolidate();
     			}
     		}
     		this.n--;
     	}
    }
  
    /*links trees in the heap until there is at most one tree of each rank.*/
    private HeapNode consolidate() {
       HeapNode[] arr = new HeapNode[calculateMaxRank()]; //
  	   for (int i =0; i< arr.length; i++) {
  		   arr[i] = null;
  	   }
    	toBuckets(arr);
    	return fromBuckets(arr);
    }

    /*Links trees w/ the same rank until there’s at most one of each rank*/
    private void toBuckets(HeapNode[] arr) {
    	HeapNode x = this.min;
 	    x.prev.next = null; //breaks the loop

   	   while (x != null) {
 		   HeapNode y = x;
 		   x = x.next;
 		   
 		   while (arr[y.rank] != null) {
 			   y = link(arr[y.rank], y); 
 			   arr[y.rank-1] = null; //deletes the node from arr.
 		   }
 		   arr[y.rank] = y;
 		   
 		   y.next = y;
 		   y.prev = y;
   	   }
 	}	
   	
    /*Creates new heap from the trees in arr*/
    private HeapNode fromBuckets(HeapNode[] arr) {
    	this.min = null;
    	treeNumber = 0;
    	for (int i=0; i< arr.length; i++){
    		if (arr[i] != null) {
    			treeNumber++;
    			if (this.min == null) {
    				this.min = arr[i];
	    			this.min.next = this.min;
	    			this.min.prev = this.min;
	    			this.min.parent = null; 
    			}
    			else {
	    			addNodeBAfterNodeA(this.min, arr[i]);
	    			arr[i].parent = null;  	
	    			if (arr[i].key < this.min.key) {
	    				this.min = arr[i];
	    			}
	    		}
	    	}
    	}
    	return this.min; 
    }
    
    /*makes x the child of y*/
 	private HeapNode link(HeapNode x, HeapNode y) {
 		if (x.key < y.key) {
 			   HeapNode tmp = y;
 			   y = x;
 			   x = tmp;
 		   }
 		if (y.child == null) { //node is of rank 0.
 			x.next = x;
 			x.prev = x;
 		}
 		else { //y has at least one child
 			HeapNode child = y.child;
 			addNodeBAfterNodeA(child, x);
 		}
 		y.child = x;
 		x.parent = y;

 		y.rank++;
 		x.mark = false;
 		totalLinks++;
 		
 		y.next = y;
 		y.prev = y;
 		return y;
 	}
	
	/*adds node and all it's brothers after node A.*/
	private void addAllChildrenAfterNodeA(HeapNode A, HeapNode node)
	{
		HeapNode nextA = A.next;
		HeapNode last = node.prev;
		
		A.next = node;
		node.prev = A;
		last.next = nextA;
		nextA.prev = last;
	}

   /**
    * public HeapNode findMin()
    *
    * Return the node of the heap whose key is minimal. 
    *
    */
    public HeapNode findMin()
    {
    	return this.min;
    } 
    
   /**
    * public void meld (FibonacciHeap heap2)
    *
    * Meld the heap with heap2
    *
    */
    public void meld (FibonacciHeap heap2)
    {
    	if (!heap2.empty()) {
    		HeapNode heap2Min = heap2.min;
    		if (empty()) {
    			this.min = heap2Min;
    			this.n= heap2.n;
    			this.treeNumber = heap2.treeNumber;
    			this.marked = heap2.marked;
    		}
    		else {
    			addAllChildrenAfterNodeA(this.min, heap2Min);
    			//updating min
		    	if (heap2Min.key < this.min.key) {
		    		this.min = heap2Min;
		    	}
    		}
	    	this.n += heap2.n;
	    	this.treeNumber += heap2.treeNumber;
    	}
	    return; 		
    }

   /**
    * public int size()
    *
    * Return the number of elements in the heap
    *   
    */
    public int size()
    {
    	return this.n;
    }
    	
    /**
    * public int[] countersRep()
    *
    * Return a counters array, where the value of the i-th entry is the number of trees of order i in the heap. 
    * 
    */
    public int[] countersRep()
    {
    	if(empty()) {
    		return new int[0];
    	}
    	int[] arr = new int[calculateMaxRank()];
    	
    	HeapNode curr = this.min;
    	for (Iterator<HeapNode> iter = this.iterator(curr); iter.hasNext();) {
    		HeapNode node = iter.next();
    		int rank = node.rank;
    		arr[rank]++;
    	}
    	return arr;
    }
    
    /*calculates log(n) w/ base phi */
    public int calculateMaxRank(){
    	final double PHI = (1+Math.sqrt(5))/2;
    	double maxRankDouble = Math.log(this.n)/Math.log(PHI);
    	int maxRank = ((int)maxRankDouble) + 1; 
    	return maxRank;
    }
	
   /**
    * public void delete(HeapNode x)
    *
    * Deletes the node x from the heap. 
    *
    */
    public void delete(HeapNode x) 
    {   
    	int delta = x.key - this.min.key + 1; //makes sure x is min.
    	this.decreaseKey(x, delta);
    	this.deleteMin();
    	return;
    }

   /**
    * public void decreaseKey(HeapNode x, int delta)
    *
    * The function decreases the key of the node x by delta. The structure of the heap should be updated
    * to reflect this chage (for example, the cascading cuts procedure should be applied if needed).
    */
    public void decreaseKey(HeapNode x, int delta)
    {    
    	if (delta > x.key) {
    		System.out.println("delta is too big");
    	}
    	else  { //(delta <= x.key)
    		x.key = x.key - delta;
	    	HeapNode parent = x.parent;
	    	if (parent != null && x.key < parent.key) {
	    		cut(x, parent);
	    		cascadingCut(parent);
	    	}
	    	//updating min
	    	if (x.key < this.min.key) {
	    		this.min = x;
	    	}
    	}
    	return;
    }
    
    /*Cuts the connection between x and y, and moves x in the root list.*/
    public void cut(HeapNode x, HeapNode y)
    {
    	y.rank--; 
    	if (x.mark == true) {
    		x.mark = false;
    		this.marked--;
    	}
    	if (x.next == x) {
    		y.child = null;
    	}
    	if (x.parent.child == x) {
    		x.parent.child = x.next;
    	}
    	deleteNode(x);
    	x.parent = null;
    	
    	addNodeBAfterNodeA(this.min, x);
    	totalCuts++;
    	treeNumber++;
    }
    
    /*Recursively cuts up the tree until it reaches an unmarked node*/
    public void cascadingCut(HeapNode y)
    {
    	HeapNode node =  y.parent;
    	if (node != null) {
    		if (y.mark == false) {
    			y.mark = true;
    			this.marked++;
    		}
    		else {
    			cut (y, node);
    			cascadingCut(node);
    		}
    	}
    }


   /**
    * public int potential() 
    *
    * This function returns the current potential of the heap, which is:
    * Potential = #trees + 2*#marked
    * The potential equals to the number of trees in the heap plus twice the number of marked nodes in the heap. 
    */
    public int potential() 
    {    
    	return this.treeNumber + 2*this.marked;
    }

   /**
    * public static int totalLinks() 
    *
    * This static function returns the total number of link operations made during the run-time of the program.
    * A link operation is the operation which gets as input two trees of the same rank, and generates a tree of 
    * rank bigger by one, by hanging the tree which has larger value in its root on the tree which has smaller value 
    * in its root.
    */
    public static int totalLinks()
    {    
    	return totalLinks;
    }

   /**
    * public static int totalCuts() 
    *
    * This static function returns the total number of cut operations made during the run-time of the program.
    * A cut operation is the operation which diconnects a subtree from its parent (during decreaseKey/delete methods). 
    */
    public static int totalCuts()
    {    
    	return totalCuts; 
    }
    
    /*adds node B after node A and updates B's parent*/
	private void addNodeBAfterNodeA(HeapNode A, HeapNode B) {   
		HeapNode next = A.next;
		B.prev = A;
		A.next = B;
		B.next = next;
		next.prev = B;
		
		//updating parent
		HeapNode parentA = A.parent;
		B.parent = parentA;
	}
	
	/*deletes node's connection to its brothers*/ 
	public void deleteNode(HeapNode node) {  
		HeapNode prev = node.prev;
		HeapNode next = node.next;
		
		if (next != null) {
			next.prev = prev;
			prev.next = next;  
		}
			
	}
	
	
	//???????????????????????????????????????????????????????????????????????????????????????
	public void printRootList() {
		if (empty()) {
			System.out.println("empty rootlist");
		}
		else {
			
		HeapNode curr = this.min;
		String str1 = "min = " + curr.key + " n = " + n + " list = ";
	
		for (Iterator<HeapNode> iter = this.iterator(curr); iter.hasNext();) {
			HeapNode node = iter.next();
			str1 += node.key +" ";
			}
	    System.out.println("****printing rootlist: ****");
		System.out.println(str1);
		
		    System.out.println("****printing rootlist: ****");
	}
		}
	
   /**
    * public class HeapNode
    * 
    * If you wish to implement classes other than FibonacciHeap
    * (for example HeapNode), do it in this file, not in 
    * another file 
    *  
    */
    public class HeapNode{

    	public int key;
    	public int rank;
    	public boolean mark;
    	public HeapNode child;
    	public HeapNode next;
    	public HeapNode prev;
    	public HeapNode parent;

      	public HeapNode(int key) {
    	    this.key = key;
    	    this.rank = 0;
    	    this.child = null;
    	    this.parent = null;
    	    this.next = this;
    	    this.prev = this;
    	    this.mark = false;
          }
      	
      	public int getKey() {
    	    return this.key;
          }

        }
    
	public Iterator<HeapNode> iterator(HeapNode startingHeap) {
		heapNodeIterator iter = new heapNodeIterator(startingHeap);
		return iter;
	}
	
	/*iterator for all brothers of startingNode*/
   public class heapNodeIterator implements Iterator<HeapNode>{
    	
    	HeapNode currNode;
    	HeapNode startingNode;
    	int counter;
    	
    	heapNodeIterator (HeapNode startingNode){
    		this.currNode = startingNode; 	
    		this.startingNode = startingNode;
    		counter = 0;
    	}
    	    	
    	public boolean hasNext() {
    		if (counter == 0) {
    			counter++;
    			return true;
    		}
    		else {
        		return (currNode != startingNode);
    		}
    	}
    
    	public HeapNode next() {

    			HeapNode returnNode = currNode;
    			currNode = currNode.next;
    			return returnNode; 		
    	} 
    	public void remove() { 
    		throw new UnsupportedOperationException();
    	}
    }
}

