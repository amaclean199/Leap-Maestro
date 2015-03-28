package ca.uwo.csd.hackwestern;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;

import com.leapmotion.leap.*;


public class ProcessFrame {
	
	// Attributes
	private int[] cScale= {60, 62, 64, 65, 67, 69, 71, 72};
	double previous = 0;
	
	// Default Constructor
	public ProcessFrame()
	{
		
	}
	
	
	/**
	 * process will take a frame as a parameter and check it for events
	 * */
	public void process(Frame f, Frame previousF)
	{
		
		if (f == null | previousF == null)
		{
			return;
		}
		else
		{

			System.out.println("processing");

			GestureList gestures = f.gestures();
	        InteractionBox i_box = f.interactionBox();

	    	//Get hands
	        for(Hand hand : f.hands()) {
	        	
	        	Vector normalizedHandPosition = i_box.normalizePoint(hand.palmPosition());
	            float normalizedX = normalizedHandPosition.getX();
	            double finalX = checkNote(normalizedX, 8.0);
	            System.out.println("Note: " + normalizedX + "finalX: " + finalX);
	            	            
	            if (finalX == previous)
	            {
	            	return;
	            }
	            else
	            {
	            	playNote((int)finalX);
	            	previous = finalX;
	            }

	        }

	    	if (!f.hands().isEmpty() || !gestures.isEmpty()) {
	            System.out.println();
	        }
			
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
//    		System.out.print("DEBUG INFO | ");
//			System.out.print("i: "+ i+ " | ");
//			System.out.print("x:" + x+ " | ");
//			System.out.println("i*div: "+ i*div);
    			
    		if (i*div <= x && x <= (i+1)*div) {
    			note = i;
    			break;
    		}
    	}
    	return note;	// Return 0 default to silence warning*/
    }
	
	/**
	 * playNote will take a note and play it based on the pitch provided
	 * @pitch is the pitch of the scale
	 * */
	private void playNote(int pitch){
    	try {
			
			Synthesizer synth = MidiSystem.getSynthesizer();
			synth.open();
			MidiChannel[] channels = synth.getChannels();
			int volume = 50;
			int duration = 300;
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
