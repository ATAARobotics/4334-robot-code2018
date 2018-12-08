package main.java.ca.fourthreethreefour.identifiers;

public class VolatileInputOutput extends InputOutput {

	private volatile double value;
	
	@Override
	public void set(double value) {
		this.value = value;
	}
	
	@Override
	public double get() {
		return value;
	}
	
}
