package ifttt;

import java.io.File;

public class Path_changed extends Thread{
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
	
	private File[] FILES1;
	private long[] TIME1;
	private long[] SIZE1;
	private String[] NAME1;
	int count1 = 0;
	
	Path_changed(String task,Record_summary summary,Record_detail detail){
		this.path = task.substring(task.indexOf("[")+1, task.indexOf("]"));	//得到监控范围
		this.summary = summary;
		this.detail = detail;
		FILES = new File[10000];
		TIME = new long[10000];
		SIZE = new long[10000];
		NAME = new String[10000];
		
		FILES1 = new File[10000];
		TIME1 = new long[10000];
		SIZE1 = new long[10000];
		NAME1 = new String[10000];
		
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
			file_path_changed(file);
		}else if(file.isDirectory()){
			traverseFolder(file);
			dir_path_changed(file);
		}
		
	}
	
	
	public void traverseFolder(File file){		//遍历
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
	
	
	public void traverse(File file){
		File list[] = file.listFiles();
		for(int j = 0; j < list.length; j++){
			if(list[j].isDirectory()){
				traverse(list[j]);
			}else{
				FILES1[count1] = list[j];
				TIME1[count1] = list[j].lastModified();
				SIZE1[count1] = list[j].length();
				NAME1[count1] = list[j].getName();
				count1++;
			}
		}
	}
	
	
	public void file_path_changed(File file){
		long time = file.lastModified();
		String name = file.getName();
		long size = file.length();
		int i = 0;
		File parefile = new File(file.getParent());
		traverse(parefile);
		while(true){
			File nfile = new File(path);
			if(nfile.exists()){
				time = nfile.lastModified();
				name = nfile.getName();
				size = nfile.length();
			}else if(!nfile.exists()){			
				count1 = 0;
				traverse(parefile);
				for(i = 0; i < count1; i++){					
					if(TIME1[i]==time && SIZE1[i]==size && NAME1[i].equals(name)){
						if(!FILES1[i].getPath().equals(path)){
							record(nfile,FILES1[i]);
							this.path = FILES1[i].getPath();
							break;
						}
					}
				}
			}
			count1 = 0;
			traverse(parefile);
	
			try{
				Thread.sleep(500);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}	
	
	
	public void dir_path_changed(File file){
		while(true){
			for(int i = 0; i < count; i++){
				File nfile = new File(FILES[i].getPath());
				if(nfile.exists()){
					TIME[i] = nfile.lastModified();
					SIZE[i] = nfile.length();
					NAME[i] = nfile.getName();
				}else if(!nfile.exists()){
					count1 = 0;
					traverse(file);
					for(int j = 0; j < count1; j++){
						if(TIME1[j]==TIME[i] && SIZE1[j]==SIZE[i] && NAME1[j].equals(NAME[i])){
							if(!FILES1[j].getPath().equals(FILES[i].getPath())){
								record(nfile,FILES1[j]);
								break;
							}
						}
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
	
	
	public void record(File file, File filetemp){
		if(sflag == 1){
			summary.setpathchanged(summary.getpathchanged() + 1);
			summary.printtimes();
		}
		
		if(dflag == 1){
			detail.printpath_changed(file,filetemp);
		}
		
		if(rflag == 1){
			filetemp.renameTo(new File(file.getPath()));
		}
	}
		
	
}
