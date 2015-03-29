package src.main.java.ca.uwo.csd.hackwestern;

import java.io.IOException;

import com.jsyn.swing.JAppletFrame;
import com.leapmotion.leap.*;

public class Main {
	
	public static void main(String[] args){
		SawFaders applet = new SawFaders();
		JAppletFrame frame = new JAppletFrame( "SawFaders", applet );
		frame.setSize( 440, 200 );
		frame.setVisible( true );
		frame.test();
		
        Controller controller = new Controller();
        controller.enableGesture(Gesture.Type.TYPE_SWIPE);
        
        ProcessFrame processor = new ProcessFrame(applet, 'C'); 
        
        while (true)	// Polls the controller for a frame
        {
        	Frame currentFrame = controller.frame();
        	processor.process(currentFrame);
        	
        	try	// Constant framerate (60 frames per second)
        	{
        		Thread.sleep(20);
        	}
        	catch (java.lang.InterruptedException e)
        	{
        		e.printStackTrace();
        	}       	
        	
        	if (!controller.isConnected())	// Disconnect since the controller is disconnected
        	{
        		break;
        	}
        }        
        
        // Keep this process running until Enter is pressed
        System.out.println("Press Enter to quit...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
