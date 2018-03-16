package ifttt;

import java.io.File;

@SuppressWarnings("serial")
public class NewFile extends File{

	public NewFile(String pathname) {
		super(pathname);
		// TODO Auto-generated constructor stub
	}
	
	boolean isFile(File f){
		synchronized(f){
			return f.isFile();
		}
	}
	
	boolean isDirectory(File f){
		synchronized(f){
			return f.isDirectory();
		}
	}
	
	long lastModified(File f){
		synchronized(f){
			return f.lastModified();
		}
	}
	 
	String getName(File f){
		synchronized(f){
			return f.getName();
		}
	}
	
	long length(File f){
		synchronized(f){
			return f.length();
		}
	}
	
	String getParent(File f){
		synchronized(f){
			return f.getParent();
		}
	}
	
	File getParentFlie(File f){
		synchronized(f){
			return f.getParentFile();
		}
	}
	
	File[] listFiles(File f){
		synchronized(f){
			return f.listFiles();
		}
	}
	
	String getPath(File f){
		synchronized(f){
			return f.getPath();
		}
	}
	
	boolean exists(File f){
		synchronized(f){
			return f.exists();
		}
	}
	

}
