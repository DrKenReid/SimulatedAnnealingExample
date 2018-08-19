/*
 * Copyright: Ken Reid
 * E: Ken@kenreid.co.uk
 *
 */
package uk.co.kenreid.dataobjects;

import java.util.HashMap;

/**
 * The Class TestParameter. Contains input data. When building more complex
 * simulated annealing programs, TestParameter can be held in a
 * List<TestParameter> and run with a variety of parameters (e.g. differing
 * temperatures, cooling rates, soft constraint weightings etc) which can be
 * useful for testing your problem space.
 */
public class TestParameter {

	/** The cooling rate. */
	private double coolingRate;

	/** The problem. */
	private final String problem;

	/** The starting temperature. */
	private double startingTemperature;

	/** The verbose. */
	private boolean verbose;

	/** The weightings per constraint. */
	private HashMap<String, Double> weightingsPerConstraint;

	/**
	 * Instantiates a new test parameter.
	 *
	 * @param startingTemperature
	 *            the starting temperature
	 * @param coolingRate
	 *            the cooling rate
	 * @param weightingsPerConstraint
	 *            the weightings per constraint
	 * @param problem
	 *            the problem
	 * @param verbose
	 *            the verbose
	 */
	public TestParameter(final double startingTemperature, final double coolingRate, final HashMap<String, Double> weightingsPerConstraint,
			final String problem, final boolean verbose) {
		super();
		this.startingTemperature = startingTemperature;
		this.coolingRate = coolingRate;
		this.weightingsPerConstraint = weightingsPerConstraint;
		this.problem = problem;
		this.setVerbose(verbose);
	}

	/**
	 * Gets the cooling rate.
	 *
	 * @return the cooling rate
	 */
	public double getCoolingRate() {
		return this.coolingRate;
	}

	/**
	 * Gets the problem.
	 *
	 * @return the problem
	 */
	public String getProblem() {
		return this.problem;
	}

	/**
	 * Gets the starting temperature.
	 *
	 * @return the starting temperature
	 */
	public double getStartingTemperature() {
		return this.startingTemperature;
	}

	/**
	 * Gets the weightings per constraint.
	 *
	 * @return the weightings per constraint
	 */
	public HashMap<String, Double> getWeightingsPerConstraint() {
		return this.weightingsPerConstraint;
	}

	/**
	 * Checks if is verbose.
	 *
	 * @return true, if is verbose
	 */
	public boolean isVerbose() {
		return this.verbose;
	}

	/**
	 * Sets the cooling rate.
	 *
	 * @param coolingRate
	 *            the new cooling rate
	 */
	public void setCoolingRate(final double coolingRate) {
		this.coolingRate = coolingRate;
	}

	/**
	 * Sets the starting temperature.
	 *
	 * @param startingTemperature
	 *            the new starting temperature
	 */
	public void setStartingTemperature(final double startingTemperature) {
		this.startingTemperature = startingTemperature;
	}

	/**
	 * Sets the verbose.
	 *
	 * @param verbose
	 *            the new verbose
	 */
	public void setVerbose(final boolean verbose) {
		this.verbose = verbose;
	}

	/**
	 * Sets the weightings per constraint.
	 *
	 * @param weightingsPerConstraint
	 *            the weightings per constraint
	 */
	public void setWeightingsPerConstraint(final HashMap<String, Double> weightingsPerConstraint) {
		this.weightingsPerConstraint = weightingsPerConstraint;
	}

}
