package src.main.java.ca.uwo.csd.hackwestern;

import com.leapmotion.leap.*;

public class ProcessFrame{
	
	private static final double[] C_MAJ = {261.63, 293.66, 329.63, 349.23, 392.00, 440.00, 493.88, 523.25, 587.33, 659.25, 698.46, 783.99, 880.00, 987.77, 1046.50};
	private static final double[] D_MIN = {293.66, 329.63, 349.23, 392.00, 440.00, 466.16, 523.25, 587.33, 659.25, 698.46, 783.99, 880.00, 932.33, 1046.50, 1174.66};
	
	// Attributes
	private double[] scale = new double[8];
	private double[] fullScale = new double[15];
	private SawFaders sawFader;
	
	// Default Constructor
	public ProcessFrame(SawFaders sFade, char c)
	{
		sawFader = sFade;
		if (c == 'C') {
			System.arraycopy(C_MAJ, 0, scale, 0, 8);
			fullScale = C_MAJ;
		}
		else if (c == 'D') {
			System.arraycopy(D_MIN, 0, scale, 0, 8);
			fullScale = D_MIN;
		}
	}
	
	/**
	 * process will take a frame as a parameter and check it for events
	 * */
	public void process(Frame f) {
	    InteractionBox i_box = f.interactionBox();
	    
	    if (f.hands().isEmpty()) {
	    	sawFader.setAmplitude(-0.4);
	    }
	    else {
	    	//Get hands
	        for(Hand hand : f.hands()) {
	        	Vector normalizedHandPosition = i_box.normalizePoint(hand.palmPosition());
	        	if (hand.isRight()) {
		            double normalizedX = normalizedHandPosition.getX();
		            double normalizedY = normalizedHandPosition.getY();
		            double normalizedZ = normalizedHandPosition.getZ();
		            double finalX = checkNote(normalizedX, 15.0);
		            sawFader.getSine().frequency.set(5-normalizedZ*5);
		            modifyPitch(finalX);
		            sawFader.setAmplitude(normalizedY);
		            //System.out.println("Note: " + normalizedX);
		            //System.out.println("finalX: " + finalX);
		            //System.out.println("Amplitude: " + normalizedY);
				}
	        }
	        for(Gesture gesture : f.gestures()) {
	        	if(gesture.type() == Gesture.Type.TYPE_SWIPE) {
	        	    SwipeGesture swipeGesture = new SwipeGesture(gesture);
	        	    Vector swipeDirection = swipeGesture.direction();
	        	    System.out.println(swipeDirection);
	        	    if (swipeDirection.getX() < 0) {
	        	    	scaleUp();
	        	    }
	        	    if (swipeDirection.getX() > 0) {
	        	    	scaleDown();
	        	    }
	        	}
	        	
	        }
	    }
	}
	
    /**
     * checkNote checks where the "x" value is in terms of the interaction Box
     * @x is the NORMALIZED x position of the finger [0,1]
     * @frameWidth is the default width of the frame's interaction box
     * @numNotes is the number of bars to be generated
     */
    private double checkNote(double x, double numNotes) {
    	
    	double note = -1;
    	double div = 1.0/numNotes;	// Represents the space allocated to each "note"
    	
    	//System.out.println("div: " + div);
    	
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
	private void modifyPitch(double pitch){
		sawFader.setFrequency(fullScale[(int)pitch]);
    	sawFader.updatePosition();	 
	}
	
	private void scaleUp() {
		if (scale[0] == fullScale[0]) {
		System.arraycopy(fullScale, 7, scale, 0, 8);
		System.out.println("up");
		}
	}
	
	private void scaleDown() {
		if (scale[0] == fullScale[7]) {
			System.arraycopy(fullScale, 0, scale, 0, 8);
			System.out.println("Down");
		}
	}
}
