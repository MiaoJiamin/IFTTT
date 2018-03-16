package ifttt;

import java.io.File;

public class Modified extends Thread{
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
	
	Modified(String task,Record_summary summary,Record_detail detail){
		this.path = task.substring(task.indexOf("[")+1, task.indexOf("]"));	//得到监控范围
		this.summary = summary;
		this.detail = detail;
		FILES = new File[10000];
		TIME = new long[10000];
		SIZE = new long[10000];
		NAME = new String[10000];
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
			file_modified(file);
		}else if(file.isDirectory()){
			traverseFolder(file);
			dir_modified(file);
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
	
	
	public void file_modified(File file){
		long time = file.lastModified();
//		String name = file.getName();
//		long size = file.length();
		
		while(true){
			File filetemp = new File(path);
			if(filetemp.exists()){
				if(filetemp.lastModified()!=time){
					record(file,filetemp,time);
					time = filetemp.lastModified();
//					name = filetemp.getName();
//					size = filetemp.length();
				}
			}
			
			try{
				Thread.sleep(500);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
		
	public void dir_modified(File file){
		while(true){
			for(int i = 0; i < count; i++){
				File filetemp = new File(FILES[i].getPath());
				if(FILES[i].exists()){
					if(filetemp.lastModified()!=TIME[i] && filetemp.getName().equals(NAME[i])){
						record(FILES[i],filetemp,TIME[i]);
					}
				}
			}
			count = 0;
			traverseFolder(file);
			
			try{
				Thread.sleep(500);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
	
	
	public void record(File file, File filetemp, long time1){
		if(sflag == 1){
			summary.setmodified(summary.getmodified() + 1);
			summary.printtimes();
		}
		
		if(dflag == 1){
			detail.printModified(file,filetemp,time1);
		}
		
		if(rflag == 1){
			filetemp.renameTo(new File(file.getPath()));
		}
	}	
	
}
