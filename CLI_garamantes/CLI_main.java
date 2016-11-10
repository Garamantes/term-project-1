//package CLI;

public class CLI_main {

	public static void main(String[] args) {
		String[] cmd = new String[5];
		int i=0;
		
		for(int j=0;j<=4;j++){
			cmd[j]="0";
			//System.out.println(cmd[i]);
		}
		
		for(String e : args){
			cmd[i]=e;
			//System.out.println(cmd[i]);
			i++;
		}
		
		cmd_check c1 = new cmd_check();
		boolean cmd_bool = c1.check(cmd);
		
		if(cmd_bool==false){
			//System.out.println("	this is syntax error!");
		}
		/*else if(cmd_bool==true){
			boolean create = c1.create_File(cmd);
			if(create==true){
				System.out.println("	Success to make html file");
			}else{
				System.out.println("	faile to make html file");
			}
		}*/
		/*else 
		{
			System.out.println("Syantax error in main");
		}*/
		
	}

}
