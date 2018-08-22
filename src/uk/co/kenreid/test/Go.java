/*
 * E: Ken@kenreid.co.uk
 *
 */
package uk.co.kenreid.test;

import java.util.List;
import java.util.Scanner;

import uk.co.kenreid.dataobjects.TestParameter;
import uk.co.kenreid.io.Input;

/**
 * The Class Go.
 */
public class Go {

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(final String[] args) {
		final Go go = new Go();
		go.go(args);
	}

	/**
	 * Go.
	 *
	 * @param args
	 *            the args
	 */
	public void go(final String[] args) {
		// Info messages, etc
		this.prepUser();

		/*
		 * Read parameters
		 */
		Input input = null;
		if (args != null && args.length != 0 && args[0] != "") {
			input = new Input(args[0]);
		}
		else {
			input = new Input(System.getProperty("user.dir") + "\\data\\input\\");
		}

		/*
		 * In this example only 1 is tested, left in the reading capability for multiple
		 * parameter lines since most programmers will want to run a number of parameter
		 * settings.
		 */
		final List<TestParameter> tPs = input.readParameters();
		final TestParameter tP = tPs.get(0);

		/*
		 * Begin!
		 */
		final Test test = new Test(tP);
		test.go();
	}

	private void prepUser() {
		/*
		 * Info message
		 */
		System.out.println("Simulated Annealing example running.");
		this.sleep(500);
		System.out.println("Attempting to work out the following passcode:");
		this.sleep(10);
		System.err.println("1-7-3-4-6-7-3-2-1-4-7-6-Charlie-3-2-7-8-9-7-7-7-6-4-3-Tango-7-3-2-Victor-7-3-1-1-7-8-8-8-7-3-2-4-7-6-7-8-9-7-6-4-3-7-6\n");
		this.sleep(5000);
		System.out.println("Using SA, the processor generates a random String, then modifies it,\ntesting if it is closer or further each iteration");
		this.sleep(3000);
		System.out.println("Our \"Hard Constraint\" will be hard-coded, in the form that the \nString must be the exact length of the above solution.");
		this.sleep(3000);
		System.out.println("\nA single argument specifying the path for the params.xlsx file is expected. \nIf no path is provided, it's assumed here:\n");
		this.sleep(500);
		System.err.println(">" + System.getProperty("user.dir") + "\\data\\input\n");
		this.sleep(3000);
		System.out.println("\n\nReady to begin? y/n");
		final Scanner scanner = new Scanner(System.in);
		final String ready = scanner.nextLine();
		if (!ready.equals("y")) {
			System.exit(0);
		}
		scanner.close();
	}

	/**
	 * Sleep.
	 *
	 * @param ms
	 *            the ms
	 */
	public void sleep(final long ms) {
		try {
			Thread.sleep(ms);
		}
		catch (final InterruptedException e) {
			System.err.println(e);
		}
	}

}
