/*
 * Copyright: Ken Reid
 * E: Ken@kenreid.co.uk
 *
 *
 *
 */
package uk.co.kenreid.dataobjects;

import java.util.HashMap;
import java.util.Map;

/**
 * The Class Context. Context is the go to class for storing settings and
 * during-run parameters. Generally accessible from most classes.
 */
public class Context {

	/** The cooling rate. */
	private double coolingRate;

	/** The original solution fitness. */
	private Map<String, Double> originalSolutionFitness = new HashMap<>();

	/** The original solution weighted fitness. */
	private Map<String, Double> originalSolutionWeightedFitness = new HashMap<>();

	/** The problem. */
	private final String problem = "1-7-3-4-6-7-3-2-1-4-7-6-Charlie-3-2-7-8-9-7-7-7-6-4-3-Tango-7-3-2-Victor-7-3-1-1-7-8-8-8-7-3-2-4-7-6-7-8-9-7-6-4-3-7-6";

	/** The temperature. */
	private double temperature;

	/** The verbose. */
	private boolean verbose;

	/** The weightings per constraint. */
	private Map<String, Double> weightingsPerConstraint;

	/**
	 * Copy.
	 *
	 * @return the context
	 */
	public Context copy() {
		final Context ctx = new Context();
		ctx.setWeightingsPerConstraint(new HashMap<>(this.weightingsPerConstraint));
		ctx.setStartingTemperature(this.getTemperature());
		ctx.setCoolingRate(this.getCoolingRate());
		ctx.setOriginalSolutionFitness(this.originalSolutionFitness);
		ctx.setTemperature(this.temperature);
		return ctx;
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
	 * Gets the original solution fitness.
	 *
	 * @return the original solution fitness
	 */
	public Map<String, Double> getOriginalSolutionFitness() {
		return this.originalSolutionFitness;
	}

	/**
	 * Gets the original solution weighted fitness.
	 *
	 * @return the original solution weighted fitness
	 */
	public Map<String, Double> getOriginalSolutionWeightedFitness() {
		return this.originalSolutionWeightedFitness;
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
	 * Gets the temperature.
	 *
	 * @return the temperature
	 */
	public double getTemperature() {
		return this.temperature;
	}

	/**
	 * Gets the weightings per constraint.
	 *
	 * @return the weightings per constraint
	 */
	public Map<String, Double> getWeightingsPerConstraint() {
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
	 * Sets the original solution fitness.
	 *
	 * @param originalSolutionFitness
	 *            the original solution fitness
	 */
	public void setOriginalSolutionFitness(final Map<String, Double> originalSolutionFitness) {
		this.originalSolutionFitness = originalSolutionFitness;
	}

	/**
	 * Sets the original solution weighted fitness.
	 *
	 * @param originalSolutionWeightedFitness
	 *            the original solution weighted fitness
	 */
	public void setOriginalSolutionWeightedFitness(final Map<String, Double> originalSolutionWeightedFitness) {
		this.originalSolutionWeightedFitness = originalSolutionWeightedFitness;
	}

	/**
	 * Sets the starting temperature.
	 *
	 * @param temperature
	 *            the new starting temperature
	 */
	public void setStartingTemperature(final double temperature) {
		this.setTemperature(temperature);
	}

	/**
	 * Sets the temperature.
	 *
	 * @param temperature
	 *            the new temperature
	 */
	public void setTemperature(final double temperature) {
		this.temperature = temperature;
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
	 * Sets the weightings.
	 *
	 * @param weightingsPerConstraint
	 *            the weightings per constraint
	 */
	public void setWeightings(final Map<String, Double> weightingsPerConstraint) {
		this.setWeightingsPerConstraint(weightingsPerConstraint);
	}

	/**
	 * Sets the weightings per constraint.
	 *
	 * @param weightingsPerConstraint
	 *            the weightings per constraint
	 */
	public void setWeightingsPerConstraint(final Map<String, Double> weightingsPerConstraint) {
		this.weightingsPerConstraint = weightingsPerConstraint;
	}

}
