package ca.fourthreethreefour.identifiers;

import edu.first.identifiers.Input;
import edu.first.identifiers.Output;

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
