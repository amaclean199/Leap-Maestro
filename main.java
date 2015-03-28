import java.io.IOException;

import com.leapmotion.leap.*;

public class main {
	public static void main(String[] args){
		Controller controller = new Controller();
		
		
		// Keep this process running until enter is pressed
		System.out.println("Press Enter to quit...");
		try
		{
			System.in.read();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
}
