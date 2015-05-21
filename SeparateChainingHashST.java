//Imani Palmer

public class SeparateChainingHashST<Key, Value> implements STInterface<Key, Value>
{
    public int N;                                // number of key-value pairs
    public int M;                                // hash table size
    public SequentialSearchST<Key, Value>[] st;  // array of linked-list symbol tables
    public int probe_count = 0;

    // create separate chaining hash table with M lists
    public SeparateChainingHashST(int M)
    {
        this.M = M;
        st = (SequentialSearchST<Key, Value>[]) new SequentialSearchST[M];
        for (int i = 0; i < M; i++)
            st[i] = new SequentialSearchST<Key, Value>();
    }//constructor

    // resize the hash table to have the given number of chains b rehashing all of the keys
    public void resize(int chains, boolean flag)
    {
        SeparateChainingHashST<Key, Value> temp = new SeparateChainingHashST<Key, Value>(chains);
        for (int i = 0; i < M; i++)
        {
            for (Key key : st[i].keys())
            {
                temp.put(key, st[i].get(key));
            }//for
        }//for
        this.M  = temp.M;
        this.N  = temp.N;
        this.st = temp.st;
    }//resize

    // hash value between 0 and M-1
    public int hash(Key key)
    {
        return (key.hashCode() & 0x7fffffff) % M;
    }//hash

    //How many items are in the ST?
    public int size()
    {
        return N;
    }//size

    //Is ST empty?
    public boolean isEmpty()
    {
        return size() == 0;
    }//isEmpty

    //Is the key in the ST?
    public boolean contains(Key key)
    {
        return get(key) != null;
    }//contains

    //Retrieve the value associated with the key , or null if the key is not found
    public Value get(Key key)
    {
        int i = hash(key);

		for(Key keys: st[i].keys())
		{
			if(key == keys)
			{
				break;
			}//if
		}//for
		probe_count = getProbes();
        return st[i].get(key);
    }//value

    //Put the key, value pair into the ST
    public void put(Key key, Value val)
    {
        if (val == null)
        {
			delete(key);
        	return;
        }//if
        int i = hash(key);
        if (!st[i].contains(key)) N++;
        probe_count = getProbes();
        st[i].put(key, val);
    }//put

    //Remove the key from the ST
    public void delete(Key key)
    {
        int i = hash(key);
        if (st[i].contains(key)) N--;
        st[i].delete(key);
    }//delete

    //Return an Iterable collection of the keys from the ST
    public Iterable<Key> keys()
    {
        Queue<Key> queue = new Queue<Key>();
        for (int i = 0; i < M; i++)
        {
            for (Key key : st[i].keys())
                queue.enqueue(key);
        }//for
        return queue;
    }//Iterable

    public int getProbes()
    {
		return probe_count;
	}
}//end