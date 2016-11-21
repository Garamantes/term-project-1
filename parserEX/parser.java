//package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class parser {
	public static void main(String[] args){
		//recieve fileName
		String fileName = args[0]; 
		//open file
		System.out.println("\n	this is file name : "+ fileName +" file!\n");
		File f = new File(fileName);
		//check exist file and md file
		if(!f.isFile() && !fileName.endsWith(".md"))
		{
			if(fileName.equals(null)){
				System.out.printf("\n no exist md file!");
			}
			else{
				System.out.println("\n	no exist "+ fileName +" file!");
			}
			
		}else if(f.isFile()){
			System.out.printf("\n file open!");
			BufferedReader br = null;
			InputStreamReader isr = null;
			FileInputStream fis = null;   
			
			//임시 변수
	        String temp = "";
	        //출력을 위한 변수
	        String content = "";
	        
	        try {
	        	//System.out.println("This is try \n ");
	        	fis = new FileInputStream(f);
	             
	            // File Input 스트림 객체를 이용해 Input 스트림 객체를 생서하는데 인코딩을 UTF-8로 지정
	            isr = new InputStreamReader(fis, "UTF-8");
	             
	            // Input 스트림 객체를 이용하여 버퍼를 생성
	            br = new BufferedReader(isr);
	            
	          //한줄 읽기
				while( (temp = br.readLine()) != null )//파일의 끝이 EOF일 때 까지 반복한다
				{
					content = temp;
					//조건문에서 한줄 읽기
					System.out.println("This is fule contents : " + temp + "\n");
					//쪼개기
					System.out.println("This is parse \n ");
					//buf에 담기(list)
					System.out.println("input buf list parse \n ");
					//for(buf[0];buf[end]) 하나씩  함수로 검사하기
					System.out.println("check buf list \n ");
				}
	        }catch (FileNotFoundException e) {
	            e.printStackTrace();
	            
	             
	        } catch (Exception e) {
	            e.printStackTrace();
	            
	             
	        } finally {
	            try {
	                fis.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	                
	            }
	            try {
	                isr.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	                
	            }
	            try {
	                br.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	                
	            }
	        }
			
		}
		
		//파일닫기
	}
	
	public static void readMD(String fileName){
		
	}
}
