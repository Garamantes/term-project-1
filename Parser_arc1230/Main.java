

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

import org.w3c.tidy.Tidy;
import org.w3c.tidy.TidyUtils;

public class Main {
	public static void main(String[] args) {
		//cmd에 args를 담기 위한 변수 선언
		String[] cmd = new String[100];
		
		//Main내용 
		MDParser myTestParser = new MDParser();
		//dummyMDList 선언
		ArrayList<String> dummyMDList = new ArrayList<String>();
		
		String filename="";
		int j=0;
		//while loop control variable
		int x=0;
		//check stage
		boolean check_stage=false;
		//check unnecessary command
		boolean is_necessary=true;
		
		int index_i_cmd=-1;
		int index_o_cmd=-1;
		int index_op_cmd=-1;
		//cmd[] 0으로 초기화
		for(int i=0;i<100;i++){
			cmd[i]="0";
		}
		
		//cmd[]에 args[]내용 담기
		for(String e : args){
			cmd[j]=e;
			j++;
		}
		
		/**********check "-help"*********/
		if(cmd[0].equals("-help"))
			printHelp();
		else if(cmd[0].equals("-input") || cmd[0].equals("-INPUT")){//it is not -help command
			while(x<100)/**********************trace cmd*******************/
			{
				//각 커맨드에 대해서 string array에 어디에 위치하는지 추적		
				if(cmd[x].equals("-input") || cmd[x].equals("-INPUT")){
					index_i_cmd = x;
					//break;
				}else if(cmd[x].equals("-output") || cmd[x].equals("-OUTPUT")){
					index_o_cmd = x;
					//break;
				}else if(cmd[x].equals("-option") || cmd[x].equals("-OPTION")){
					index_op_cmd = x;
					//break;
					System.out.println(cmd[index_op_cmd+1]);
					if(!cmd[index_op_cmd+2].equals("0")){
						System.out.println("	error : This command line includes unnecessary code!-");
						System.exit(0);
					}
					if(!cmd[index_op_cmd+1].equals("fancy")&&!cmd[index_op_cmd+1].equals("plain")&&!cmd[index_op_cmd+1].equals("slide")&& !cmd[index_op_cmd+1].equals("0")){
						System.out.println("	error : This command line includes unnecessary code!*");
						System.exit(0);
					}
				}
				
				if(cmd[x].endsWith(".html")){
					//plain 처리
					index_op_cmd = x+1;
				}
				x++;
			}
			
			if(index_op_cmd!=-1){
				if(cmd[index_op_cmd].equals("-option")||cmd[index_op_cmd].equals("-OPTION")||cmd[index_op_cmd].equals("0")||cmd[index_op_cmd].equals("")){
				}else{
					System.out.println("	error : This command line includes unnecessary code!!!");
					System.exit(0);
				}
			}
			
			x=0;
			
			/**************check input files******************/
			
			for(int f=(index_i_cmd+1);f<index_o_cmd;f++){
				//there is .md file?
				if(cmd[f].endsWith(".md")){
					File file = new File(cmd[f]);
					//there is cmd[f]'s md file?
					if(file.isFile()){
						BufferedReader br = null;
						InputStreamReader isr = null;
						FileInputStream fis = null;   
				        String temp = "";
				        
				        try {
				            fis = new FileInputStream(file);
				            isr = new InputStreamReader(fis, "UTF-8");
				            br = new BufferedReader(isr);
//============================파일에서 한 줄씩 읽어서 Token처리, node추가까지========================================
					            // 버퍼를 한줄한줄 읽어들여 내용 추출
					            while( (temp = br.readLine()) != null) {
					                Node tempNode = new Node();
					                tempNode.setToken(myTestParser.tokenize(temp));
					            	myTestParser.addNodeToList(tempNode);
					            }
					            check_stage=true;
//=============================================================================================================
				        }catch (FileNotFoundException e) {e.printStackTrace(); check_stage=false; }
				         catch (Exception e) {e.printStackTrace();check_stage=false;} 
				         finally { 
				        	 //close() 실행
				        	 try {fis.close();isr.close();br.close();} 
				        	 catch (IOException e) {e.printStackTrace();check_stage=false;}
				        }

						//myTestParser 안에 있는 nodeList에 어떤 노드들이 들어가 있는지 확인.
						//myTestParser.printAllNode();
				        
				        
					}else {
						System.out.println("	there is not exist "+cmd[f]+" file");
						check_stage=false;
					}
					
				}else if(!cmd[f].endsWith(".md")){
					System.out.println(	cmd[f]+" : this file name is not correnct\nplease input .md file");
					check_stage=false;
				}
			} 
			
			
			x=0;
			/*******************check output********************/
			if(check_stage==true){
				int num_md=0;
				int count_md=(index_o_cmd-index_i_cmd-1);
				int count_html=(index_op_cmd-index_o_cmd-1);
				
				for(int f=(index_o_cmd+1);f<index_op_cmd;f++){
					//there is .md file?
				//change to create only first html file	
				//int f=(index_o_cmd+1);
					
					if((num_md<count_md) && cmd[f].endsWith(".html")){
						//test code
						/*System.out.println("이거다 num_md : "+ num_md);
						System.out.println("이거다 count_md : "+ count_md);
						System.out.println("이거다 count_html : "+ count_html);*/
						if(count_html != count_md)
						{
							System.out.println("number of md files and html files is not equal ");
							System.out.println("please input same number of md files and html files");
							System.exit(1);
						}
						try{
							
							filename = cmd[f];
							File file2 = new File(cmd[f]);
							String override;
							//입력을 위한 변수
							//Scanner sc =new Scanner(System.in);
							
							//there is same name file already??
							while(true){
								if(file2.isFile())
								{
									System.out.println("	warning : already exist "+cmd[f]+" file of same name!");
									//System.out.println("	Do you want to override exist file to new file?(Y/N)\n");
									//override = sc.nextLine();
									
									System.out.println("	please enter the html file of another name.");
									
									System.exit(1);
								}else{
									BufferedWriter out = new BufferedWriter(new FileWriter(cmd[f]));
	//===============================HTML 생성=============================================================	
								
								PlainVisitor plainvisitor = new PlainVisitor();
								
								out.write(plainvisitor.startHtml());
								for(int i=0;i<myTestParser.nodeList.size();i++){
									out.write(myTestParser.nodeList.get(i).accept(plainvisitor));
								}
								out.write(plainvisitor.endHtml());
								
									
									
	//======================================================================================================
									 out.close();
									 System.out.println("	Success to make a "+cmd[f] +" file!!\n");
									 num_md++;
									 break;
								}
								
							}//while
						}catch(IOException ex){
							System.err.println(ex);
							System.exit(1);
						}
					}else if(!cmd[f].endsWith(".html"))//it is not html file
					{
						System.out.println("\n	please type correct html file name in legal format");
						System.out.println("	legal output file format is \"outputFileName.html\"");
					}
				}//for
			}else
			{
				//if(check_stage=false){
					System.out.println("	error : please input correct command");
				//}
				
			}
			
			x=0;
			/*************check options************/
			if(check_stage==true){
				
				if(index_op_cmd !=404){
					if(cmd[index_op_cmd+1].equals("plain")){
						//plain();
						System.out.println("	output html file style is plain\n");
					}
					else if(cmd[index_op_cmd+1].equals("fancy")){
						//fancy();
						System.out.println("	output html file style is fancy\n");
					}
					else if(cmd[index_op_cmd+1].equals("slide")){
						//slide();
						System.out.println("	output html file style is slide\n");
					}
					else
					{
						if(index_op_cmd<=-1)
						{
							System.out.println("\n	error : please input correct command");
							System.exit(0);
						}
							
						if(cmd[index_op_cmd].equals("-option")||cmd[index_op_cmd].equals("-OPTION"))
						{
							
						}else{
							if(!cmd[index_op_cmd+2].equals("0") || !cmd[index_op_cmd+1].equals("0") || !cmd[index_op_cmd].equals("0"))
							{	//is_necessary=false;
								System.out.println("\n	error : This command line includes unnecessary code");
								System.exit(0);
							}
						}
						if(!cmd[index_op_cmd+1].equals("plain") &&!cmd[index_op_cmd+1].equals("fancy")&&!cmd[index_op_cmd+1].equals("slide")&&!cmd[index_op_cmd+1].equals("0"))
						{
							System.out.println("this : " +cmd[index_op_cmd+1] );	
							System.out.println("\n	error : This command line includes unnecessary code*");
							System.exit(0);
						}else{
							System.out.println("	output html file default style is plain\n");
						}
						
					}
				}else{
					System.out.println("option error");
				}
			}
		}else{///if 1st command is not -help or -input
				System.out.println("	please input correct command!");
		}
	
	
	
		
	HtmlValidator jtidy = new HtmlValidator();
	jtidy.checkHtml(filename);
	
	
	
	
	
	
	
	
	
	
	
	}
	
	
	
	
	
	public static void printHelp(){
		System.out.println("----------------------------------------------------------------");
		System.out.println("	command line format : java CLI_main -input md_file_name.md -output html_file_name.html -option option_command");
		System.out.println("	option command : plain / fancy / slide\n");
		System.out.println("	you can omit -option command");
		System.out.println("	you can input several md files");
		System.out.println("	you can output several html files\n");
		System.out.println("	But, You must enter the same number of md files and html files \n");
		System.out.println("	you must input md files to same directory of CLI_mian.class file.");
		System.out.println("	html files are created to directory of CLI_mian.class file.\n");
		System.out.println("	this CLI is not support overriding html files");
		System.out.println("----------------------------------------------------------------");
	}
	
	
	
}
