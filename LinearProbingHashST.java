//Imani Palmer

public class LinearProbingHashST<Key, Value> implements STInterface<Key, Value>
{
	public int N;           // number of key-value pairs in the symbol table
	public int M;           // size of linear probing table
	public Key[] keys;      // the keys
	public Value[] vals;    // the values
	public int probe_count = 0; //probe count
	public boolean check = true; //Default to resize

    //Create linear proving hash table of given capacity
    public LinearProbingHashST(int capacity, boolean flag)
    {
        M = capacity;
        keys = (Key[])   new Object[M];
        vals = (Value[]) new Object[M];
        check = flag;
    }//constructor


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

	//Put the key, value pair into the ST
	public boolean contains(Key key)
	{
		return get(key) != null;
	}//contains

	// hash function for keys - returns value between 0 and M-1
	public int hash(Key key)
	{
	        return (key.hashCode() & 0x7fffffff) % M;
    }//hash

     // resize the hash table to the given capacity by re-hashing all of the keys
	 public void resize(int capacity, boolean flag)
	 {
		 	//If flag is false, do not resize
	        if(flag == false)
	        {
				return;
			}//if
			else
			{
	        	LinearProbingHashST<Key, Value> temp = new LinearProbingHashST<Key, Value>(capacity, flag);
	        	for (int i = 0; i < M; i++)
	        	{
	        	    if (keys[i] != null)
	        	    {
	        	        temp.put(keys[i], vals[i]);
	        	    }//if
	        	}//for
	        	keys = temp.keys;
	        	vals = temp.vals;
	        	M    = temp.M;
			}//else
    }//resize

	//Put the key, value pair into the ST
	public void put(Key key, Value val)
	{
		probe_count = 0;
		if (val == null) delete(key);

	        // double table size if 50% full
	        if (N >= M/2) resize(2*M, check);

	        int i;
	        for (i = hash(key); keys[i] != null; i = (i + 1) % M)
	        {
				probe_count++;
	            if (keys[i].equals(key))
	            {
					vals[i] = val; return;
				}//if
	        }//for
	        keys[i] = key;
	        vals[i] = val;
	        N++;
    }//put

	//Retrieve the value associate with the key, or null if the key is not found
	public Value get(Key key)
	{
		probe_count = 0;
		for (int i = hash(key); keys[i] != null; i = (i + 1) % M)
		{
				probe_count++;
		        if (keys[i].equals(key))
		        {
		            return vals[i];
				}//if
		}//for
        return null;
	}//get

	//Remove the key from the ST
	public void delete(Key key)
	{
		if (!contains(key)) return;

		        // find position i of key
		        int i = hash(key);
		        while (!key.equals(keys[i]))
		        {
		            i = (i + 1) % M;
		        }//while

		        // delete key and associated value
		        keys[i] = null;
		        vals[i] = null;

		        // rehash all keys in same cluster
		        i = (i + 1) % M;
		        while (keys[i] != null)
		        {
		            // delete keys[i] an vals[i] and reinsert
		            Key   keyToRehash = keys[i];
		            Value valToRehash = vals[i];
		            keys[i] = null;
		            vals[i] = null;
		            N--;
		            put(keyToRehash, valToRehash);
		            i = (i + 1) % M;
		        }//while

		        N--;

		        // halves size of array if it's 12.5% full or less
		        if (N > 0 && N <= M/8) resize(M/2, false);

        assert check();
	}//delete

	 // return all of the keys as in Iterable
	    public Iterable<Key> keys()
	    {
	        Queue<Key> queue = new Queue<Key>();
	        for (int i = 0; i < M; i++)
	            if (keys[i] != null) queue.enqueue(keys[i]);
	        return queue;
	    }//queue

	    // integrity check - don't check after each put() because
	    // integrity not maintained during a delete()
	    public boolean check()
	    {

	        // check that hash table is at most 50% full
	        if (M < 2*N) {
	            System.err.println("Hash table size M = " + M + "; array size N = " + N);
	            return false;
	        }//if

	        // check that each key in table can be found by get()
	        for (int i = 0; i < M; i++) {
	            if (keys[i] == null) continue;
	            else if (get(keys[i]) != vals[i]) {
	                System.err.println("get[" + keys[i] + "] = " + get(keys[i]) + "; vals[i] = " + vals[i]);
	                return false;
	            }//else if
	        }//for
	        return true;
	    }//check


	//Return the number of probes required in the last lookup(either contains() or get()
	public int getProbes()
	{
		return probe_count;
	}//getProbes
}//class