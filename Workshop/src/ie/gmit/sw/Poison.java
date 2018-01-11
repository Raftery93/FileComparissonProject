package ie.gmit.sw;

import java.util.Map;

/**
 * Used to represent words as hash codes
 * 
 * 
 *@author Conor Raftery
 * */
public class Poison extends Shingle {


	/**
	 * Constructor with fields
	 * 
	 *  @param docId
	 *            The ID given to the document
	 * 
	 * @param shingleHashCode
	 *            An array of Strings represented by a hashcode
	 * 
	 * */
	public Poison(int docId, int shingleHashCode) {
		super(docId, shingleHashCode);
		
	}
	


}
