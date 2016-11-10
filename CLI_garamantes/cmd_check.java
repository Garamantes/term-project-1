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
		
		//0��° string�� 0�̸� false return
		if(origin[0].equals("0")){
			return false;
		}
		//0��° string�� 0�̾ƴϸ�, 0��°�� �����Ͽ� ����
		else{
			
			//help�϶�
			if(origin[0].equals("-help"))
			{
				
				System.out.println("->command line format : inputFileName.md outputFileName.html options_command");
				System.out.println("->options command is palin, fancy, slide");
				
				
				return true;
				
			}//help �ƴҶ� ����
			else if(origin[0].endsWith(".md") && origin[1].endsWith(".html"))
			{
				
				/*******************************read_file*************************************/
					BufferedReader br = null;
					
					InputStreamReader isr = null;
					
					FileInputStream fis = null;   
					
					File file = new File(origin[0]);
					
					//�ӽ� ����
			        String temp = "";
			         
			        //����� ���� ����
			        String content = "";
			        
			        try {
			            // ������ �о�鿩 File Input ��Ʈ�� ��ü ����
			            fis = new FileInputStream(file);
			             
			            // File Input ��Ʈ�� ��ü�� �̿��� Input ��Ʈ�� ��ü�� �����ϴµ� ���ڵ��� UTF-8�� ����
			            isr = new InputStreamReader(fis, "UTF-8");
			             
			            // Input ��Ʈ�� ��ü�� �̿��Ͽ� ���۸� ����
			            br = new BufferedReader(isr);
			         
			            // ���۸� �������� �о�鿩 ���� ����
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
						//�Է��� ���� ����
						Scanner sc =new Scanner(System.in);
						
						while(true){
							if(file2.isFile())
							{
								System.out.println("	message : already exist file of same name");
								System.out.println("	message : Do you want to override exist file to new file?(Y/N)\n");
								
								override = sc.nextLine();
								
							}else{
								BufferedWriter out = new BufferedWriter(new FileWriter(origin[1]));
								 
								 //���Ŀ� html�ڵ� ������ ����� ��Ƽ� �Է��ؾ��Ѵ�.
								 String s = "this is html code\n";
								 String s1 = "this is other html code";
								 
								 //html ���Ͽ� ���ڿ� �Է�
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
								 
								 //���Ŀ� html�ڵ� ������ ����� ��Ƽ� �Է��ؾ��Ѵ�.
								 String s = "this is html code\n";
								 String s1 = "this is other html";
								 
								 //html ���Ͽ� ���ڿ� �Է�
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




			        
			
		
	


		

