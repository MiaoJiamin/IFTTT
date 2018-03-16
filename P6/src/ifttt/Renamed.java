package ifttt;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class Renamed extends Thread{
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
	
	Renamed(String task,Record_summary summary,Record_detail detail){
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
			file_renamed(file);
		}else if(file.isDirectory()){
			traverseFolder(file);
			dir_renamed(file);
		}
	}
	
	public void file_renamed(File file){
		long time = file.lastModified();
		String name = file.getName();
		long size = file.length();
		int i = 0;
		File parefile = new File(file.getParent());
		File[] file1 = parefile.listFiles();
		ArrayList<File> files = new ArrayList<File>(Arrays.asList(file1));
		
		while(true){
			File nfile = new File(path);
			if(nfile.exists()){
				time = nfile.lastModified();
				name = nfile.getName();
				size = nfile.length();
				parefile = new File(nfile.getParent());
				file1 = parefile.listFiles();
				files = new ArrayList<File>(Arrays.asList(file1));
			}else{
				for(i=0;i<parefile.listFiles().length;i++){
					File filetemp = parefile.listFiles()[i];
					if((filetemp.lastModified() == time) && (filetemp.length() == size)){
						if(!files.contains(filetemp)){
							if(!(filetemp.getName().equals(name))){
								record(nfile,filetemp);
								
								nfile = filetemp;
								this.path = filetemp.getPath();
								time = nfile.lastModified();
								name = nfile.getName();
								size = nfile.length();
								file1 = parefile.listFiles();
								files = new ArrayList<File>(Arrays.asList(file1));
								break;
							}
						}
					}
				}
			}
			
			try{
				Thread.sleep(500);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
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
	
	public void dir_renamed(File file){
		while(true){
			for(int i = 0; i < count; i++){
				if(FILES[i].exists()){
					FILES[i] = new File(FILES[i].getPath());
					TIME[i] = FILES[i].lastModified();
					SIZE[i] = FILES[i].length();
					NAME[i] = FILES[i].getName();
				}else if(!FILES[i].exists()){
					File filetemp = null;
					File list[] = FILES[i].getParentFile().listFiles();
					ArrayList<File> files = new ArrayList<File>(Arrays.asList(FILES));
					for(int j = 0; j < list.length; j++){
						if(list[j].isDirectory())
							continue;
						else{
							if((list[j].lastModified() == TIME[i]) && (list[j].length() == SIZE[i])){
								if(!list[j].getName().equals(NAME[i])){
									if(!files.contains(list[j])){
										filetemp = list[j];
										break;
									}
								}
							}
						}
					}
					
					if(filetemp!=null){
						record(FILES[i],filetemp);
					}
					break;
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
	
	
	public void record(File file, File filetemp) {
		if(sflag == 1){
			summary.setrenamed(summary.getrenamed() + 1);
			summary.printtimes();
		}
		
		if(dflag == 1){
			detail.printrenamed(file,filetemp);
		}
		
		if(rflag == 1){
			filetemp.renameTo(new File(file.getPath()));
		}
	}
	
}
