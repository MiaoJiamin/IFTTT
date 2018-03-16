package ifttt;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;


public class Record_summary extends Thread {
	private int modified;
	private int renamed;
	private int pathchanged;
	private int sizechanged;
	
	public static void toFile(String str, String path){
		Charset charset = Charset.forName("UTF-8");
		try{
			FileOutputStream out = new FileOutputStream(path, true); 
			out.write(str.getBytes(charset)); 
			out.close();    
		} catch (IOException x) {
		    System.err.format("IOException: %s%n", x);
		}
	}
	
	
	synchronized void printtimes(){
		try{
			toFile("renamed: " + this.renamed + "\r\n","D:\\record-summary.txt");
			toFile("Modified: " + this.modified + "\r\n","D:\\record-summary.txt");
			toFile("path_changed: " + this.pathchanged + "\r\n","D:\\record-summary.txt");
			toFile("size_changed: " + this.sizechanged + "\r\n","D:\\record-summary.txt");
		    sleep(10);
		}catch (InterruptedException e) {
			e.printStackTrace();
		}
		

		
//		System.out.println("renamed: " + this.renamed);
//		System.out.println("Modified: " + this.modified);
//		System.out.println("path_changed: " + this.pathchanged);
//		System.out.println("size_changed: " + this.sizechanged);
	}
	
	
	int getmodified(){
		return modified;
	}
	void setmodified(int a){
		modified = a;
	}
	
	int getrenamed(){
		return renamed;
	}
	void setrenamed(int a){
		renamed = a;
	}
	
	int getpathchanged(){
		return pathchanged;
	}
	void setpathchanged(int a){
		pathchanged = a;
	}
	
	int getsizechanged(){
		return sizechanged;
	}
	void setsizechanged(int a){
		sizechanged = a;
	}
}


