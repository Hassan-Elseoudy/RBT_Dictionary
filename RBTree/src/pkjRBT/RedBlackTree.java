package pkjRBT;

public class RedBlackTree <T extends Comparable<T>> {
	public static final int DUMMY_VALUE = -1;
	// This is the class that stores and manipulates the Red-Black tree data structure.
	// It uses a Comparable T so that different types of data could be stored in the structure.
	class Node {
		private T data; //Comparable T data
		private boolean isBlack; //color
		private Node left; //left child node
		private Node right; //right child node
		private Node parent; //parent node
		public Node() { //creates a leaf
			this.data = null;
			this.isBlack = true; //black node
			this.left = null;
			this.right = null;
			this.parent = null;
		}
		public Node(T data) { //creates a red node ready to be inserted
			this.data = data;
			this.isBlack = false; //red node
			this.left = new Node(); //left child is a leaf
			this.right = new Node(); //right child is a leaf
			this.parent = null;
			this.left.parent = this; //leaf recognizes this
			this.right.parent = this; //leaf recognizes this
		}
		public boolean isLeaf() { //decides whether the node is a leaf
			return (this.isBlack && this.data == null && this.left == null && this.right == null);
		}
	}
	private Node root; //used to navigate structure throughout code
	private int blackHeight; //calculated at various points in the code
	public RedBlackTree() {
		this.root = new Node(); //creates a leaf at the root
		this.blackHeight = DUMMY_VALUE;
	}
	public T add(T input) { //publicly accessible add method
		if (this.root.isLeaf()) { //if the root is a leaf
			Node n = new Node(input);
			n.isBlack = true; //root is black
			this.root = n;
			return input;
		}
		else {
			return add(this.root, input); //helper method
		}
	}
	public T remove(T target) { //publicly accessible remove method
		return remove(this.root, target); //helper method
	}
	public Boolean search(T target) { //publicly accessible search method
		return search(this.root, target, 0); //helper method
	}
	public boolean isValid() { //checks to see if the tree meets the requirements
		boolean rootBlack = this.root.isBlack; //is the root black?
		boolean leavesBlack = AllLeavesAreBlack(this.root); //are all the leaves black?
		boolean redBlack = EveryRedNodeHasBlackChildren(this.root); //do all red nodes have black children?
		this.blackHeight = DUMMY_VALUE; //reset black height to dummy value
		boolean uniform = BlackHeightIsUniform(this.root, 0); //is the black height uniform across all leaves?
		return (rootBlack && leavesBlack && redBlack && uniform);
	}
	public int getBlackHeight() {
		this.blackHeight = DUMMY_VALUE; //resets this.blackHeight
		if (BlackHeightIsUniform(this.root, 0)) { //calculates this.blackHeight
			return this.blackHeight;
		}
		System.out.println("Black height is not uniform.");
		return DUMMY_VALUE; //if the black height isn't uniform, return an obviously wrong answer
	}
	public void printPreOrder() { //publicly accessible pre-order traversal
		//this method is never used in my driver, but it is quite useful for debugging
		printPreOrder(this.root); //helper method
	}
	private T add(Node node, T input) { //helper method
		if (node.isLeaf()) { //found a place to insert the input
			//create a full node with input and replace node with n
			Node n = new Node(input);
			n.parent = node.parent;
			if (n.parent.right == node) {
				n.parent.right = n;
			}
			else {
				n.parent.left = n;
			}
			fixAdd(n); //restructure the tree
			return input;
		}
		else if (node.data.compareTo(input) < 0.0) { //go right if input > node.data
			return add(node.right, input);
		}
		else if (node.data.compareTo(input) > 0.0){ // go left if input < node.data
			return add(node.left, input);
		}
		else { //return null if input is a duplicate value
			return null;
		}
	}
	private T remove(Node node, T input) { //helper method
		if (node.isLeaf()) { //input is not in the tree; return null
			return null;
		}
		if (node.data.compareTo(input) == 0) { //found input
			if (!node.left.isLeaf() && !node.right.isLeaf()) { //node has two non-leaf children
				Node replacement = node.right;
				while (!replacement.left.isLeaf()) {
					replacement = replacement.left;
				}
				//replacement is the smallest node larger than node, and replacement has no left child
				node.data = replacement.data; //fill in node with replacement's data, effectively removing node
				remove(replacement, replacement.data); //recursive remove call on replacement
				return input;
			}
			//at this point, we have at most one non-leaf children
			if (!node.isBlack) { //must have two leaves, and node cannot be root
				//since node is red, we can delete it without worry
				if (node.parent.right == node) {
					node.parent.right = node.right;
				}
				else {
					node.parent.left = node.right;
				}
				node.right.parent = node.parent;
				return input;
			}
			if (!node.left.isBlack || !node.right.isBlack) { //node is black but has a red child and a leaf
				//node can be replaced with its red child if said child is recolored black
				if (!node.left.isBlack) { //left child is red, right child is black leaf
					if (node.parent == null) { //node is root
						node.left.parent = null;
						node.left.isBlack = true; //root is always black
						this.root = node.left; //node's left child becomes the root
					}
					else {
						//safely replace node with node.left
						if (node.parent.right == node) {
							node.parent.right = node.left;
						}
						else {
							node.parent.left = node.left;
						}
						node.left.parent = node.parent;
						node.left.isBlack = true;
					}
				}
				else { //right child is red, left child is black leaf
					if (node.parent == null) { //node is root
						node.right.parent = null;
						node.right.isBlack = true; //root is black
						this.root = node.right; //node's right child is root
					}
					else {
						//safely replace node with node.right
						if (node.parent.right == node) {
							node.parent.right = node.right;
						}
						else {
							node.parent.left = node.right;
						}
						node.right.parent = node.parent;
						node.right.isBlack = true;
					}
				}
				return input;
			}
			//node to delete is black and has two leaf children
			fixDelete(node); //make the tree able to survive the deletion
			if (node.parent == null) { //make deletion if node is root
				this.root = node.right;
				node.right.parent = null;
			}
			else { //make deletion otherwise
				if (node.parent.left == node) {
					node.parent.left = node.right;
				}
				else {
					node.parent.right = node.right;
				}
				node.right.parent = node.parent;
			}
			return input;
		}
		else if (node.data.compareTo(input) < 0) { //keep looking right
			return remove(node.right, input);
		}
		else { //keep looking left
			return remove(node.left, input);
		}
	}
	private Boolean search(Node node, T input, int checks) { //helper method
		++checks; //increment the counter
		if (node.isLeaf()) { //not contained
			return false;
		}
		if (node.data.compareTo(input) == 0) { //found
			return true;
		}
		if (node.data.compareTo(input) < 0) { //look right
			return search(node.right, input, checks);
		}
		else { //look left
			return search(node.left, input, checks);
		}
	}
	private boolean AllLeavesAreBlack(Node node) { //requirement testing
		if (node.isLeaf()) { //if node.isLeaf(), then node must be a leaf that is black
			return true;
		}
		if ((node.left == null || node.right == null || node.data == null) && !node.isBlack) {
			//only leaves should meet these qualities, so any red node with such qualities is illegal
			return false;
		}
		return (AllLeavesAreBlack(node.left) && AllLeavesAreBlack(node.right)); //look left and right before returning
	}
	private boolean EveryRedNodeHasBlackChildren(Node node) { //requirement testing
		if (node.isLeaf()) { //halting condition
			return true;
		}
		if (!node.isBlack && (!node.left.isBlack || !node.right.isBlack)) {
			//a red node cannot have any red children
			return false;
		}
		//look left and right before returning
		return (EveryRedNodeHasBlackChildren(node.left) && EveryRedNodeHasBlackChildren(node.right));
	}
	private boolean BlackHeightIsUniform(Node node, int height) { //requirement testing
		if (node.isBlack) { //increment the counter
			++height;
		}
		if (node.isLeaf()) { //done calculating
			if (this.blackHeight == DUMMY_VALUE) { //this.blackHeight hasn't been set yet
				this.blackHeight = height; //set it
				return true;
			}
			else {
				if (this.blackHeight == height) { //everything is uniform so far
					return true;
				}
				else { //irregularity spotted
					return false;
				}
			}
		}
		//look left and right before returning
		return (BlackHeightIsUniform(node.left, height) && BlackHeightIsUniform(node.right, height));
	}
	private void leftRotation(Node node) { //helper method
		if (node.isLeaf() || node.right.isLeaf()) { //cannot rotate from such a position
			System.out.println("Left rotation called in error.");
			return;
		}
		if (node.parent != null) { //node.right is taking node's place at the higher level in the tree
			if (node.parent.right == node) {
				node.parent.right = node.right;
			}
			else {
				node.parent.left = node.right;
			}
		}
		else { //node is the current root, node.right should be the new one
			this.root = node.right;
		}
		node.right.parent = node.parent; //make node.right recognize its new parent
		node.right.left.parent = node; //make node's new right child recognize node
		node.parent = node.right; //node.right moves to node.parent
		node.right = node.right.left; //node recognizes its new right child
		node.parent.left = node; //node's new parent recognizes node
	}
	private void rightRotation(Node node) { //helper method
		if (node.isLeaf() || node.left.isLeaf()) { //cannot rotate from such a position
			System.out.println("Right rotation called in error.");
			return;
		}
		if (node.parent != null) { //node.left is taking node's place at the higher level in the tree
			if (node.parent.right == node) {
				node.parent.right = node.left;
			}
			else {
				node.parent.left = node.left;
			}
		}
		else { //node is the current root, node.left should be the new one
			this.root = node.left;
		}
		node.left.parent = node.parent; //make node.left recognize its new parent
		node.left.right.parent = node; //make node's new left child recognize node
		node.parent = node.left; //node.left moves to node.parent
		node.left = node.left.right; //node recognizes its new left child
		node.parent.right = node; //node's new parent recognizes node
	}
	private void fixAdd(Node node) { //balancing method
		//if this method was called, node should be red and not a leaf
		if (node.isBlack || node.isLeaf()) {
			System.out.println("fixAdd called in error.");
			return;
		}
		if (node.parent == null) { //node is the root
			node.isBlack = true; //the root is always black
			return;
		}
		//we will only have a problem that requires restructuring if parent is red, as that violates that red-red restriction
		if (!node.parent.isBlack) { //parent is red, so grandparent exists and is black
			Node grandparent = node.parent.parent;
			if (!grandparent.left.isBlack && !grandparent.right.isBlack) { //both children of grandparent are red
				//we can make grandparent red and both of its kids black. This fixes the original problem of node vs node.parent.
				//However, grandparent may now be in conflict with its parent, so we have to call fixAdd on grandparent
				grandparent.left.isBlack = true; //first child is black
				grandparent.right.isBlack = true; //second child is black
				grandparent.isBlack = false; //grandparent is red
				fixAdd(grandparent); //call balancing method on grandparent
			}
			else { //one of the children of the grandparent is black and the other is red (the red one is node's parent)
				if (grandparent.left.right == node) { //we want node on the outside, not the inside, so rotate outwards = left
					leftRotation(grandparent.left);
				}
				else if (grandparent.right.left == node) { //we want node on the outside, not the inside, so rotate outwards = right
					rightRotation(grandparent.right);
				}
				if (grandparent.left == node || grandparent.left.left == node) { //node is on the left side of grandparent
					//both grandparent.left and grandparent.left.left are red
					grandparent.isBlack = false; //grandparent is red
					grandparent.left.isBlack = true; //grandparent.left is black
					rightRotation(grandparent);
					//now there are no two bordering red nodes
				}
				else { //node is on the right side of grandparent
					//both grandparent.right and grandparent.right.right are red
					grandparent.isBlack = false; //grandparent is red
					grandparent.right.isBlack = true; //grandparent.right is black
					leftRotation(grandparent);
					//now there are no two bordering red nodes
				}
			}
		}
	}
	private void fixDelete(Node node) { //balancing method
		//make the given node able to be safely deleted
		//if this method was called, node should be black and not a leaf
		if (!node.isBlack || node.isLeaf()) {
			System.out.println("fixDelete called in error.");
			return;
		}
		//if node is the root, deleting it is not a problem because this will affect every node's black height equally
		if (node.parent == null) {
			return;
		}
		//node has a parent, which means it must have a non-leaf sibling
		Node sibling;
		if (node.parent.left == node) { //node is the left child of its parent
		  sibling = node.parent.right;
		  //we know that having node.parent be red is a desirable situation for balancing
		  //if node.parent is black and sibling is red, we can rotate left at node.parent and recolor to get the favorable orientation
		  //we also need to reassign sibling based on the rotation
		  if (!sibling.isBlack) { //sibling is red, so node.parent cannot be
		    sibling.isBlack = true; //sibling becomes black
		    node.parent.isBlack = false; //parent becomes red
		    sibling = sibling.left; //sibling's left child becomes new sibling
		    leftRotation(node.parent);
		  }
		  //if sibling, parent, and both of sibling's children are all black, recoloring sibling red will make the parent subtree stable
		  //however, parent will now be out of alignment with the rest of the tree, so the method will be called again on parent
		  //after parent is fixed, the tree will be aligned and our balancing work will be done
		  if (sibling.isBlack && sibling.left.isBlack && sibling.right.isBlack && node.parent.isBlack) {
		    sibling.isBlack = false; //sibling is red, and everything under node.parent has the same blackHeight
		    fixDelete(node.parent); //call the method again on node.parent
		    return;
		  }
		  //if parent is red and sibling and its children are black, we can add one black node to the node's path by swapping the colors of parent and sibling
		  //this will not affect the black height of sibling or any other nodes, so we are done balancing
		  if (!node.parent.isBlack && sibling.left.isBlack && sibling.right.isBlack) {
		    node.parent.isBlack = true; //parent becomes black
		    sibling.isBlack = false; //sibling becomes red
		    return;
		  }
		  //if sibling has one red child and one black child, we want the red one to be on the outside for future rotation purposes.
		  //We can rotate right and recolor to preserve the validity of the tree, and we need to reassign the sibling after the rotation
		  if (sibling.isBlack && !sibling.left.isBlack && sibling.right.isBlack) {
		    sibling.left.isBlack = true; //inner red child becomes black
		    sibling.isBlack = false; //sibling (future sibling.right) becomes red
		    rightRotation(sibling);
		    sibling = node.parent.right; //reassign sibling
		  }
		  //the final possibility is that sibling is black and has a red outer (right) child.
		  //We can then rotate left on parent and recolor to preserve the structure of the tree.
		  if (sibling.isBlack && !sibling.right.isBlack) {
		    sibling.right.isBlack = true; //red outer child becomes black
		    sibling.isBlack = node.parent.isBlack; //sibling takes parent's color
		    node.parent.isBlack = true; //parent is black
		    leftRotation(node.parent);
		  }
		}
		else { //node is the right child of its parent
		  sibling = node.parent.left;
		  //we know that having node.parent be red is a desirable situation for balancing
		  //if node.parent is black and sibling is red, we can rotate right at node.parent and recolor to get the favorable orientation
		  //we also need to reassign sibling based on the rotation
		  if (!sibling.isBlack) { //sibling is red, so node.parent cannot be
		    sibling.isBlack = true; //sibling becomes black
		    node.parent.isBlack = false; //parent becomes red
		    sibling = sibling.right; //sibling's right child becomes new sibling
		    rightRotation(node.parent);
		  }
		  //if sibling, parent, and both of sibling's children are all black, recoloring sibling red will make the parent subtree stable
		  //however, parent will now be out of alignment with the rest of the tree, so the method will be called again on parent
		  //after parent is fixed, the tree will be aligned and our balancing work will be done
		  if (sibling.isBlack && sibling.left.isBlack && sibling.right.isBlack && node.parent.isBlack) {
		    sibling.isBlack = false; //sibling is red, and everything under node.parent has the same blackHeight
		    fixDelete(node.parent); //call the method again on node.parent
		    return;
		  }
		  //if parent is red and sibling and its children are black, we can add one black node to the node's path by swapping the colors of parent and sibling
		  //this will not affect the black height of sibling or any other nodes, so we are done balancing
		  if (!node.parent.isBlack && sibling.left.isBlack && sibling.right.isBlack) {
		    node.parent.isBlack = true; //parent becomes black
		    sibling.isBlack = false; //sibling becomes red
		    return;
		  }
		  //if sibling has one red child and one black child, we want the red one to be on the outside for future rotation purposes.
		  //We can rotate left and recolor to preserve the validity of the tree, and we need to reassign the sibling after the rotation
		  if (sibling.isBlack && sibling.left.isBlack && !sibling.right.isBlack) {
		    sibling.right.isBlack = true; //inner red child becomes black
		    sibling.isBlack = false; //sibling (future sibling.left) becomes red
		    leftRotation(sibling);
		    sibling = node.parent.left; //reassign sibling
		  }
		  //the final possibility is that sibling is black and has a red outer (left) child.
		  //We can then rotate right on parent and recolor to preserve the structure of the tree.
		  if (sibling.isBlack && !sibling.left.isBlack) {
		    sibling.left.isBlack = true; //red outer child becomes black
		    sibling.isBlack = node.parent.isBlack; //sibling takes parent's color
		    node.parent.isBlack = true; //parent is black
		    rightRotation(node.parent);
		  }
		}
	}
	public void printPreOrder(Node node) { //helper method
		if (node.isLeaf()) { //halting condition
			return;
		}
		System.out.println(node.data+", "+node.isBlack); //print the current node
		printPreOrder(node.left); //explore the left subtree
		printPreOrder(node.right); //explore the right subtree
	}
}