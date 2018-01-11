package ie.gmit.sw;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.text.html.parser.DocumentParser;
/**
 * 
 * UI represents a user interface
 * 
 * @author connor raftery
 * 
 * */
public class UI {
	
	Scanner s = new Scanner(System.in);
	
	//BlockingQueue queue = new LinkedBlockingQueue(100);//No idea where this goes
	public void show() {
		
		BlockingQueue<Shingle>queue = new LinkedBlockingQueue<>();
		
		System.out.println("1)New Test\n-1)exit\n");
		 int keepMenuAlive = s.nextInt();
		
		while(keepMenuAlive!=-1){
			
			////get the file names
			
			System.out.println("Please enter the name of first file:\n");
			String file1 = s.next();
			
			System.out.println("Please enter the name of second file:\n");
			String file2 = s.next();
			
			System.out.println("Please enter the size of Shingle size:\n");
			int shingleSize = s.nextInt();
			
			//Thread t1 = new Thread(new FileParser(queue, file1, shingleSize), "A");
			
			Thread t1 = new Thread(new FileParser(file1, shingleSize, queue, 1), "A");
			Thread t2 = new Thread(new FileParser(file2, shingleSize, queue, 2), "B");
			//Thread t2 = new Thread((Runnable) new FileParser(f2, q, shingleSize, k), "T2");
			t1.start();
			t2.start();
			try {
				t1.join();
				t2.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Thread t3 = new Thread(new MinHasher(queue, shingleSize, shingleSize));
			//System.out.println(t1);
			//System.out.println(t2);
			t3.start();
			
			System.out.println("\n1)New Test\n-1)exit/n");
			keepMenuAlive = s.nextInt();
			
		}
		
	}

}
