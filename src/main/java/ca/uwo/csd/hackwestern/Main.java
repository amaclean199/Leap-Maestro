//package src.main.java.ca.uwo.csd.hackwestern;

import java.io.IOException;

import com.jsyn.swing.JAppletFrame;
import com.leapmotion.leap.*;
import com.leapmotion.leap.Vector;

//import src.main.java.ca.uwo.csd.hackwestern.SampleListener;
//import src.main.java.ca.uwo.csd.hackwestern.ProcessFrame;
import java.util.*;

public class Main {
	
	public static void main(String[] args){

		SawFaders applet = new SawFaders();
		JAppletFrame frame = new JAppletFrame( "SawFaders", applet );
		frame.setSize( 440, 200 );
		frame.setVisible( true );
		frame.test();
		
        Controller controller = new Controller();
        ProcessFrame processor = new ProcessFrame(applet); 
        
		
        
        while (true)	// Polls the controller for a frame
        {
        	Frame currentFrame = controller.frame();
        	Frame previousFrame = controller.frame(1);
        	processor.process(currentFrame,previousFrame);
//        	System.out.println("timer");
        	
        	
        	try	// Constant framerate (10 frames per second)
        	{
        		Thread.sleep(300);
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
