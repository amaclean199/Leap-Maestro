package ca.uwo.csd.hackwestern;

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

    	//Get hands
        for(Hand hand : frame.hands()) {
        	
        	Vector normalizedHandPosition = i_box.normalizePoint(hand.palmPosition());
            float normalizedX = normalizedHandPosition.getX();
            double finalX = checkNote(normalizedX, 8.0);
            System.out.println("Note: " + normalizedX + "finalX: " + finalX);
            playNote((int)finalX);
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
    private double checkNote(Float x, Double numNotes) {
    	
    	double note = -1;
    	double div = 1.0/numNotes;	// Represents the space allocated to each "note"
    	
    	System.out.println("div: " + div);
    	
    	for (double i = 0.0; i < numNotes; i +=1.0) {
    		System.out.print("DEBUG INFO | ");
			System.out.print("i: "+ i+ " | ");
			System.out.print("x:" + x+ " | ");
			System.out.println("i*div: "+ i*div);
    			
    		if (i*div <= x && x <= (i+1)*div) {
    			note = i;
    			break;
    		}
    	}
    	return note;	// Return 0 default to silence warning*/
    }
    
    private void playNote(int pitch){
    	try {
			
			Synthesizer synth = MidiSystem.getSynthesizer();
			synth.open();
			MidiChannel[] channels = synth.getChannels();
			int volume = 50;
			int duration = 1000;
			System.out.println("Playing: ");
			channels[5].noteOn(cScale[pitch], volume);
			Thread.sleep(duration);
			channels[5].noteOff(cScale[pitch]);
			synth.close();	
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
    }
}