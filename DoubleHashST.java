//Imani Palmer
import java.math.*;
import java.util.*;

public class DoubleHashST<Key, Value> implements STInterface<Key, Value>
{
	//Number of key-value pairs
	public int N  = 0;
	//Hash table size
	public int M;
	//Rehash Table Size
	public int D;
	//The keys
	public Key[] keys;
	//The values
	public Value[] vals;
	//Number of probes
	public int probe_count;
	//Decide if resizing if allowed
	public boolean check = true;
	//Holds table count, and state
	HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
	//Deleted item count
	public int delete_count = 0;
	public int oldM;

	public DoubleHashST(int capacity)
	{
		//Find the next greatest prime
		greaterThanPrime(capacity);
		//Find the next lesser prime, set D
		lessThanPrime(capacity);
		//Set the number of keys
		keys = (Key[]) new Object[M];
		//Set the number of values
		vals = (Value[]) new Object[M];
		//Create a hashMap full of empty states
		emptyStates(M);
	}//constructor

	//Create a hashtable of given capacity
	public DoubleHashST(int capacity, boolean flag)
	{
		//Find the next greatest prime
		greaterThanPrime(capacity);
		//Find the next lesser prime
		lessThanPrime(capacity);
		//Set the number of keys
		keys = (Key[]) new Object[M];
		//Set the number of values
		vals = (Value[]) new Object[M];
		//Keep track of resize
		check = flag;
		//Create a hashMap full of empty states
		emptyStates(M);
	}//DoubleHashST

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

	//First time has the code returns value between 0 and M-1
	public int hash(Key key)
	{
		return (key.hashCode() & 0x7fffffff) % M;
    }//hash

    // resize the hash table to the given capacity by re-hashing all of the keys
    public void resize(int capacity)
    {
		System.out.println("Resizing from M = " + M + " and N = " + N);

		//Do not resize
		if(check == false)
		{
			return;
		}//if
		//Resize is allowed
		else
		{
			oldM = M;
			capacity = (2 * M);
			greaterThanPrime(capacity);
			//Set D
			lessThanPrime(capacity);

			//Pass the greatest prime number from table size
			DoubleHashST<Key, Value> temp = new DoubleHashST<Key, Value>(M, check);
			//Set all the positions to empty
			emptyStates(M);

			for (int i = 0; i < oldM; i++)
			{
				if (keys[i] != null)
				{
					temp.put(keys[i], vals[i]);
					//Store positions as full
					hm.put(i, 1);
				}//if
			}//for
			 keys = temp.keys;
			 vals = temp.vals;
			 M = temp.M;
		 }//else
		System.out.println("\t New Values: " + M + " and D = " + D);
	 }//resize

    //Put the key, value pair into the ST
	public void put(Key key, Value val)
	{
		probe_count = 0;
		if (val == null) delete(key);

		  //If the table gets more than 3/4 full resize it
		  //twice its previous sice
		  if (N > (3 * M) / 4) {resize(2*M);}

		  int i;

		  System.err.println("Hash " + hash(key));
		  System.err.println(hm.get(hash(key)) == null);
		  System.err.println("D " + D);
		  System.err.println("M " + M);
		  for (i = hash(key); keys[i] != null; i = (i + (D - (key.hashCode() & 0x7fffffff) % D)) % M)
		  {
			  System.err.println("D again: " + D);
		  	  System.err.println("M  again: " + M);
			  System.err.println("I " + i);

			  probe_count++;
			  if (keys[i].equals(key))
			  {
				  System.err.println("IF STATEMENT");
				  vals[i] = val;
				  return;
			  }//if
			  System.err.println("I again " + i);
		}//for
		System.out.println("Out of for loop");
		N++;
		keys[i] = key;
		vals[i] = val;
		//Set this position to full
		hm.put(i, 1);
	}//put

	//Retrieve the value associate with the key, or null if the key is not found
	public Value get(Key key)
	{
		probe_count = 0;
		for (int i = hash(key); keys[i] != null; i = (i + (D - (key.hashCode() & 0x7fffffff) % D)) % M)
		{
			probe_count++;
			if (keys[i].equals(key))
			{
				return vals[i];
			}
		}//for
		return null;
	}//get

	//Remove the key from the ST
	public void delete(Key key)
	{
		//Count the number of deleted locations
		//If the table gets less than or equal to 1/8 full
		//Resize it to 1/2 its previous size
		for(int i = 0; i < M; i++)
		{
			if(hm.get(i) == null)
			{
				break;
			}
			if(hm.get(i) == 2)
			{
				delete_count++;
			}
		}
		if(delete_count > (1/(4*N)))
		{

			System.out.println("Removing Deleted Items");
			resize(oldM);
		}

		//If the table gets less than or equal to 1/8 full
		//resize it to 1/2 its previous size!!!
		if (N >= (M / 8)) resize(M / 2);

		//If this key does not exist return
		if (!contains(key)) return;

		// find position i of key
		int i = hash(key);

		while (!key.equals(keys[i]))
		{
			i = (i + (D - (key.hashCode() & 0x7fffffff) % D)) % M;
		}//while
		// delete key and associated value
		keys[i] = null;
		vals[i] = null;
		//Set this value as deleted
		hm.put(i, 2);

		// rehash all keys in same cluster
		i = (i + (D - (key.hashCode() & 0x7fffffff) % D)) % M;
		while (keys[i] != null)
		{
			// delete keys[i] an vals[i] and reinsert
			Key   keyToRehash = keys[i];
			Value valToRehash = vals[i];
			keys[i] = null;
			vals[i] = null;
			//Set values as deleted and reinsert
			//Set this value as deleted
			hm.put(i, 2);
			N--;
			//HashMap is rehashed in put
			put(keyToRehash, valToRehash);
			i = (i + (D - (key.hashCode() & 0x7fffffff) % D)) % M;
		}//while
		N--;
	}//delete

	// return all of the keys as in Iterable
	public Iterable<Key> keys()
	{
		Queue<Key> queue = new Queue<Key>();
		for (int i = 0; i < M; i++)
			if (keys[i] != null) queue.enqueue(keys[i]);
		   	return queue;
	}//iterable

	// integrity check - don't check after each put() because
	// integrity not maintained during a delete()
	public boolean check()
	{
		// check that hash table is at most 50% full
		if (M < 2*N)
		{
			System.err.println("Hash table size M = " + M + "; array size N = " + N);
			return false;
		}//if

		// check that each key in table can be found by get()
		for (int i = 0; i < M; i++)
		{
			if (keys[i] == null)
			{
				continue;
			}
		    else if (get(keys[i]) != vals[i])
		    {
				System.err.println("get[" + keys[i] + "] = " + get(keys[i]) + "; vals[i] = " + vals[i]);
				return false;
		    }//elseif
		}//for
		return true;
	}//check

	public int getProbes()
	{
		return probe_count;
	}//getProbes

	public void emptyStates(int size)
	{
		//Resize hashMap of position and states
		hm = new HashMap<Integer, Integer>(size);
		for(int i = 0; i < size; i++)
		{
			hm.put(i,0);
		}
	}

	public void lessThanPrime(int size)
	{
		//Check make sure the table size
		//The smallest prime number greater than
		//or equal to capacity
		//Check if capacity is prime
		BigInteger bigNum = BigInteger.valueOf(size-1);
		//This is not prime
		//If even
		if((size % 2) == 0)
		{
			while(!bigNum.isProbablePrime(1))
			{
				//Continually increase number until prime
				size--;
				bigNum = BigInteger.valueOf(size);
			}//while
		}//if
		//This is odd
		else
		{
			while(!bigNum.isProbablePrime(1))
			{
				//Continually increase number by 2 until prime
				size = size - 2;
				bigNum = BigInteger.valueOf(size);
			}//while
		}//else
		//Convert size back to int
		D = bigNum.intValue();
	}//greaterThanPrimee

	public void greaterThanPrime(int size)
	{
		//Check make sure the table size
		//The smallest prime number greater than
		//or equal to capacity
		//Check if capacity is prime
		BigInteger bigNum = BigInteger.valueOf(size-1);
		//This is not prime
		//If even
		if((size % 2) == 0)
		{
			while(!bigNum.isProbablePrime(1))
			{
				//Continually increase number until prime
				size++;
				bigNum = BigInteger.valueOf(size);
			}//while
		}//if
		//This is odd
		else
		{
			while(!bigNum.isProbablePrime(1))
			{
				//Continually increase number by 2 until prime
				size = size + 2;
				bigNum = BigInteger.valueOf(size);
			}//while
		}//else
		//Convert size back to int
		M = bigNum.intValue();
	}//greaterThanPrime
}//DoubleHashSt