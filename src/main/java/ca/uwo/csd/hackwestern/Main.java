//package src.main.java.ca.uwo.csd.hackwestern;

import java.io.IOException;

import com.leapmotion.leap.*;
import com.leapmotion.leap.Vector;

import java.util.*;

class SampleListener extends Listener {
	public void onInit(Controller controller) {
        System.out.println("Initialized");
    }

    public void onConnect(Controller controller) {
        System.out.println("Connected");
        controller.enableGesture(Gesture.Type.TYPE_SWIPE);
        controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
        controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
        controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
    }

    public void onDisconnect(Controller controller) {
        //Note: not dispatched when running in a debugger.
        System.out.println("Disconnected");
    }

    public void onExit(Controller controller) {
        System.out.println("Exited");
    }

    public void onFrame(Controller controller) {
    	Frame frame = controller.frame();
    	GestureList gestures = frame.gestures();
        InteractionBox i_box = frame.interactionBox();

    	/*System.out.println("Frame id: " + frame.id()
                         + ", timestamp: " + frame.timestamp()
                         + ", hands: " + frame.hands().count()
                         + ", fingers: " + frame.fingers().count()
                         + ", tools: " + frame.tools().count()
                         + ", gestures " + frame.gestures().count());*/

    	//Get hands
        for(Hand hand : frame.hands()) {
        	String handType = hand.isLeft() ? "Left hand" : "Right hand";
        	System.out.println("  " + handType + ", id: " + hand.id()
                             + ", palm position: " + hand.palmPosition() + "frame width: " + frame.interactionBox().width());
            Vector normalizedHandPosition = i_box.normalizePoint(hand.palmPosition());

            float normalizedX = normalizedHandPosition.getX();

            int finalX = checkNote(normalizedX, 8.0);
            
            System.out.println("Note: " + normalizedX + "finalX: " + finalX);
        }

    	// Get tools
        for(Tool tool : frame.tools()) {
            System.out.println("  Tool id: " + tool.id()
                             + ", position: " + tool.tipPosition()
                             + ", direction: " + tool.direction());
        }

    	if (!frame.hands().isEmpty() || !gestures.isEmpty()) {
            System.out.println();
        }
    }
    
    
    /**
     * checkNote checks where the "x" value is in terms of the interaction Box
     * @x is the NORMALIZED x position of the finger [0,1]
     * @frameWidth is the default width of the frame's interaction box
     * @numNotes is the number of bars to be generated
     */
    private int checkNote(Float x, Double numNotes)
    {
    	double div = 1.0/numNotes;	// Represents the space allocated to each "note"
    	
    	System.out.println("div: "+div);
    	
    	/*if (0 < x && x < div)
    	{
    		return 0;
    	}
    	else if ( div < x && x < 2*div)
    	{
    		return 1;
    	}
    	else if (2*div < x && x < 3*div)
    	{
    		return 2;
    	}
    	else if (3*div < x && x < 4*div)
    	{
    		return 3;
    	}
    	else if (4*div < x && x < 5*div)
    	{
    		return 4;
    	}
    	else if (5*div < x && x < 6*div)
    	{
    		return 5;
    	}
    	else if (6*div < x && x < 7*div)
    	{
    		return 6;
    	}
    	else
    	{
    		return 7;
    	}*/
    	
    	for (double i = 0.0; i < numNotes; i ++)
    	{
    		if ( i < x && x < div)
    		{
    			return (int)i;
    		}
    	}
    	return 0;	// Return 0 default to silence warning
    	
    }
}

public class main {
	public static void main(String[] args){
		// Create a sample listener and controller
        SampleListener listener = new SampleListener();
        Controller controller = new Controller();

        // Have the sample listener receive events from the controller
        controller.addListener(listener);
        
        // Keep this process running until Enter is pressed
        System.out.println("Press Enter to quit...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Remove the sample listener when done
        controller.removeListener(listener);
	}
}
