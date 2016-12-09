import static org.junit.Assert.*;

import org.junit.Test;

public class MainTest {

	private Main man=new Main();
	@Test
	public void printHelp() {
		man.printHelp();
		System.out.println("");
	}

}
