/*
package ie.gmit.sw;

public class MinHasher implements Runnable {

	private int[] hashes;
	
	public MinHasher(int[] hashes, Shingle s){
		this.hashes = hashes;
	}
	
	public void run(Shingle s){
		
		
		for(int i =0; i<hashes.length;i++){
			int hash = s.getShingleHashCode()^hashes[i];
			
		}
		
		
		
		
		
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
}
*/
package ie.gmit.sw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * 
 * 
 * Takes values from Blocking Queue and hases them to create the minimum hash values,
 * which are then stored on a map.
 * 
 * 
 * @author Conor Raftery
 */
public class MinHasher implements Runnable {

	private BlockingQueue<Shingle> q;
	private int k;
	private int[] minhashes;
	private Map<Integer, List<Integer>> map = new ConcurrentHashMap<Integer, List<Integer>>();
	private ExecutorService pool;

	/**
	 * Gets map from with List
	 * 
	 * @return map new {@link Map} object
	 *
	 * */
	public Map<Integer, List<Integer>> getMap() {
		return map;
	}

	/**
	 * Constructor with fields
	 * 
	 * @param q
	 *            A BlockingQueue of Shingle Objects
	 * @param k
	 *            The amount of minHashes
	 *            
	 * @param poolSize
	 *            The size of the thread pool
	 *
	 * */
	public MinHasher(BlockingQueue<Shingle> q, int k, int poolSize) {
		this.q = q;
		this.k = k;
		pool = Executors.newFixedThreadPool(poolSize);
		init();
	}

	/**
	 * Gets {@link Random} minhash
	 * 
	 *
	 *
	 * */
	public void init() {
		Random random = new Random();//Code adapted from method in Runner class
		minhashes = new int[k];
		for (int i = 0; i < minhashes.length; i++) {
			minhashes[i] = random.nextInt();
		}
	}
	
	/**
	 * Creates 2 lists for comparing
	 * 
	 * 
	 *
	 * */
	public void run() {
		List<Integer> alist = new ArrayList<>();
		List<Integer> blist = new ArrayList<>();

		int docCount = 2;
		while (docCount > 0) {
			try {
				Shingle s = q.take();
				//like poison, if its not finished it keeps going
				if (s instanceof Poison ) {
					//System.out.println("s is instance of poison");
					docCount--;

				} else {
					pool.execute(new Runnable() {

						@Override
						public void run() {
							//System.out.println("Starting run" + Thread.currentThread());
							//List<Integer> list = map.get(s.getDocId());
							int minValue = Integer.MAX_VALUE;
							for (int i = 0; i < k; i++) {
								int value = s.getShingleHashCode() ^ minhashes[i];
								//if (list == null) {
									// list = new
									// ArrayList<Integer>(Collections.nCopies(k,
									// Integer.MAX_VALUE));
									// map.put(s.getDocId(), list);
								//} else {
									if (value < minValue) {
										minValue = value;
									}
								//}
				//				System.out.println("In loop");
							} // for
							if(s.getDocId()==1){
								alist.add(minValue);
							}else if (s.getDocId()==2){
								blist.add(minValue);

							}
			//				System.out.println("Out of loop");
							//map.put(s.getDocId(), list);
						}
					});
				} // else

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		//System.out.println("Before Jaccard");
		pool.shutdown();
		try {
			pool.awaitTermination(60, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//boolean isEmpty1 = map.containsKey(1);
		//boolean isEmpty = map.containsKey(2);
		map.put(1, alist);
		map.put(2, blist);
		
	//	System.out.println("DEBUG___" + isEmpty + "_____" + isEmpty1);
		List<Integer> tempdata = map.get(1);
		tempdata.retainAll(map.get(2));
		float jaccard = (float) tempdata.size() / (alist.size() + blist.size() - (float) tempdata.size());

		//to get the percentage

		System.out.format("J: %.2f", jaccard);

	}
}