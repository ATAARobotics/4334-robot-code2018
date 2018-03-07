package main.java.ca.fourthreethreefour.identifiers;

import edu.first.identifiers.Input;
import edu.first.identifiers.Output;

/**
 * Object of type {@link Input} and {@link Output}. Value can be set and received.
 */
public class InputOutput implements Input, Output {

	private double value;
	
	@Override
	public void set(double value) {
		this.value = value;
	}

	@Override
	public double get() {
		return value;
	}

}
