

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
		
		/*파일 이름을 받아서, 그 이름으로 파일 만들고, 한줄 일어서,dummyMDList에 담기*/
		
		//1. 파일 이름 받기
		String fileName = args[0];
		//2. 받은 이름으로 파일 열기
		File f = new File(fileName);
		//3. dummyMDList 선언
		ArrayList<String> dummyMDList = new ArrayList<String>();
		//해당 파일이 없거나, md파일이 아닌 경우 break;
		if(!f.isFile() || !fileName.endsWith(".md")){
			System.out.printf("no exist "+ fileName + "file or file is no .md file");			
		}else if(f.isFile()){
			System.out.printf("\n file open!\n");
			BufferedReader br = null;
			InputStreamReader isr = null;
			FileInputStream fis = null;   
			
			//임시로 파일 한 줄을 받을 변수 생성
			String temp = "";
			//while문 count를 위한 변수
			int count = 0;
			
			try{
				
				//파일 input 스트림 생성
				fis = new FileInputStream(f);
				
				//파일 input 스트림 객체를 이용하는데, UTF-8로 지정
				isr = new InputStreamReader(fis,"UTF-8");
				
				//input 스트림객체를 이용해 버퍼 생성
				br = new BufferedReader(isr);
				
				
				//한 줄 읽기
				while( (temp = br.readLine()) != null){
					//조건문에서 한줄 읽기
					System.out.println("This is file contents : " + temp + "\n");
					//읽은 내용을 dummyMDList에 담기
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
		//아래와 같은 String 3줄이 있다고 가정하고 dummyMDList에 삽입
		/*ArrayList<String> dummyMDList = new ArrayList<String>();
		String str1 = "## This is <H2> header #"; 	dummyMDList.add(str1);
		String str2 = "This is H1 header";			dummyMDList.add(str2);
		String str3 = "=======================";	dummyMDList.add(str3);
		String str4 = "This is normal text";		dummyMDList.add(str4);*/
		/*	결과적으로 
		 * "This is <H2> header" 라는 내용이 있는 level 2 헤더 노드 하나랑
		 * "This is H1 header"라는 내용이 있는 level 1 헤더 노드가 생성되면 됨.
		 */
		
		for(int i=0;i<dummyMDList.size();i++){
			//그냥 일반 노드 하나를 만들고,
			Node tempNode = new Node();
			
			//dummyMDList에 있는 스트링 한줄을 myTestParser에 있는 함수인 tokenize를 사용해서 토큰리스트를 만들고
			//그걸 tempNode에 넣는다.
			tempNode.setToken(myTestParser.tokenize(dummyMDList.get(i)));
			
			//addNodeToList 메소드를 이용해서 tempNode가 어떤 노드여야 하는지 확인하고
			//적절한 타입의 노드를 생성해서 nodeList에 추가한다.
			myTestParser.addNodeToList(tempNode);
		}
		
		//myTestParser 안에 있는 nodeList에 어떤 노드들이 들어가 있는지 확인.
		myTestParser.printAllNode();

	}
}
