public class Node<T extends Comparable<T>> {

	public Comparable<T>[] keys;
	public Node<T>[] children;
	public boolean leaf;
	public int numberOfKeys;
	public Node<T> parent; 
	public int maxKeys;
	public int m;

	/**
	 * 
	 * @param m
	 */
	@SuppressWarnings("unchecked")
	public Node(int m) {
		
		keys = new Comparable[m-1];
		children = new Node[m];
		numberOfKeys = 0;
		maxKeys = m-1;
		this.m = m;

	}

	@Override
	public String toString() {
		String res = "[";
		for (int i = 0; i < keys.length; i++) {
			if (keys[i] != null)
				res += keys[i];
			else
				res += "null";
			res += ",";
		}
		if (res.length() > 1) {
			res = res.substring(0, res.length() - 1);
		}
		return res + "]";
	}

}