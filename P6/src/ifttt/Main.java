package ifttt;

import java.util.Scanner;

public class Main {
	public static void main(String[] args){
		Record_summary summary = new Record_summary();
		Record_detail detail = new Record_detail();
		String[] array = new String[10000];
		int i = 0;
		String line = null;
		Scanner scanner = new Scanner(System.in);
		while(!(line=scanner.nextLine()).equals("run")){
			array[i] = line;
			i++;
		}
		scanner.close();
		
		for(i = 0; array[i]!=null; i++){	//一个工作区开一个线程
			if(array[i].matches("IF +\\[[^ ]+\\] +(Modified|renamed|path_changed|size_changed)? +THEN +(record_summary|record_detail|recover)?")){
				String[] str = array[i].split(" +");
				if(str[2].equals("Modified")){
					Modified m = new Modified(array[i],summary,detail);
					new Thread(m).start();
				}else if(str[2].equals("renamed")){
					Renamed r = new Renamed(array[i],summary,detail);
					new Thread(r).start();
				}else if(str[2].equals("path_changed")){
//					System.out.println("&&&&&&&");
					Path_changed p = new Path_changed(array[i],summary,detail);
					new Thread(p).start();
					//System.out.println("1231231");
				}else if(str[2].equals("size_changed")){
					Size_changed s = new Size_changed(array[i],summary,detail);
					new Thread(s).start();
				}
			}else{
				System.out.println("INVALID INPUT");
			}
		}
		
	}
}
