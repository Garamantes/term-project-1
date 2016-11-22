//package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;
import java.util.regex.*;

public class parser {
	public static void main(String[] args){
		//recieve fileName
		String fileName = args[0]; 
		//open file
		System.out.println("\n	this is file name : "+ fileName +" file!\n");
		File f = new File(fileName);
		Vector<String> list= new Vector<String>();
		//int LISTSIZE=1024;
		//���Ͽ��� ���� content�� ã�Ƽ�, list�� �����ߴ��� ���ߴ��� check�ϴ� ���� ����
		int listCheck=0;
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
					listCheck=0;
					content = temp;
					//���ǹ����� ���� �б�
					System.out.println("This is fule contents : " + temp + "\n");
					//�ɰ���
					//System.out.println("This is parse \n ");
					//buf�� ���(list)
					//System.out.println("input buf list parse \n ");
					//for(buf[0];buf[end]) �ϳ���  �Լ��� �˻��ϱ�
					//System.out.println("check buf list \n ");
					
					System.out.println("\n1 listCheck : "+listCheck);
					listCheck += checkshap(content,list,listCheck);
					System.out.println("\n2 listCheck : "+listCheck);
					listCheck += checkequal(content,list,listCheck);
					System.out.println("\n3 listCheck : "+listCheck);
					if(listCheck==0)
						setplaintext(content,list,listCheck);
					System.out.println("\n\nthis is func()\n");
					
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
	        int i=0;
	        for(i=0;i<list.size();i++){
	        	 System.out.println("list["+i+"] :"+list.elementAt(i));
	        }
	        
	       
		}
		
		//���ϴݱ�
	}
	//listCheck�� false�� ���, ���� ���Ͽ��� ���� content�� plain���� ó���ؼ� list�� ��
	public static void setplaintext(String content,Vector<String> list, int listCheck){
		System.out.println("*************************\nthis is setplaintext()\n");
		//content�� ��� �ӽú��� temp����
		String temp = content;
		list.addElement(temp);
		//listCheck=true;		
		System.out.println("this is setPlaintext()\n");
		System.out.println(": " + temp);
	}
	//���ϸ�ó�� �̿��� =ã�� �Լ�
	public static int checkequal(String content,Vector<String> list, int listCheck){
		System.out.println("*************************\nthis is checkequal()\n");
		//listCheck=false;
		//=�� �Ѱ� �̻��̰�,�ڿ� �ƹ��͵� ���°�� ���� ����
		Pattern p = Pattern.compile("^={1,}$");
		//content�� ��� �ӽú��� temp����
		String temp = content;
		//�Էµ� content�� �Բ� ���� Ŭ���� ����
		Matcher m = p.matcher(temp);
		StringBuffer sb = new StringBuffer();
		//StringBuffer sb1 = new StringBuffer();
		int i=0;
		String cool[] = new String[10];
		
		//������ ������ �ִ��� ������ Ȯ��
		boolean result = m.find();
		if(result){
			System.out.println("=find\n");
			cool[1]=temp.substring(m.start(),m.end());
			list.addElement(cool[1]);
			//listCheck+=1;
			return 1;
			
		}
		else
		{
			System.out.println("= is not found");
			//list.addElement(temp);
			//listCheck+=0;
			return 0;
		}
			
			
	}
	
	//���ϸ�ó�� �̿��� ##ã�� �Լ�
	public static int checkshap(String content,Vector<String> list, int listCheck){
		System.out.println("*************************\nthis is checkshap()\n");
		//listCheck=false;
		//##�̶�� ���� ����
		Pattern p = Pattern.compile("(######)|(#####)|(####)|(###)|(##)|(#)");
		//content�� ��� �ӽú��� temp����
		String temp = content;
		//�Էµ� content�� �Բ� ���� Ŭ���� ����
		Matcher m = p.matcher(temp);
		StringBuffer sb = new StringBuffer();
		//StringBuffer sb1 = new StringBuffer();
		int i=0;
		int size_count=1;
		int LISTSIZE=1024;
		String cool[] = new String[10];
		//������ ������ �ִ��� ������ Ȯ��		
		boolean result = m.find();
		if(!result)
		{
			System.out.println("it is not found");
			//list.addElement(temp);
			return 0;
			
		}else{
			
			while(result){
				
				m.group();
				System.out.println("3");
				//
				if(m.start()==0){
					cool[i]=temp.substring(m.start(),m.end());
					System.out.println("cool["+i+"] :"+cool[i]);
					cool[i+1]=temp.substring(m.end());
					System.out.println("cool["+(i+1)+"] :"+cool[i+1]);
					temp=cool[i+1];
					i=i+1;
					size_count=i+1;
					
				}else
				{
					cool[i]=temp.substring(0,m.start());
					System.out.println("cool["+i+"] :"+cool[i]);
					cool[i+1]=temp.substring(m.start(),m.end());
					System.out.println("cool["+(i+1)+"] :"+cool[i+1]);
					cool[i+2]=temp.substring(m.end());
					System.out.println("cool["+(i+2)+"] :"+cool[i+2]);
					temp=cool[i+2];
					i=i+2;
					size_count=i+2;
					
				}
				
				
				m = p.matcher(temp);
				System.out.println("content:"+temp);
				
				if(m.find()){
					System.out.println("i'm found!!");
					
				}else
				{
					System.out.println("break!!\n");
					break;
				}
					
				
				
			}
			int j=0;
			for(j=0;j<size_count-1;j++){
				//list[j]=cool[j];
				list.addElement(cool[j]);
			}
			System.out.println("cool["+0+"] :"+cool[0]);
			System.out.println("cool["+1+"] :"+cool[1]);
			System.out.println("cool["+2+"] :"+cool[2]);
			System.out.println("cool["+3+"] :"+cool[3]);
			//System.out.println("cool["+4+"] :"+cool[4]);
			
			System.out.println("list["+0+"] :"+list.elementAt(0));
			System.out.println("list["+1+"] :"+list.elementAt(1));
			System.out.println("list["+2+"] :"+list.elementAt(2));
			System.out.println("list["+3+"] :"+list.elementAt(3));
			//System.out.println("list["+4+"] :"+list.elementAt(4));
			
			System.out.println("\n\ncool :"+cool[0]+cool[1]+cool[2]+cool[3]);
			return 1;
		}
			
	}
}
