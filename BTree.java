public class BTree<T extends Comparable<T>> {

	public int m;
	public int maxKeys;
	public int numNodes;
	public Node<T> root;
	public Node<T>[] nodeArray;

	/**
	 * 
	 * @param m
	 */
	public BTree(int m) {
		
		this.m = m;
		maxKeys = this.m-1;
		numNodes = 0;
		root = null;
		nodeArray = null;

	}

	/**
	 * 
	 * @param data
	 */
	public Node<T> insert(T data) {

		//save the original data for checks of the insertion node
		T originalData = data;

		//check if data already in tree
		Node<T> returnNode = find(data);
		if(returnNode != null){
			return returnNode;
		}

		//find the node to insert into

		Node<T> curr = root;
		Node<T> childReference = null;

		//empty case
		if(curr == null){
			root = new Node<T>(m);
			numNodes++;
			nodeArray = new Node[numNodes];
			nodeArray[numNodes-1] = root;
			root.keys[0] = data;
			root.parent = null;
			root.leaf = true;
			root.numberOfKeys = 1;
			return root;
		}

		//while the current node is not a leaf
		while(!curr.leaf){
			int i = 0;
			while((i < curr.numberOfKeys) && (curr.keys[i].compareTo(data) < 0)){
				i++;
			}

			curr = curr.children[i];

		}

		//curr is the node to insert into

		while(true){

			//Case 1: The node still has room
			if(curr.numberOfKeys < maxKeys){
				//shift larger keys to the right
				int i = curr.numberOfKeys-1;
				while((i >= 0) && (curr.keys[i].compareTo(data)>0)){
					curr.keys[i+1] = curr.keys[i];
					curr.children[i+2] = curr.children[i+1];
					i--;
				}
				//insert data
				curr.keys[i+1] = data;
				if(data.compareTo(originalData) == 0){
					returnNode = curr;
				}
				curr.children[i+2] = childReference;
				curr.numberOfKeys++;
				return returnNode;
			}
			//Case 2: Insertion into a full node
			else{
				
				//split the node and then insert the midpoint higher up
				
				Node<T> sibling = new Node<>(m);
				numNodes++;

				//update nodeArray
				addToNodeArray(sibling);

				sibling.leaf = curr.leaf;
				sibling.parent = curr.parent;

				int midpoint = (m-1)/2;

				//create a temp array of size m to store all the keys including the new one
				//and a node array of size m+1 to store all the child references
				Comparable<T>[] temp = new Comparable[m];
				Node<T>[] childrenTemp = new Node[m+1];
				//copy all of curr's keys into temp
				for(int i = 0; i < m-1; i++){
					temp[i] = curr.keys[i];
					childrenTemp[i] = curr.children[i];
				}
				childrenTemp[m-1] = curr.children[m-1];
				//add the new data at the correct position
				int i = m-2;
				while((i >= 0) && (temp[i].compareTo(data)>0)){
					temp[i+1] = temp[i];
					childrenTemp[i+2] = childrenTemp[i+1];
					i--;
				}
				//insert data
				temp[i+1] = data;
				childrenTemp[i+2] = childReference;

				//wipe keys array of curr
				curr.keys = new Comparable[m-1];
				curr.children = new Node[m];
				
				//distribute keys before the midpoint into curr
				for(int j = 0; j < midpoint; j++){
					curr.keys[j] = temp[j];
					//if data was inserted, set returnNode to curr
					if(temp[j].compareTo(originalData) == 0){
						returnNode = curr;
					}
					curr.children[j] = childrenTemp[j];
					if(childrenTemp[j] != null){
						childrenTemp[j].parent = curr;
					}
				}
				curr.children[midpoint] = childrenTemp[midpoint];
				if(childrenTemp[midpoint] != null){
					childrenTemp[midpoint].parent = curr;
				}
				curr.numberOfKeys = midpoint;

				//distribute keys after the midpoint into sibling
				int k = 0;
				int j = midpoint+1;
				for(j = midpoint+1; j < m; j++, k++){
					sibling.keys[k] = temp[j];
					//if data was inserted, set returnNode to sibling
					if(temp[j].compareTo(originalData) == 0){
						returnNode = sibling;
					}
					sibling.children[k] = childrenTemp[j];
					if(childrenTemp[j] != null){
						childrenTemp[j].parent = sibling;
					}
				}
				sibling.children[k] = childrenTemp[j];
				if(childrenTemp[j] != null){
					childrenTemp[j].parent = sibling;
				}

				//wipe children references at end of curr
				for(int a = midpoint+1; a < m; a++){
					curr.children[a] = null;
				}

				sibling.numberOfKeys = m-midpoint-1;
				data = (T)temp[midpoint];
				childReference = sibling;

				//if curr was the root
				if(curr.parent == null){
					root = new Node<>(m);
					numNodes++;

					//update nodeArray
					addToNodeArray(root);

					root.keys[0] = data;
					root.numberOfKeys = 1;
					root.children[0] = curr;
					root.leaf = false;
					curr.parent = root;
					root.children[1] = sibling;
					sibling.parent = root;
					return returnNode;
				}
				else{
					curr = curr.parent;
				}

			}
		}

	}

	public void addToNodeArray(Node<T> node){

		//update nodeArray
		Node<T>[] newNodeArray = new Node[numNodes];
		for(int i = 0; i < numNodes-1; i++){
			newNodeArray[i] = nodeArray[i];
		}
		newNodeArray[numNodes-1] = node;
		nodeArray = newNodeArray;

	}

	/**
	 * 
	 * @param data
	 */
	public Node<T> find(T data) {

		Node<T> curr = root;

		//while the current node is not null and the data has not been found
		while((curr != null)){
			int i = 0;
			while((i < curr.numberOfKeys) && (curr.keys[i].compareTo(data) < 0)){
				i++;
			}

			//must go down a child ref
			if(i == curr.numberOfKeys || curr.keys[i].compareTo(data) > 0){
				curr = curr.children[i];
			}
			//has been found
			else{
				return curr;
			}

		}

		return null;
	
	}

	public Node<T>[] nodes() {
		
		return nodeArray;

	}

	public int numKeys() {
		
		int sumOfKeys = 0;

		for(int i = 0; i < numNodes; i++){
			sumOfKeys += nodeArray[i].numberOfKeys;
		}

		return sumOfKeys;

	}

	public int countNumNodes() {
		
		return numNodes;

	}

}