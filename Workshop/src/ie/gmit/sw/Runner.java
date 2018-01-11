package ie.gmit.sw;
import java.util.Random;

/**
 * 
 * Runner is used to run the UserInterace
 * 
 * @author Conor Raftery
 * 
 * */
public class Runner {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		new UI().show();

	}


//==================PLACE METHOD INTO RIGHT CLASS, NOT USED======================

	public int[] getMinHashSet(){
		
		int[] minhashes = null;
		
		Random r = new Random();
		
		int[] hashes = new int[minhashes.length]; //size = minhashes.length
		
		for(int i= 0; i < minhashes.length; i++){
			hashes[i] = r.nextInt();
		}
		return hashes;
	}
}