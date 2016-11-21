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
			
			//�ӽ� ����
	        String temp = "";
	        //����� ���� ����
	        String content = "";
	        
	        try {
	        	//System.out.println("This is try \n ");
	        	fis = new FileInputStream(f);
	             
	            // File Input ��Ʈ�� ��ü�� �̿��� Input ��Ʈ�� ��ü�� �����ϴµ� ���ڵ��� UTF-8�� ����
	            isr = new InputStreamReader(fis, "UTF-8");
	             
	            // Input ��Ʈ�� ��ü�� �̿��Ͽ� ���۸� ����
	            br = new BufferedReader(isr);
	            
	          //���� �б�
				while( (temp = br.readLine()) != null )//������ ���� EOF�� �� ���� �ݺ��Ѵ�
				{
					content = temp;
					//���ǹ����� ���� �б�
					System.out.println("This is fule contents : " + temp + "\n");
					//�ɰ���
					System.out.println("This is parse \n ");
					//buf�� ���(list)
					System.out.println("input buf list parse \n ");
					//for(buf[0];buf[end]) �ϳ���  �Լ��� �˻��ϱ�
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
		
		//���ϴݱ�
	}
	
	public static void readMD(String fileName){
		
	}
}
