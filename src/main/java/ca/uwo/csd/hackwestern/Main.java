package src.main.java.ca.uwo.csd.hackwestern;

import java.io.IOException;

import com.leapmotion.leap.*;
import com.leapmotion.leap.Vector;

import java.util.*;

public class Main {
	
	public static void main(String[] args){
        Controller controller = new Controller();
        SawFaders sFade = new SawFaders();
        ProcessFrame processor = new ProcessFrame(sFade); 
        
        while (true)	// Polls the controller for a frame
        {
        	Frame currentFrame = controller.frame();
        	
        	processor.process(currentFrame);        	
        	
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
