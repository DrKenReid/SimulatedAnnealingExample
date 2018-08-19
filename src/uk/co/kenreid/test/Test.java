/*
 * Copyright: Ken Reid
 * E: Ken@kenreid.co.uk
 *
 */
package uk.co.kenreid.test;

import java.util.HashMap;

import uk.co.kenreid.dataobjects.Context;
import uk.co.kenreid.dataobjects.TestParameter;
import uk.co.kenreid.function.Function;
import uk.co.kenreid.io.Output;
import uk.co.kenreid.sa.SimulatedAnnealing;

/**
 * The Class Test. Receives input, begins SA, calls output.
 */
public class Test {

	/** The cooling rate. */
	private double coolingRate;

	/** The function. */
	Function function = new uk.co.kenreid.function.Function();

	/** The starting temperature. */
	private double startingTemperature;

	/** The verbose. */
	private final boolean verbose;

	/** The weightings per constraint. */
	private HashMap<String, Double> weightingsPerConstraint;

	/**
	 * Instantiates a new test.
	 *
	 * @param params
	 *            the params
	 */
	public Test(final TestParameter params) {
		this.startingTemperature = params.getStartingTemperature();
		this.coolingRate = params.getCoolingRate();
		this.weightingsPerConstraint = params.getWeightingsPerConstraint();
		this.verbose = params.isVerbose();
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
	 * Go.
	 */
	void go() {

		/*
		 * Record start time.
		 */
		final long start = System.currentTimeMillis();

		/*
		 * Context is a nice place to store some parameters.
		 */
		final Context ctx = new Context();
		ctx.setStartingTemperature(this.startingTemperature);
		ctx.setCoolingRate(this.coolingRate);
		ctx.setVerbose(this.verbose);
		// We can weight parameters in this example to have preference for one or the
		// other. By default these will be equal.
		ctx.setWeightings(this.weightingsPerConstraint);

		/*
		 * Begin algorithm proper.
		 */
		final SimulatedAnnealing sA = new SimulatedAnnealing(ctx);
		sA.go();

		final long end = System.currentTimeMillis();

		System.out.println("Outputting results...");

		/*
		 * Output some runtime data. Currently outputs some xlsx files, but can be
		 * easily converted to CSV or JSON etc.
		 */
		final Output output = new Output(sA, ctx, sA.getCurrentSolution());
		output.outputAll();

		final long realEnd = System.currentTimeMillis();

		/*
		 * Print algorithmic + output runtime.
		 */

		System.out.println("Completed sA after " + (end / 1000 - start / 1000) + " seconds. Completed output after " + (realEnd / 1000 - end / 1000));
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
	 * Sets the weightings per constraint.
	 *
	 * @param weightingsPerConstraint
	 *            the weightings per constraint
	 */
	public void setWeightingsPerConstraint(final HashMap<String, Double> weightingsPerConstraint) {
		this.weightingsPerConstraint = weightingsPerConstraint;
	}
}
