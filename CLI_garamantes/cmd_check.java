//package CLI;

import java.io.BufferedReader;
import java.util.Scanner;
import java.io.*;

public class cmd_check {
	public boolean check(String cmd[]){
		String[] origin = cmd;
		File file1 = new File(origin[0]);
		if(!file1.isFile() && !origin[0].equals("-help")){
			if(origin[0].equals("0")){
				System.out.println("\n	no exist md file!");
				return false;
			}else
			{
				System.out.println("\n	no exist "+ origin[0] +" file!");
				return false;
			}
			
		}
		
		//test code
		/*for(int i=0;i<=4;i++)
		{
			System.out.println(origin[i]);
		}*/
		
		//0번째 string이 0이면 false return
		if(origin[0].equals("0")){
			return false;
		}
		//0번째 string이 0이아니면, 0번째에 접근하여 진행
		else{
			
			//help일때
			if(origin[0].equals("-help"))
			{
				
				System.out.println("->command line format : inputFileName.md outputFileName.html options_command");
				System.out.println("->options command is palin, fancy, slide");
				
				
				return true;
				
			}//help 아닐때 진행
			else if(origin[0].endsWith(".md") && origin[1].endsWith(".html"))
			{
				
				/*******************************read_file*************************************/
					BufferedReader br = null;
					
					InputStreamReader isr = null;
					
					FileInputStream fis = null;   
					
					File file = new File(origin[0]);
					
					//임시 변수
			        String temp = "";
			         
			        //출력을 위한 변수
			        String content = "";
			        
			        try {
			            // 파일을 읽어들여 File Input 스트림 객체 생성
			            fis = new FileInputStream(file);
			             
			            // File Input 스트림 객체를 이용해 Input 스트림 객체를 생서하는데 인코딩을 UTF-8로 지정
			            isr = new InputStreamReader(fis, "UTF-8");
			             
			            // Input 스트림 객체를 이용하여 버퍼를 생성
			            br = new BufferedReader(isr);
			         
			            // 버퍼를 한줄한줄 읽어들여 내용 추출
			            while( (temp = br.readLine()) != null) {
			                content += temp + "\n";
			            }
			             
			            System.out.println("\n======== This is file contents ========\n");
			            System.out.println(content);
			            System.out.println("=================================\n");
			             
			        } catch (FileNotFoundException e) {
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
			        
			        /*************************************create_file**************************************/
			        try{
						File file2 = new File(origin[1]);
						String override;
						//입력을 위한 변수
						Scanner sc =new Scanner(System.in);
						
						while(true){
							if(file2.isFile())
							{
								System.out.println("	message : already exist file of same name");
								System.out.println("	message : Do you want to override exist file to new file?(Y/N)\n");
								
								override = sc.nextLine();
								
							}else{
								BufferedWriter out = new BufferedWriter(new FileWriter(origin[1]));
								 
								 //추후에 html코드 내용을 여기다 담아서 입력해야한다.
								 String s = "this is html code\n";
								 String s1 = "this is other html code";
								 
								 //html 파일에 문자열 입력
								 out.write(s); out.newLine(); 
								 out.write(s1); out.newLine(); 
								 
								 out.close();
								 System.out.println("	message : Success to make"+origin[1] +" file");
								 options(origin);
								 break;
							}
							
							if(file1.isFile() && override.equals("Y"))
							{
								
								BufferedWriter out = new BufferedWriter(new FileWriter(origin[1]));
								 
								 //추후에 html코드 내용을 여기다 담아서 입력해야한다.
								 String s = "this is html code\n";
								 String s1 = "this is other html";
								 
								 //html 파일에 문자열 입력
								 out.write(s); out.newLine(); 
								 out.write(s1); out.newLine(); 
								 
								 out.close();
								 System.out.println("	message : Success to make a "+origin[1] +" file");
								 options(origin);
								 break;
								 
							}else if(file1.isFile() && override.equals("N"))
							{
								System.out.println("	message : please input another html file name");
								
								break;
							}
							else{
								System.out.println("	message : please input \"Y\" or \"N\"");
							}
							break;
							
						}	 
					}catch(IOException ex){
						System.err.println(ex);
						System.exit(1);
						
						return false;
					}
			      
			}else{
				System.out.println("\n	message : please input html file name in legal format");
				System.out.println("	message : legal output file format is \"outputFileName.html\"");
				return false;
			}
		}
		return true;
	}
	
	
	/*************************************options**************************************/
	public boolean options(String cmd[]){
		String[] origin = cmd;
		
		if(origin[2].equals("0") || origin[2].equals("plain")){
			System.out.println("\n	->"+origin[1]+" file made on palin style");
			return true;
		}
		else if(origin[2].equals("fancy")){
			System.out.println("\n	->"+origin[1]+" file made on fancy style");
			return true;
		}
		else if(origin[2].equals("slide")){
			System.out.println("\n	->"+origin[1]+" file made on slide style");
			return true;
		}else{
			return false;
		}
				
	}
}




			        
			
		
	


		

