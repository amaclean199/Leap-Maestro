package src.main.java.ca.uwo.csd.hackwestern;

import java.awt.*;

import javax.swing.JApplet;
import javax.swing.JPanel;

import com.jsyn.*;
import com.jsyn.swing.ExponentialRangeModel;
import com.jsyn.swing.PortControllerFactory;
import com.jsyn.swing.PortModelFactory;
import com.jsyn.swing.RotaryTextController;
import com.jsyn.unitgen.AsymptoticRamp;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.SawtoothOscillatorBL;
import com.jsyn.unitgen.UnitOscillator;
import com.jsyn.unitgen.SineOscillator;

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
	public void init()
	{
		synth = JSyn.createSynthesizer();
		
		// Add a tone generator.
		synth.add( osc = new SawtoothOscillatorBL() );
		synth.add( sine = new SineOscillator() );
		sine.frequency.set(5);
		
		// Add a lag to smooth out amplitude changes and avoid pops.
		synth.add( lag = new AsymptoticRamp() );
		
		// Add an output mixer.
		synth.add( lineOut = new LineOut() );
		
		// Connect the oscillator to the output.
		osc.output.connect( 0, lineOut.input, 0 );
		osc.output.connect( 0, lineOut.input, 1 );
		sine.output.connect( 0, lineOut.input, 0);
		sine.output.connect( 0, lineOut.input, 1);
		
		// Set the minimum, current and maximum values for the port.
		lag.output.connect( osc.amplitude );
		lag.input.setup( 0.0, 0.5, 1.0 );

		// Arrange the faders in a stack.
		setLayout( new GridLayout( 0, 1 ) );
	
		amplitudeModel = PortModelFactory.createExponentialModel( lag.input );
		RotaryTextController knob = new RotaryTextController( amplitudeModel, 5 );
		JPanel knobPanel = new JPanel();
		knobPanel.add( knob );
		add( knobPanel );
		osc.frequency.setup( 261, 300.0, 2000.0 );
		osc.amplitude.setup(0, 0.5, 1);
		x = PortControllerFactory.createExponentialPortSlider( osc.frequency );
		add( x );
		validate();
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
	}

	public void stop()
	{
		synth.stop();
	}
	
	public void setFrequency(double freq) {
		osc.frequency.set(freq);
		customFrq = freq;
	}
	
	public void setAmplitude(double amp) {
		amplitudeModel.setDoubleValue(amp);
	}
}
