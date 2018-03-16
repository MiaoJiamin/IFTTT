package ifttt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;

public class Record_detail extends Thread{
	
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
	
	synchronized void printrenamed(File file, File filetemp){
		try{
			Thread.sleep(500);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		toFile("renamed: " + file.getName() + " --> " + filetemp.getName() + "\r\n","D:\\record-detail.txt");
//		System.out.println("renamed: " + file.getName() + " --> " + filetemp.getName());
	}
	
	synchronized void printModified(File file, File filetemp,long time){
		try{
			Thread.sleep(1000);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		Date date1 = new Date(time);
		Date date2 = new Date(filetemp.lastModified());
		toFile("Modified: " + "[" + file + "] " + date1 + " --> " + date2 + "\r\n","D:\\record-detail.txt");
//		System.out.println("Modified: " + "[" + file + "] " + date1 + " --> " + date2);
	}
	
	synchronized void printpath_changed(File file, File filetemp){
		try{
			Thread.sleep(1000);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
		toFile("path_changed: " + file.getPath() + " --> " + filetemp.getPath() + "\r\n","D:\\record-detail.txt");
//		System.out.println("path_changed: " + file.getPath() + " --> " + filetemp.getPath());
	}
	
	synchronized void printsize_changed(File file, File filetemp, long size1, long size2){
		try{
			Thread.sleep(1000);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
		toFile("size_changed: " + "[" + filetemp + "] " + size1 + " --> " + size2 + "\r\n","D:\\record-detail.txt");
//		System.out.println("size_changed: " + "[" + filetemp + "] " + size1 + " --> " + size2);
	}
}
