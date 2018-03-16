package ifttt;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class Size_changed extends Thread{
	private String path;
	private Record_summary summary;
	private Record_detail detail;
	private int sflag;
	private int dflag;
	private int rflag;
	
	private File[] FILES;
	private long[] TIME;
	private long[] SIZE;
	private String[] NAME;
	int count = 0;
	
	private File[] Folder;
	private long[] Folder_SIZE;
	int num = 0;
	long size = 0;
	
	Size_changed(String task,Record_summary summary,Record_detail detail){
		this.path = task.substring(task.indexOf("[")+1, task.indexOf("]"));	//得到监控范围
		this.summary = summary;
		this.detail = detail;
		FILES = new File[10000];
		TIME = new long[10000];
		SIZE = new long[10000];
		NAME = new String[10000];
		Folder = new File[10000];
		Folder_SIZE = new long[10000];
		if(task.matches(".*record_summary*."))
			this.sflag = 1;
		if(task.matches(".*record_detail*."))
			this.dflag = 1;
		if(task.matches(".*recover*."))
			this.rflag = 1;
	}
	
	
	public void run(){
		File file = new File(path);
		if(file.isFile()){
			file_size_changed(file);
		}else if(file.isDirectory()){
			traverseFolder(file);
			getFolder(file);
			dir_size_changed(file);
		}
	}
	

	public void traverseFolder(File file){
		File list[] = file.listFiles();
		for(int i = 0; i < list.length; i++){
			if(list[i].isDirectory()){
				traverseFolder(list[i]);
			}else{
				FILES[count] = list[i];
				TIME[count] = list[i].lastModified();
				SIZE[count] = list[i].length();
				NAME[count] = list[i].getName();
				count++;
			}
		}
	}
	
	
	public void file_size_changed(File file){
		long size = file.length();
		File parefile = new File(file.getParent());
		File[] file1 = parefile.listFiles();
		ArrayList<File> files = new ArrayList<File>(Arrays.asList(file1));
		
		while(true){
			File nfile = new File(path);
			if(nfile.exists()){
				if(size!=nfile.length()){
					record(file,nfile,size,nfile.length());
				}
				size = nfile.length();
				parefile = new File(nfile.getParent());
				file1 = parefile.listFiles();
				files = new ArrayList<File>(Arrays.asList(file1));
			}else if(!nfile.exists()){
				parefile = new File(nfile.getParent());
				file1 = parefile.listFiles();
				files = new ArrayList<File>(Arrays.asList(file1));
				if(!files.contains(nfile)){
					record(file,nfile,size,nfile.length());
				}
				break;
			}
			
			try{
				Thread.sleep(500);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
	
	

	public void getFolder(File file){
		File list[] = file.listFiles();
		for(int i = 0; i < list.length; i++){
			if(list[i].isDirectory()){
				Folder[num] = list[i];
				size = 0;
				Folder_SIZE[num] = getsize(list[i]);
				num ++;
				getFolder(list[i]);
			}else if(list[i].isFile()){
				continue;
			}
		}
	}
	
	public long getsize(File folder){
		File list[] = folder.listFiles();
		for(int i = 0; i < list.length; i++){
			if(list[i].isDirectory()){
				size = size + getsize(list[i]); 
			}else{
				size = size + list[i].length();
			}
		}
		return size;
	}
	

	public void dir_size_changed(File file){
		while(true){			
			for(int i = 0; i < count; i++){
				File filetemp = new File(FILES[i].getPath());
				if(FILES[i].exists()){
					if(filetemp.length() != SIZE[i]){
						record(FILES[i],filetemp,SIZE[i],filetemp.length());
						SIZE[i] = filetemp.length();
					}
					
				}else if(!FILES[i].exists()){
					record(FILES[i],filetemp,SIZE[i],filetemp.length());
				}
			}
			
			
			for(int j = 0; j < num; j++){
				File foldertemp = new File(Folder[j].getPath());

				if(Folder[j].exists()){
					size = 0;
					if(getsize(foldertemp) != Folder_SIZE[j]){
						size = 0;
						record(Folder[j],foldertemp,Folder_SIZE[j],getsize(foldertemp));
						size = 0;
						Folder_SIZE[j] = getsize(foldertemp);
					}
				}else if(!Folder[j].exists()){
					size = 0;
					record(Folder[j],foldertemp,Folder_SIZE[j],getsize(foldertemp));
				}
			}
			
			num = 0;
			getFolder(file);
			count = 0;
			traverseFolder(file);
			
			try{
				Thread.sleep(500);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
	
	
	
	
	
	
	public void record(File file, File filetemp, long size1,long size2){
		if(sflag == 1){
			summary.setsizechanged(summary.getsizechanged() + 1);
			summary.printtimes();
		}
		
		if(dflag == 1){
			detail.printsize_changed(file,filetemp,size1,size2);
		}
		
		if(rflag == 1){
			filetemp.renameTo(new File(file.getPath()));
		}
	}
	
	
}
