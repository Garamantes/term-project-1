# term-project-1
1. -help command
> you can type -help command if you need a help for command line.

2. command line format 
> java CLI_main -input md_file_name.md -output html_file_name.html -option option_command 

3. -input command
> you must type only .md file.  
> you must input md files to same directory of CLI_mian.class file.   
> you can input several .md files     
>
> ex) java CLI_main -input ex1.md ex2.me ex3.md -output html_file_name.html -option option_command 

4. -output command
> you must type only .html file.    
> html files are created to directory of CLI_mian.class file.   
> you can output several html files.  
> But, But, only create one html file that has first html file's name.  
>
> ex) java CLI_main -input ex1.md ex2.md -output ex1.html ex2.html ex3.html -option option_command 

5. overriding html file
> This CLI is not support overriding html files. 
>
> if you create file of exist name, you can see message : 
> please enter the html file of another name.

6. option command
> option command has 3 types ( plain, fancy, slide ).    
> you can enter command line, except -option and option command.   
> if you not type -option or option command, default option is plain .  
