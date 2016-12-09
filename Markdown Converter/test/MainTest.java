import static org.junit.Assert.*;
import java.io.*;
import java.util.ArrayList;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

//import java.io.BufferedReader;

public class MainTest {

	private Main one;
	BufferedReader in = null;
	//ArrayList<String> inputFile = new ArrayList<String>();		//.md 파일이름 리스트
	//ArrayList<String> outputFile = new ArrayList<String>();		//.html 파일이름 리스트
	//String option = new String();
	//String a = "-help";
	@Before
	public void setup() {
		one = new Main();
		
		System.out.println("setup");
	}

	@After
	public void teardown() {
		System.out.println("teardown");
	}

	@Test
	public void test() {
		try {
			  one.main(new String[] {"-help"});
			  fail("Expected exception not thrown");
		} catch (Exception e) {}
		
			System.out.println("test");
	}
	
	/*
	public static void main(String[] args)
	{
		Result result = JUnitCore.runClasses(Main.class);
		
	      for (Failure failure : result.getFailures()) {
	         System.out.println(failure.toString());
	      }
			
	      System.out.println(result.wasSuccessful());
	}
	*/
}
