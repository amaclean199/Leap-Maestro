import java.io.IOException;
import com.leapmotion.leap.*;

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
