# term-project-1


##How to build and run the project with Windows command line

0. Locate the md file (ex test.md) in src folder
1. Go to root directory (Where build.xml is located)
2. \> ant jar

   This command will compile java files to bin folder
3. Move to bin folder and run Main class with input commands

   ex) ...\bin>java -cp .;jtidy-r938.jar mdconverter.Main -input test.md -output out.html
   (jtidy-r938.jar file will be copied to bin folder when building with Ant)
4. out.html file will be created in src folder.

-----

##Manual

1. -help command
> you can type -help command if you need a help for command line.

2. command line format 
> java CLI_main -input md_file_name.md -output html_file_name.html -option option_command 

3. -input command
> you must type only .md file.  
> you must input md files to src directory.   
> you can input several .md files     
>
> ex) java CLI_main -input ex1.md ex2.me  -output html_file_name1.html html_file_name2.html -option option_command 

4. -output command
> you must type only .html file.    
> html files are created to src directory.   
> you can output several html files.  
> <strong>But, You must enter the same number of md files and html files.</strong>
>
> ex) java CLI_main -input ex1.md ex2.md -output ex1.html ex2.html -option option_command 

5. overriding html file
> This CLI default is overriding html output files. 

6. option command
> option command has 3 types ( plain, fancy, slide ).    
> you can enter command line, except -option and option command.   
> if you not type -option or option command, default option is plain .  
