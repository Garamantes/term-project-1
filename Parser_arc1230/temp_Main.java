

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class temp_Main {
	public static void main(String[] args){
		MDParser myTestParser = new MDParser();
		
		/*���� �̸��� �޾Ƽ�, �� �̸����� ���� �����, ���� �Ͼ,dummyMDList�� ���*/
		
		//1. ���� �̸� �ޱ�
		String fileName = args[0];
		//2. ���� �̸����� ���� ����
		File f = new File(fileName);
		//3. dummyMDList ����
		ArrayList<String> dummyMDList = new ArrayList<String>();
		//�ش� ������ ���ų�, md������ �ƴ� ��� break;
		if(!f.isFile() || !fileName.endsWith(".md")){
			System.out.printf("no exist "+ fileName + "file or file is no .md file");			
		}else if(f.isFile()){
			System.out.printf("\n file open!\n");
			BufferedReader br = null;
			InputStreamReader isr = null;
			FileInputStream fis = null;   
			
			//�ӽ÷� ���� �� ���� ���� ���� ����
			String temp = "";
			//while�� count�� ���� ����
			int count = 0;
			
			try{
				
				//���� input ��Ʈ�� ����
				fis = new FileInputStream(f);
				
				//���� input ��Ʈ�� ��ü�� �̿��ϴµ�, UTF-8�� ����
				isr = new InputStreamReader(fis,"UTF-8");
				
				//input ��Ʈ����ü�� �̿��� ���� ����
				br = new BufferedReader(isr);
				
				
				//�� �� �б�
				while( (temp = br.readLine()) != null){
					//���ǹ����� ���� �б�
					System.out.println("This is file contents : " + temp + "\n");
					//���� ������ dummyMDList�� ���
					dummyMDList.add(temp);				
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
		//
		//�Ʒ��� ���� String 3���� �ִٰ� �����ϰ� dummyMDList�� ����
		/*ArrayList<String> dummyMDList = new ArrayList<String>();
		String str1 = "## This is <H2> header #"; 	dummyMDList.add(str1);
		String str2 = "This is H1 header";			dummyMDList.add(str2);
		String str3 = "=======================";	dummyMDList.add(str3);
		String str4 = "This is normal text";		dummyMDList.add(str4);*/
		/*	��������� 
		 * "This is <H2> header" ��� ������ �ִ� level 2 ��� ��� �ϳ���
		 * "This is H1 header"��� ������ �ִ� level 1 ��� ��尡 �����Ǹ� ��.
		 */
		
		for(int i=0;i<dummyMDList.size();i++){
			//�׳� �Ϲ� ��� �ϳ��� �����,
			Node tempNode = new Node();
			
			//dummyMDList�� �ִ� ��Ʈ�� ������ myTestParser�� �ִ� �Լ��� tokenize�� ����ؼ� ��ū����Ʈ�� �����
			//�װ� tempNode�� �ִ´�.
			tempNode.setToken(myTestParser.tokenize(dummyMDList.get(i)));
			
			//addNodeToList �޼ҵ带 �̿��ؼ� tempNode�� � ��忩�� �ϴ��� Ȯ���ϰ�
			//������ Ÿ���� ��带 �����ؼ� nodeList�� �߰��Ѵ�.
			myTestParser.addNodeToList(tempNode);
		}
		
		//myTestParser �ȿ� �ִ� nodeList�� � ������ �� �ִ��� Ȯ��.
		myTestParser.printAllNode();

	}
}
