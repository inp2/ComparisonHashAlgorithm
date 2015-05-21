//Imani Palmer

public class SequentialSearchST<Key, Value>
{
    private int N;           // number of key-value pairs
    private Node first;      // the linked list of key-value pairs
	public int probe_count = 0;

    // a helper linked list data type
    private class Node
    {
        private Key key;
        private Value val;
        private Node next;

        public Node(Key key, Value val, Node next)
        {
            this.key  = key;
            this.val  = val;
            this.next = next;
        }
    }

    // return number of key-value pairs
    public int size() { return N; }

    // is the symbol table empty?
    public boolean isEmpty() { return size() == 0; }

    // does this symbol table contain the given key?
    public boolean contains(Key key) {
        return get(key) != null;
    }

    // return the value associated with the key, or null if the key is not present
    public Value get(Key key) {
		probe_count = 1;
        for (Node x = first; x != null; x = x.next)
        {
            if (key.equals(x.key))
            {
				return x.val;
			}
			probe_count++;
        }
        return null;
    }

    // add a key-value pair, replacing old key-value pair if key is already present
    public void put(Key key, Value val)
    {
		//probe_count = 1;
        if (val == null) { delete(key); return; }
        for (Node x = first; x != null; x = x.next)
        {

            if (key.equals(x.key))
            {
				x.val = val;
				return;
			}
			probe_count++;
		}
		first = new Node(key, val, first);
        N++;
    }

    // remove key-value pair with given key (if it's in the table)
    public void delete(Key key) {
        first = delete(first, key);
    }

    // delete key in linked list beginning at Node x
    // warning: function call stack too large if table is large
    private Node delete(Node x, Key key) {
        if (x == null) return null;
        if (key.equals(x.key)) { N--; return x.next; }
        x.next = delete(x.next, key);
        return x;
    }


    // return all keys as an Iterable
    public Iterable<Key> keys()  {
        Queue<Key> queue = new Queue<Key>();
        for (Node x = first; x != null; x = x.next)
            queue.enqueue(x.key);
        return queue;
    }

	//Return the number of probes required in the last lookup
	public int getProbes()
	{
		return probe_count;
	}//getProbes
}
