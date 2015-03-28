package src.main.java.ca.uwo.csd.hackwestern;

import com.leapmotion.leap.*;

public class ProcessFrame{
	
	// Attributes
	private double[] cScale= {261.63, 293.66, 329.63, 349.23, 392.00, 440.00, 493.88, 523.25};
	private double[] dMinScale = {293.66, 329.63, 349.23, 392.00, 440.00, 466.16, 523.25, 587.33};
	private SawFaders sawFader;
	
	// Default Constructor
	public ProcessFrame(SawFaders sFade)
	{
		sawFader = sFade;
	}
	
	/**
	 * process will take a frame as a parameter and check it for events
	 * */
	public void process(Frame f)
	{
		System.out.println("processing");
		//GestureList gestures = f.gestures();
	    InteractionBox i_box = f.interactionBox();    	
	       
    	//Get hands
        for(Hand hand : f.hands()) {
        	
        	Vector normalizedHandPosition = i_box.normalizePoint(hand.palmPosition());
            float normalizedX = normalizedHandPosition.getX();
            double normalizedY = normalizedHandPosition.getY();
            //double y = hand.palmPosition().getY();
            double finalX = checkNote(normalizedX, 8.0);
            modifyPitch(finalX);
            sawFader.setAmplitude(normalizedY);
            System.out.println("Note: " + normalizedX);
            System.out.println("finalX: " + finalX);
            System.out.println("Amplitude: " + normalizedY);
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
	public void modifyPitch(double pitch){
		sawFader.setFrequency(cScale[(int)pitch]);
    	sawFader.updatePosition();	
	}
}
