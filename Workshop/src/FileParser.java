import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;

public class FileParser implements Runnable {
	
	private int shingleSize;
	private File f; //used to be type File - he changed it to type string after writing Shingle Class
	private BlockingQueue<Shingle>b;
	private Deque<String> buffer = new LinkedList<>();
	private int docId;
	
	
	public FileParser(String file) throws Exception{
		this.f = new File(file);
	}
	
	public FileParser(String file, int shingleSize, BlockingQueue<Shingle> queue) {
		// TODO Auto-generated constructor stub
		this.f = new File(file);
		this.shingleSize = shingleSize;
		this.b = queue;
	}

	public void run(){
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
	
	String line = null;
	
	try {
		while((line = br.readLine()) != null){
			
			String uLine = line.toUpperCase();
			
			String[] words = uLine.split(".");
			
			addWordsToBuffer(words);
			
			Shingle s = getNextShingle();
			
			b.put(s);
			
			/*
			for(int i = 0; i <shingleSize;i++){
				
				//Convert string to an int
			//	int shingle = makeShingleSizeStrings.toHashCode()
			}
			*/
			
		}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}//while
	try {
		flushBuffer();
	} catch (InterruptedException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	try {
		br.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
}//run
	
	private void addWordsToBuffer(String [] words) {
		for(String s : words) {
			buffer.add(s);
		}
  
        }

  	private Shingle getNextShingle() {
		StringBuffer sb = new StringBuffer();
		int counter = 0;
		while(counter < shingleSize) {
			if(buffer.peek() != null) {
				sb.append(buffer.poll());
				counter++;
			}
		}  
		if (sb.length() > 0) {
			return(new Shingle(docId, sb.toString().hashCode()));
		}
		else {
			return(null);
		}
  	} // Next shingle
	

	private void flushBuffer() throws InterruptedException {
		while(buffer.size() > 0) {
			Shingle s = getNextShingle();
			if(s != null) {
				b.put(s);
			}
			else {
				b.put(new Poison(docId, 0));
			}
		}
	}

}
