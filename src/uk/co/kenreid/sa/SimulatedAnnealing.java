/*
 * E: Ken@kenreid.co.uk
 *
 */
package uk.co.kenreid.sa;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import uk.co.kenreid.dataobjects.Context;
import uk.co.kenreid.function.Function;

/**
 * The Class SimulatedAnnealing. Fairly simple example of SA. Basically works by
 * saying "Generate random solution. If time is left AND solution isn't perfect,
 * loop. Mutate the current solution slightly. If it's improved, keep it. If
 * it's not improved, maybe keep it, where the "maybe" is really likely early in
 * the run, unlikely later." This is where fiddling with temperature and cooling
 * rate is useful, problem dependent.
 *
 * For a useful run down of simulated annealing I recommend
 * http://www.theprojectspot.com/tutorial-post/simulated-annealing-algorithm-for-beginners/6
 */
public class SimulatedAnnealing {

	/** The accepted solutions. */
	List<String> acceptedSolutions = new ArrayList<>();

	/** The cooling rate. */
	private double coolingRate;

	/** The ctx. */
	private final Context ctx;

	/** The current solution. */
	private String currentSolution;

	/** The fitnesses. */
	private final List<Double> fitnesses = new ArrayList<>();

	/** The fitness object over time. */
	private LinkedHashMap<Integer, Fitness> fitnessObjectOverTime = new LinkedHashMap<>();

	/** The function. */
	Function function = new Function();

	/** The temperature. */
	private double temperature;

	/**
	 * Instantiates a new simulated annealing.
	 *
	 * @param ctx
	 *            the ctx
	 */
	public SimulatedAnnealing(final Context ctx) {
		this.ctx = ctx;
	}

	/**
	 * Acceptance probability.
	 *
	 * @param originalSolutionFitness
	 *            the original solution fitness
	 * @param mutatedSolutionFitness
	 *            the mutated solution fitness
	 * @return the double
	 */
	private double acceptanceProbability(final double originalSolutionFitness, final double mutatedSolutionFitness) {
		// if fitness has improved, keep it!
		final double originalEnergyCost = (1 - originalSolutionFitness) * 10000;
		final double newEnergyCost = (1 - mutatedSolutionFitness) * 10000;

		if (newEnergyCost < originalEnergyCost) {
			return 1.0;
		}
		// otherwise, MAYBE keep it
		final double chance = 1 / (1 + Math.exp((newEnergyCost - originalEnergyCost) / this.temperature));
		return chance;
	}

	/**
	 * Cool system.
	 */
	private void coolSystem() {
		this.temperature *= 1 - this.coolingRate;
	}

	/**
	 * Creates the random string. Thanks to here for this code:
	 * https://www.baeldung.com/java-random-string This can theoretically be used
	 * for any string input.
	 *
	 * @return the string
	 */
	private String createRandomString() {
		final String potentialCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		final int solutionLength = this.ctx.getProblem().length();
		String initialAttempt = "";
		while (initialAttempt.length() != solutionLength) {
			initialAttempt = initialAttempt + potentialCharacters.charAt(this.function.getRandom().nextInt(potentialCharacters.length()));
		}
		return initialAttempt;

	}

	/**
	 * Gets the accepted solutions.
	 *
	 * @return the accepted solutions
	 */
	public List<String> getAcceptedSolutions() {
		return this.acceptedSolutions;
	}

	/**
	 * Gets the current solution.
	 *
	 * @return the current solution
	 */
	public String getCurrentSolution() {
		return this.currentSolution;
	}

	/**
	 * Gets the fitness object over time.
	 *
	 * @return the fitness object over time
	 */
	public LinkedHashMap<Integer, Fitness> getFitnessObjectOverTime() {
		return this.fitnessObjectOverTime;
	}

	/**
	 * Gets the fitness over time.
	 *
	 * @return the fitness over time
	 */
	public List<Double> getFitnessOverTime() {
		return this.fitnesses;
	}

	/**
	 * Go.
	 */
	public void go() {

		// analytical vars
		double prevFitness = 0.0;
		double currFitness = 0.0;
		double bestFitness = 0.0;

		// sA specific vars
		this.temperature = this.ctx.getTemperature();
		this.coolingRate = this.ctx.getCoolingRate();

		// Initial solution
		this.setCurrentSolution(this.createRandomString());
		String previousSolution;
		final Fitness originalFitness = new Fitness(this.ctx, this.getCurrentSolution());
		// Store original fitness for output later
		this.ctx.setOriginalSolutionFitness(originalFitness.getFitnessesPerConstraintWithoutWeightingsApplied());
		this.ctx.setOriginalSolutionWeightedFitness(originalFitness.getFitnessesPerConstraintWithWeightingsApplied());

		if (this.ctx.isVerbose()) {
			System.out.println("\nInitial attempt:\n" + this.getCurrentSolution());
			System.out.println("Actual solution is:\n" + this.ctx.getProblem());
			System.out.println("Initial fitness is: " + originalFitness.getOverallFitness());
		}

		int iterations = 0;

		while (this.temperature > 0.00001 && currFitness != 1) {
			// Save previous solution in case we revert.
			// remember strings are immutable, so this is fine.
			previousSolution = this.getCurrentSolution();

			// change a random character in the String.
			this.setCurrentSolution(this.mutateString(this.getCurrentSolution()));

			// analyze new state
			final Fitness fitness = new Fitness(this.ctx, this.getCurrentSolution());

			this.getFitnessObjectOverTime().put(iterations, fitness);

			// new fitness value
			currFitness = fitness.getOverallFitness();
			// overall fitness over time saved for analytics
			this.fitnesses.add(currFitness);

			// decide to keep or not, depending on runtime.
			final double acceptanceProbability = this.acceptanceProbability(prevFitness, currFitness);

			// accept change
			if (acceptanceProbability > this.function.getRandom().nextDouble()) {
				if (this.ctx.isVerbose()) {
					System.out.println(this.getCurrentSolution());
				}

				// store each improved solution
				this.acceptedSolutions.add(this.getCurrentSolution());

				// set previous to current, as we accept this change.
				prevFitness = currFitness;
				previousSolution = this.getCurrentSolution();

				// if best found yet, save it!
				if (currFitness > bestFitness) {
					bestFitness = currFitness;
				}
			}

			// reject change
			else {
				this.setCurrentSolution(previousSolution);
			}

			// cool simulated annealing
			this.coolSystem();
			iterations++;

		}
		String recommendation = "";
		if (bestFitness != 1.0) {
			recommendation = ". It is recommended to run for more iterations to get the correct answer.";
		}
		if (this.ctx.isVerbose()) {
			System.out.println("\nAfter " + iterations + " iterations, this solution was produced (solution directly below for comparison):");
			System.out.println(this.getCurrentSolution());
			System.out.println(this.ctx.getProblem());
			System.out.println("Best fitness: " + bestFitness + recommendation + "\n");
		}
	}

	/**
	 * Mutate string.
	 *
	 * @param currentSolution
	 *            the current solution
	 * @return the string
	 */
	private String mutateString(final String currentSolution) {
		// probably easiest way to mutate string is like this, without importing any
		// libraries to allow a regex-ish generator.

		// can create string with any of these following characters
		final String potentials = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890-";

		// builder used to modify string
		final StringBuilder builder = new StringBuilder(currentSolution);

		// character at a random point in the string is modified with a random character
		// from the potentials
		builder.setCharAt(this.function.getRandom().nextInt(currentSolution.length()),
				potentials.charAt(this.function.getRandom().nextInt(potentials.length())));
		return builder.toString();
	}

	/**
	 * Sets the current solution.
	 *
	 * @param currentSolution
	 *            the new current solution
	 */
	public void setCurrentSolution(final String currentSolution) {
		this.currentSolution = currentSolution;
	}

	/**
	 * Sets the fitness object over time.
	 *
	 * @param fitnessObjectOverTime
	 *            the fitness object over time
	 */
	public void setFitnessObjectOverTime(final LinkedHashMap<Integer, Fitness> fitnessObjectOverTime) {
		this.fitnessObjectOverTime = fitnessObjectOverTime;
	}

}
