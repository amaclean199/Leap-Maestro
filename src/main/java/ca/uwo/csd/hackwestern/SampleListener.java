package src.main.java.ca.uwo.csd.hackwestern;

import java.io.File;

import jm.JMC;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.InteractionBox;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Tool;
import com.leapmotion.leap.Vector;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.MidiChannel;



public class SampleListener extends Listener implements JMC {
	
	private int[] cScale= {60, 62, 64, 65, 67, 69, 71, 72};
	
	public void onInit(Controller controller) {
        System.out.println("Initialized");
//        checkNote((float) 0.5,8.0);
        testNote();
        
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
    
    
    private void testNote()
    {
    	for (float i = 0; i < 8.0; i +=1.0)
    	{
    		checkNote (i, 8.0);
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
    	
    	for (double i = 0.0; i < numNotes; i +=1.0)
    	{
    		System.out.print("DEBUG INFO | ");
			System.out.print("i: "+ i+ " | ");
			System.out.print("x:" + x+ " | ");
			System.out.println("i*div: "+ i*div);
    		
    		try {
    			
    			Synthesizer synth = MidiSystem.getSynthesizer();
    			synth.open();
    			MidiChannel[] channels = synth.getChannels();
    			int volume = 50;
    			int duration = 200;
    			
    			if ( i*div <= x && x <= (i+1)*div)
    			{
    				System.out.println("Playing: ");
    				channels[0].noteOn(cScale[(int)i], volume);
    				Thread.sleep(duration);
    				channels[0].noteOff(cScale[(int)i]);
    				synth.close();
    				
    				return (int)i;	// Call function here
    				
    			}
    		}
    		catch (Exception e)
    		{
    			e.printStackTrace();
    		}
    	}
    	return 0;	// Return 0 default to silence warning*/
    	
    }
}