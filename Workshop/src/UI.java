import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.text.html.parser.DocumentParser;

public class UI {
	
	Scanner s = new Scanner(System.in);
	
	//BlockingQueue queue = new LinkedBlockingQueue(100);//No idea where this goes
	public void show() {
		
		BlockingQueue<Shingle>queue = null;
		
		System.out.println("1 to exit");
		 int keepMenuAlive = s.nextInt();
		
		while(keepMenuAlive!=1){
			
			////get the file names
			
			Thread t1 = new Thread(new FileParser("ddsdc.txt", 3, queue));
			//Thread t2 = new Thread((Runnable) new FileParser(f2, q, shingleSize, k), "T2");
			
			
				
			
		}
		
	}

}
