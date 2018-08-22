/*
 * E: Ken@kenreid.co.uk
 *
 */
package uk.co.kenreid.sa;

import java.util.HashMap;
import java.util.Map;

import uk.co.kenreid.dataobjects.Context;
import uk.co.kenreid.function.Function;

/**
 * The Class Fitness. Contains two soft constraints as examples, but SC2 always
 * returns 100%, and SC1 is the only one in use. Can also be used for Hard
 * Constraints, and the SCs can also be weighted (see map variables)
 */
public class Fitness {

	/** The ctx. */
	private final Context ctx;

	/** The current solution. */
	private final String currentSolution;

	/** The fitnesses per constraint without weightings applied. */
	private Map<String, Double> fitnessesPerConstraintWithoutWeightingsApplied = new HashMap<>();

	/** The fitnesses per constraint with weightings applied. */
	private Map<String, Double> fitnessesPerConstraintWithWeightingsApplied = new HashMap<>();

	/** The function. */
	Function function = new Function();

	/** The overall fitness. */
	double overallFitness = 0;

	/** The solution. */
	final String solution;

	/**
	 * Instantiates a new fitness.
	 *
	 * @param ctx
	 *            the ctx
	 * @param currentSolution
	 *            the current solution
	 */
	public Fitness(final Context ctx, final String currentSolution) {
		this.ctx = ctx;
		this.currentSolution = currentSolution;
		this.solution = ctx.getProblem();

		// run soft constraint checks, store as is
		this.getFitnessesPerConstraintWithoutWeightingsApplied().put("SC1", this.sc1());
		this.getFitnessesPerConstraintWithoutWeightingsApplied().put("SC2", this.sc2());

		// store parameterised weighted versions
		this.getFitnessesPerConstraintWithWeightingsApplied().put("SC1",
				this.fitnessesPerConstraintWithoutWeightingsApplied.get("SC1") * this.ctx.getWeightingsPerConstraint().get("SC1"));
		this.getFitnessesPerConstraintWithWeightingsApplied().put("SC2",
				this.fitnessesPerConstraintWithoutWeightingsApplied.get("SC2") * this.ctx.getWeightingsPerConstraint().get("SC2"));

		// store overall fitness as weighted fitness.
		// optionally you could place unweighted fitness as the "overallFitness", but
		// parameterising the weightings evenly has the same effect.
		final double weightedSCFitness = this.getFitnessesPerConstraintWithWeightingsApplied().values().stream().mapToDouble(Number::doubleValue).sum();
		this.overallFitness = weightedSCFitness;
	}

	/**
	 * Gets the constraints and fitness for best solution.
	 *
	 * @return the constraints and fitness for best solution
	 */
	public Map<String, Double> getConstraintsAndFitnessForBestSolution() {
		final Map<String, Double> map = new HashMap<>();
		map.put("SC1", this.sc1());
		map.put("SC2", this.sc2());
		this.overallFitness = (map.get("SC1") + map.get("SC2")) / map.size();
		return map;
	}

	/**
	 * Gets the fitnesses per constraint without weightings applied.
	 *
	 * @return the fitnesses per constraint without weightings applied
	 */
	public Map<String, Double> getFitnessesPerConstraintWithoutWeightingsApplied() {
		return this.fitnessesPerConstraintWithoutWeightingsApplied;
	}

	/**
	 * Gets the fitnesses per constraint with weightings applied.
	 *
	 * @return the fitnesses per constraint with weightings applied
	 */
	public Map<String, Double> getFitnessesPerConstraintWithWeightingsApplied() {
		return this.fitnessesPerConstraintWithWeightingsApplied;
	}

	/**
	 * Gets the overall fitness.
	 *
	 * @return the overall fitness
	 */
	public double getOverallFitness() {
		return this.overallFitness;
	}

	/**
	 * Sc 1.
	 *
	 * @return the double
	 */
	private double sc1() {
		double failCounter = 0;
		int lengthOfShortestString = 0;
		if (this.currentSolution.length() > this.solution.length()) {
			lengthOfShortestString = this.solution.length();
		}
		else {
			lengthOfShortestString = this.currentSolution.length();
		}
		for (int charCounter = 0; charCounter < lengthOfShortestString; charCounter++) {
			final char correctChar = this.solution.charAt(charCounter);
			final char attemptChar = this.currentSolution.charAt(charCounter);
			if (correctChar != attemptChar) {
				failCounter++;
			}
		}
		final double fitness = (lengthOfShortestString - failCounter) / lengthOfShortestString;
		return fitness;
	}

	/**
	 * Sc 2.
	 *
	 * @return the double
	 */
	private double sc2() {
		return 1;
	}

	/**
	 * Sets the fitnesses per constraint without weightings applied.
	 *
	 * @param fitnessesPerConstraintWithoutWeightingsApplied
	 *            the fitnesses per constraint without weightings applied
	 */
	public void setFitnessesPerConstraintWithoutWeightingsApplied(final Map<String, Double> fitnessesPerConstraintWithoutWeightingsApplied) {
		this.fitnessesPerConstraintWithoutWeightingsApplied = fitnessesPerConstraintWithoutWeightingsApplied;
	}

	/**
	 * Sets the fitnesses per constraint with weightings applied.
	 *
	 * @param fitnessesPerConstraintWithWeightingsApplied
	 *            the fitnesses per constraint with weightings applied
	 */
	public void setFitnessesPerConstraintWithWeightingsApplied(final Map<String, Double> fitnessesPerConstraintWithWeightingsApplied) {
		this.fitnessesPerConstraintWithWeightingsApplied = fitnessesPerConstraintWithWeightingsApplied;
	}

}
