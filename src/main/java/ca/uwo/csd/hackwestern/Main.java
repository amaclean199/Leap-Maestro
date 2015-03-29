package src.main.java.ca.uwo.csd.hackwestern;

import java.io.IOException;

import com.jsyn.swing.JAppletFrame;
import com.leapmotion.leap.*;

public class Main {
	
	public static void main(String[] args){
		SawFaders applet = new SawFaders();
		char currentScale = 'G';
		JAppletFrame frame = new JAppletFrame("Leap Maestro", applet);
		frame.setSize( 640, 480 );
		frame.setVisible( true );
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.test();
		
        Controller controller = new Controller();
        controller.enableGesture(Gesture.Type.TYPE_SWIPE);
        boolean hasPrinted = false;
        
        ProcessFrame processor = new ProcessFrame(applet, currentScale);
        
        while (true)	// Polls the controller for a frame
        {
        	processor = new ProcessFrame(applet, currentScale);
        	Frame currentFrame = controller.frame();
        	
        	currentScale = processor.process(currentFrame);
        	System.out.println("Scale: "+ currentScale);
        	/*
        	if (temp == currentScale)
        	{
        		hasPrinted = false;
        	}
        	else if (!hasPrinted)
        	{
        		currentScale = temp;
        		System.out.println("Scale: " + currentScale);
        		hasPrinted = true;
        	}*/
        	
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
