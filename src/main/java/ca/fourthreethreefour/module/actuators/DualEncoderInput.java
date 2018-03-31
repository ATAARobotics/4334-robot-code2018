package main.java.ca.fourthreethreefour.module.actuators;

import edu.first.identifiers.Input;
import edu.first.module.sensors.EncoderModule;

/**
 * Adds together the values of two encoders and uses that value as an {@link Input}.
 * 
 */
public class DualEncoderInput implements Input {

	EncoderModule left, right;
	
	public DualEncoderInput(EncoderModule left, EncoderModule right) {
		this.left = left;
		this.right = right;
	}
	
	@Override
	public double get() {
		return -left.get() + right.get();
	}
	
}

