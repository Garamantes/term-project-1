# term-project-1
1. -help command
> you can type 

2. command line format 
> java CLI_main -input md_file_name.md -output html_file_name.html -option option_command 

3. -input command
> you must type only .md file.  
> you must input md files to same directory of CLI_mian.class file.   
> you can input several .md files     

> ex) java CLI_main -input ex1.md ex2.me ex3.md -output html_file_name.html -option option_command 

4. -output command
> you must type only .html file.    
> html files are created to directory of CLI_mian.class file.   
> you can output several html files.  

> ex) java CLI_main -input ex1.md ex2.md -output ex1.html ex2.html ex3.html -option option_command 

5. overriding html file
> you can choose overriding or not on html files. 
>
> if you create file of exist name, you can see message : 
> Do you want to override exist file to new file?(Y/N)
>
> if you type "Y", html file is overrided.  
> if you type "N", html file is not overrided.  
>
> when you type another code on this message, ypu see a same message until type "Y" or "N". 

6. option command
> option command has 3 type ( plain, fancy, slide ).    
> you can type command line, except -option and option command.   
> if you not type -option or option command, default option is plain .  
