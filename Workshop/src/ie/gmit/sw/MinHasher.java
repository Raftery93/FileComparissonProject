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

public class MinHasher implements Runnable {

	private BlockingQueue<Shingle> q;
	private int k;
	private int[] minhashes;
	private Map<Integer, List<Integer>> map = new ConcurrentHashMap<Integer, List<Integer>>();
	private ExecutorService pool;

	public Map<Integer, List<Integer>> getMap() {
		return map;
	}

	public MinHasher(BlockingQueue<Shingle> q, int k, int poolSize) {
		this.q = q;
		this.k = k;
		pool = Executors.newFixedThreadPool(poolSize);
		init();
	}

	public void init() {
		Random random = new Random();
		minhashes = new int[k];
		for (int i = 0; i < minhashes.length; i++) {
			minhashes[i] = random.nextInt();
		}
	}

	public void run() {
		List<Integer> alist = new ArrayList<>();
		List<Integer> blist = new ArrayList<>();

		int docCount = 2;
		while (docCount > 0) {
			try {
				Shingle s = q.take();
				// works like poison if its not finished keeps going
				if (s instanceof Poison ) {
					//System.out.println("ddddoooccccccccccccccer");
					docCount--;

				} else {
					pool.execute(new Runnable() {

						@Override
						public void run() {
							//System.out.println("IN 2nd run" + Thread.currentThread());
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
				//				System.out.println("INNNNNNNNNNNNNNNN");
							} // for
							if(s.getDocId()==1){
								alist.add(minValue);
							}else if (s.getDocId()==2){
								blist.add(minValue);

							}
			//				System.out.println("kkkkkeeeedd");
							//map.put(s.getDocId(), list);
						}
					});
				} // else

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		//System.out.println("BEFFORE JJAAAAAAAAAAC");
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
		float jacquared = (float) tempdata.size() / (alist.size() + blist.size() - (float) tempdata.size());

		// to get the percentage

		System.out.format("J: %.2f", jacquared);

	}
}