package mdconverter;
import java.io.*;
import java.util.ArrayList;


public class Main {
	static String option = new String(); //option 변수 (Plain, Fancy, Slide 등)
	public static void main(String[] args) {
		ArrayList<String> inputFile = new ArrayList<String>();		//.md 파일이름 리스트
		ArrayList<String> outputFile = new ArrayList<String>();		//.html 파일이름 리스트
		ConcreteVisitor visitor = null;

		//Command Line 입력 오류 확인
		checkCLI(args, inputFile, outputFile);

		//Parser 객체 생성
		MDParser mdParser = new MDParser();

		//파일 읽기/쓰기 준비
		//이클립스용
		//File upOne = new File(System.getProperty("user.dir")).getAbsoluteFile();
		//CMD 용
		File upOne = new File(System.getProperty("user.dir")).getParentFile();

		String filepath = upOne.getAbsolutePath();

		File fin = new File(filepath+"/src/"+inputFile.get(0));
		File fout = new File(filepath+"/src/"+outputFile.get(0));
		FileReader fr = null;
		FileWriter fw = null;
		BufferedReader in = null;
		BufferedWriter out = null;

		try {
			fr = new FileReader(fin);
			in = new BufferedReader(fr);
			fw = new FileWriter(fout);
			out = new BufferedWriter(fw);

			String temp=new String();

			//.md파일에서 temp로 한줄씩 읽어서 그 문자열을 처리
			while((temp = in.readLine()) != null){
				//System.out.println(temp);
				Node tempNode = new Node();
				tempNode.setToken(mdParser.tokenize(temp));
				mdParser.addNodeToList(tempNode, mdParser.nodeList);
			}

			///*
			//어떤 노드들이 있는지 확인용. 최종본엔 있을 필요 없음.
			for(int i=0;i<mdParser.nodeList.size();i++){
			//	mdParser.nodeList.get(i).printNodeInfo();
			}
			//*/


			//Visitor 패턴으로 plain 스타일 html 적용
			if(option.equals("plain"))
				visitor = new PlainVisitor();
			else if(option.equals("fancy"))
				visitor = new FancyVisitor();

			out.write(visitor.startHtml());
			for(int i=0;i<mdParser.nodeList.size();i++){
				//System.out.println(mdParser.nodeList.get(i).accept(plainvisitor));
				if(i>0 && mdParser.nodeList.get(i) instanceof N_TextNode && mdParser.nodeList.get(i-1) instanceof N_TextNode)
					out.write("<br>");
				out.write(mdParser.nodeList.get(i).accept(visitor));
			}
			out.write(visitor.endHtml());

			//파일닫기
			in.close();
			out.close();
			fr.close();
			fw.close();

		} catch (IOException e) {e.printStackTrace();}


		//JTidy 로 html 검사
		HtmlValidator jtidy = new HtmlValidator();
		jtidy.checkHtml(outputFile.get(0));



	}


	//========= -help 입력 시 출력 =================
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

	//CLI 입력 확인
	public static void checkCLI(String[] args, ArrayList<String> inputFile, ArrayList<String> outputFile){
		int index_input = -1;
		int index_output = -1;
		int index_option = -1;
		int input_count = 0;
		int output_count = 0;



		if(args.length == 0){
			System.out.println("No argument");
			System.exit(0);
		}
		//1. args0 이 help => help page 띄움
		else if(args[0].equals("-help") || args[0].equals("-HELP"))
			printHelp();
		//2. help가 아니라면, input, output, option의 위치 기억
		else{
			int i=0;
			while(i<args.length){
				if(args[i].equals("-input") || args[i].equals("-INPUT"))
					index_input = i;
				else if(args[i].equals("-output") || args[i].equals("-OUTPUT"))
					index_output = i;
				else if(args[i].equals("-option") || args[i].equals("-OPTION"))
					index_option = i;
				i++;
			}
			//input, output을 안썼거나 input, output, option 순서를 틀린 경우
			if(index_input == -1 || index_output == -1 ||
				index_input>index_output || index_output>index_option ||
				index_output - index_input == 1){
				System.out.println("Wrong command. Check -help for commnad line syntax");
				System.exit(0);
			}
			//input과 output 파일의 개수 확인
			input_count = index_output - index_input;
			if(index_option != -1)
				output_count = index_option - index_output;
			else
				output_count = args.length - index_output;

			if(input_count != output_count || input_count <= 0 || output_count <= 0){
				System.out.println("Number of input files and output files doesn't match");
				System.exit(0);
			}

			//option 변수에 option값 전달
			if(index_option	== -1)
				option = "plain";
			else if(args[index_option+1].equals("plain") ||args[index_option+1].equals("fancy") ||args[index_option+1].equals("slide")){
				option = args[index_option+1];
			}else{
				System.out.println("Wrong option");
				System.exit(0);
			}
				System.out.println(option);

			//inputFile리스트에 input 파일이름 저장, outputFile리스트에 output 파일이름 저장
			for(int k=1;k<index_output-index_input;k++){
				inputFile.add(args[index_input+k]);
				outputFile.add(args[index_output+k]);
			}
		}


		//확장자 확인
		if(checkExtension(inputFile, "in")==false || checkExtension(outputFile, "out") == false)
			System.exit(0);

		//파일 읽기/쓰기 준비
		//이클립스용
		//File upOne = new File(System.getProperty("user.dir")).getAbsoluteFile();
		//CMD 용
		File upOne = new File(System.getProperty("user.dir")).getParentFile();

		String filepath = upOne.getAbsolutePath();

		//Input파일 존재여부 확인
		for(int i=0;i<inputFile.size();i++){
			File file = new File(filepath+"/src/"+inputFile.get(0));
			if(file.exists()==false){
				System.out.println("No input file. Check file name");
				System.exit(0);
			}
		}

		//Output파일 확인
		for(int i=0;i<outputFile.size();i++){
			File file = new File(filepath+"/src/"+outputFile.get(0));
			if(file.isFile() == true){
				System.out.println("There already exists file: "+outputFile.get(i));
				System.out.println("This will be overwrited");
				//System.out.print("Overwrite? (y/n)");
				InputStreamReader ir = new InputStreamReader(System.in);
				BufferedReader br = new BufferedReader(ir);
				String newFile;
				char yn;
				try {
					yn = 'y'; System.out.println();	//Overwrite 물어볼꺼면 이거 지우고 아래꺼 살리기
					//yn = br.readLine().charAt(0);
					if(yn == 'n' || yn == 'N'){
						while(true){
							System.out.print("Enter new output filename : ");
							newFile = br.readLine();
							if(newFile.endsWith(".html")==false)
								System.out.println("Wrong filename");
							else{
								outputFile.add(i,newFile);
								outputFile.remove(i+1);
								i--;
								break;
							}
						}
					}else{
						//overwrite
					}
				} catch (IOException e) {e.printStackTrace();}
			}
		}

	}


	//.md, .html 확장자 확인하는 메소드
	public static boolean checkExtension(ArrayList<String> list, String type){
		for(int i=0;i<list.size();i++){
			if(type.equals("in") && list.get(i).endsWith(".md")==false){
				System.out.println("Wrong extension. \nUse .md for Input");
				return false;
			}else if(type.equals("out") && list.get(i).endsWith(".html")==false){
				System.out.println("Wrong extension. \nUse .html for Output");
				return false;
			}
		}
		return true;
	}

}
