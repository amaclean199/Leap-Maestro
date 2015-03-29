package src.main.java.ca.uwo.csd.hackwestern;

import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.jsyn.*;
import com.jsyn.swing.ExponentialRangeModel;
import com.jsyn.swing.PortControllerFactory;
import com.jsyn.swing.PortModelFactory;
import com.jsyn.swing.RotaryTextController;
import com.jsyn.unitgen.AsymptoticRamp;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.SawtoothOscillator;
import com.jsyn.unitgen.SawtoothOscillatorBL;
import com.jsyn.unitgen.UnitOscillator;
import com.jsyn.unitgen.SineOscillator;
import com.jsyn.scope.AudioScope;

/**
 * Play a tone using a JSyn oscillator and some knobs.
 * 
 * @author Phil Burk (C) 2010 Mobileer Inc
 * 
 */
public class SawFaders extends JApplet
{
	private static final long serialVersionUID = -2704222221111608377L;
	private Synthesizer synth;
	private UnitOscillator osc;
	private AsymptoticRamp lag;
	private LineOut lineOut;
	private double customFrq;
	private Component x;
	private ExponentialRangeModel amplitudeModel;
	private SineOscillator sine;
	private AudioScope scope;

	public void init()
	{
		synth = JSyn.createSynthesizer(); 
		
		// Add a tone generator.
		synth.add( osc = new SawtoothOscillatorBL() );
		synth.add( sine = new SineOscillator() );
		//synth.add( red = new SawtoothOscillator() );
		sine.frequency.set(0.5);
		//red.amplitude.set(-0.3);
		
		// Add a lag to smooth out amplitude changes and avoid pops.
		synth.add(lag = new AsymptoticRamp());

		// Add an output mixer.
		synth.add(lineOut = new LineOut());

		// Connect the oscillator to the output.
		osc.output.connect(0, lineOut.input, 0);
		osc.output.connect(0, lineOut.input, 1);
		sine.output.connect(0, lineOut.input, 0);
		sine.output.connect(0, lineOut.input, 1);
		//red.output.connect( 0, lineOut.input, 0);
		//red.output.connect( 0, lineOut.input, 1);
		
		// Set the minimum, current and maximum values for the port.
		lag.output.connect( osc.amplitude );
		lag.output.connect( sine.amplitude );
		//lag.output.connect( red.amplitude );
		lag.input.setup( 0.0, 0.5, 1.0 );

		// Arrange the faders in a stack.
		setLayout(new GridLayout(0, 1));
		
		// Add title
		addTitle("Leap Maestro");
		
		// Create Audio Scope using the synthesizer.
		scope = new AudioScope(synth);
		scope.addProbe(osc.output);
		scope.addProbe(sine.output);
		scope.setTriggerMode(AudioScope.TriggerMode.NORMAL);
		scope.getView().setControlsVisible(false);
		Border border = BorderFactory.createLineBorder(Color.BLACK);
		scope.getView().setBorder(border);
		add(scope.getView());

		// Create amplitude model and add to panel.
		amplitudeModel = PortModelFactory.createExponentialModel(lag.input);
		RotaryTextController knob = new RotaryTextController(amplitudeModel, 5);
		JPanel knobPanel = new JPanel();
		knobPanel.add(knob);
		knobPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		JLabel label = new JLabel("Amplitude", JLabel.CENTER);
		knobPanel.add(label);
		add(knobPanel);

		// Set up the oscillator frequency and amplitude.
		osc.frequency.setup(261, 300.0, 2000.0);
		osc.amplitude.setup(0, 0.5, 1);

		// Create the frequency slider and update panel.
		x = PortControllerFactory.createExponentialPortSlider(osc.frequency);
		add(x);
		validate();
	}
	
	private void addTitle(String title) {
		/*GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;*/
		add(Layout.makeTitle("Leap Maestro"));
	}
	
	public double getFrq()
	{
		return customFrq;
	}
	
	public UnitOscillator getOsc() {
		return osc;
	}
	
	public void updatePosition()
	{
		remove (x);
		x = PortControllerFactory.createExponentialPortSlider( osc.frequency );
		add( x );
		validate();
	}
	
	public void start()
	{
		// Start synthesizer using default stereo output at 44100 Hz.
		synth.start();
		// We only need to start the LineOut. It will pull data from the oscillator.
		lineOut.start();
		scope.start();
	}
	
	public SineOscillator getSine() {
		return sine;
	}
	
	public void stop()
	{
		synth.stop();
		scope.stop();
	}
	
	public void setFrequency(double freq) {
		osc.frequency.set(freq);
		customFrq = freq;
	}
	
	public void setAmplitude(double amp) {
		amplitudeModel.setDoubleValue(amp);
	}
}
