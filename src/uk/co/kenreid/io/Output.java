/*
 * Copyright: Ken Reid
 * E: Ken@kenreid.co.uk
 *
 */
package uk.co.kenreid.io;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import uk.co.kenreid.dataobjects.Context;
import uk.co.kenreid.function.Function;
import uk.co.kenreid.sa.Fitness;
import uk.co.kenreid.sa.SimulatedAnnealing;

/**
 * The Class Output. Contains some examples of output for exploring the
 * simulated annealing steps. Can be modified to output more information in
 * various formats.
 */
public class Output {

	/** The ctx. */
	private final Context ctx;

	/** The current solution. */
	private final String currentSolution;

	/** The function. */
	Function function = new Function();

	/** The path. */
	private final String path;

	/** The s A. */
	private final SimulatedAnnealing sA;

	/**
	 * Instantiates a new output.
	 *
	 * @param getsA
	 *            the gets A
	 * @param ctx2
	 *            the ctx 2
	 * @param currentSolution
	 *            the current solution
	 */
	public Output(final SimulatedAnnealing getsA, final Context ctx2, final String currentSolution) {
		this.sA = getsA;
		this.ctx = ctx2;
		this.currentSolution = currentSolution;
		// if folders don't exist to hold excel data, make 'em.
		new File("data/output").mkdirs();

		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss");
		final long ms = Calendar.getInstance().getTimeInMillis();
		final Date resultdate = new Date(ms);
		final String time = sdf.format(resultdate);

		this.path = "data/output/" + time;
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
	 * Output all.
	 */
	public void outputAll() {
		// "time" used to contain time information, but now that the folder has that,
		// it's redundant. Kept in case useful in the future.

		// final String time = "\\" + new
		// SimpleDateFormat("yyyy.MM.dd-HH.mm.ss").format(new
		// Date(Calendar.getInstance().getTimeInMillis()));

		final String time = "";

		this.outputXLSXFitnessPerIteration(time + " fitness per iteration", this.sA.getFitnessOverTime());
		this.outputXLSXFitnessOfBestSolutionComparedToOriginal(time + " Original fitness vs Best fitness");
		this.outputXLSXFitnessPerIterationDetailed(time + " fitness per iteration (detailed)");
		this.outputXLSXacceptedSolutions(time + " accepted solutions");

	}

	/**
	 * Output XLS xaccepted solutions.
	 *
	 * @param fileName
	 *            the file name
	 */
	private void outputXLSXacceptedSolutions(final String fileName) {
		try {
			final String path = this.path + fileName + ".xlsx";
			// setup
			final FileOutputStream outputStream = new FileOutputStream(path);
			final Workbook workbook = new XSSFWorkbook();
			final Sheet worksheet = workbook.createSheet("Results");

			final Row row1 = worksheet.createRow(0);

			final Cell cellA1 = row1.createCell(0);
			cellA1.setCellValue("Solutions");

			int rowNumber = 1;

			final List<String> acceptedSolutions = this.sA.getAcceptedSolutions();

			for (final String solution : acceptedSolutions) {
				final Row row = worksheet.createRow(rowNumber);

				final Cell cell1 = row.createCell(0);
				cell1.setCellValue(solution);

				rowNumber++;
			}

			this.function.makeRowBold(workbook, row1, false);

			for (int i = 0; i < 15; i++) {
				worksheet.autoSizeColumn(i);
			}
			// Save the workbook in .xls file
			workbook.write(outputStream);
			workbook.close();
			outputStream.flush();
			outputStream.close();
		}
		catch (final Exception e) {
			this.printErr(e);
		}

	}

	/**
	 * Output XLSX fitness of best solution compared to original.
	 *
	 * @param fileName
	 *            the file name
	 */
	public void outputXLSXFitnessOfBestSolutionComparedToOriginal(final String fileName) {
		try {
			final String path = this.path + fileName + ".xlsx";
			// setup
			final FileOutputStream outputStream = new FileOutputStream(path);
			final Workbook workbook = new XSSFWorkbook();
			final Sheet worksheet = workbook.createSheet("Results");

			final Row row1 = worksheet.createRow(0);

			final Cell cellA1 = row1.createCell(0);
			cellA1.setCellValue("Constraint");

			final Cell cellA2 = row1.createCell(1);
			cellA2.setCellValue("Weighted Final Fitness");

			final Cell cellA3 = row1.createCell(2);
			cellA3.setCellValue("Weighted Original value");

			final Cell cellA4 = row1.createCell(3);
			cellA4.setCellValue("Weighting");

			final Cell cellA5 = row1.createCell(4);
			cellA5.setCellValue("Unweighted Final Fitness");

			final Cell cellA6 = row1.createCell(5);
			cellA6.setCellValue("Unweighted Original value");

			int rowNumber = 1;

			final Fitness fitness = new Fitness(this.ctx, this.currentSolution);
			final Map<String, Double> bestSolutionWeightedFitness = fitness.getFitnessesPerConstraintWithWeightingsApplied();

			final Map<String, Double> originalSolutionFitnessAndConstraints = this.ctx.getOriginalSolutionWeightedFitness();

			for (final Entry<String, Double> weightedConstraintAndFitness : bestSolutionWeightedFitness.entrySet()) {
				final Row row = worksheet.createRow(rowNumber);

				final Cell cell1 = row.createCell(0);
				cell1.setCellValue(weightedConstraintAndFitness.getKey());

				final Cell cell2 = row.createCell(1);
				cell2.setCellValue(weightedConstraintAndFitness.getValue());

				final Cell cell3 = row.createCell(2);
				cell3.setCellValue(originalSolutionFitnessAndConstraints.get(weightedConstraintAndFitness.getKey()));

				final Cell cell4 = row.createCell(3);
				cell4.setCellValue(this.ctx.getWeightingsPerConstraint().get(weightedConstraintAndFitness.getKey()));

				final Cell cell5 = row.createCell(4);
				cell5.setCellValue(fitness.getFitnessesPerConstraintWithoutWeightingsApplied().get(weightedConstraintAndFitness.getKey()));

				final Cell cell6 = row.createCell(5);
				cell6.setCellValue(this.ctx.getOriginalSolutionFitness().get(weightedConstraintAndFitness.getKey()));

				rowNumber++;
			}

			final Row row = worksheet.createRow(rowNumber);

			final Cell cellB = row.createCell(1);
			cellB.setCellValue(fitness.getOverallFitness());

			final Cell cellC = row.createCell(2);
			cellC.setCellValue(originalSolutionFitnessAndConstraints.get("SC1") + originalSolutionFitnessAndConstraints.get("SC2"));

			final Cell cellD = row.createCell(3);
			cellD.setCellValue(1);

			final Cell cellE = row.createCell(4);
			double average = 0;
			for (final Map.Entry<String, Double> e : fitness.getFitnessesPerConstraintWithoutWeightingsApplied().entrySet()) {
				average += e.getValue();
			}
			average = average / fitness.getFitnessesPerConstraintWithoutWeightingsApplied().size();
			cellE.setCellValue(average);

			final Cell c = row.createCell(5);
			c.setCellValue((this.ctx.getOriginalSolutionFitness().get("SC1") + this.ctx.getOriginalSolutionFitness().get("SC2"))
					/ this.ctx.getOriginalSolutionFitness().size());

			this.function.makeRowBold(workbook, row1, false);
			this.function.makeRowBold(workbook, row, false);

			for (int i = 0; i < 15; i++) {
				worksheet.autoSizeColumn(i);
			}
			// Save the workbook in .xls file
			workbook.write(outputStream);
			workbook.close();
			outputStream.flush();
			outputStream.close();
		}
		catch (final Exception e) {
			this.printErr(e);
		}
	}

	/**
	 * Output XLSX fitness per iteration.
	 *
	 * @param fileName
	 *            the file name
	 * @param fitnesses
	 *            the fitnesses
	 */
	public void outputXLSXFitnessPerIteration(final String fileName, final List<Double> fitnesses) {
		try {
			final String path = this.path + fileName + ".xlsx";
			// setup
			final FileOutputStream outputStream = new FileOutputStream(path);
			final Workbook workbook = new XSSFWorkbook();
			final Sheet worksheet = workbook.createSheet("Results");

			// Create ROW-1
			final Row row1 = worksheet.createRow(0);

			// Create COL-A from ROW-1 and set data
			final Cell cellA1 = row1.createCell(0);
			cellA1.setCellValue("Fitness");

			this.function.makeRowBold(workbook, row1, false);

			int rowNumber = 1;
			final CellStyle currStyle = workbook.createCellStyle();
			currStyle.setDataFormat(workbook.createDataFormat().getFormat("0.000%"));

			int counter = 0;
			boolean flag = false;
			for (double d : fitnesses) {
				counter++;
				// Create ROW-1
				final Row currRow = worksheet.createRow(rowNumber);

				// Create COL-A from ROW-1 and set data
				final Cell currCell = currRow.createCell(0);
				if (Double.isNaN(d)) {
					d = 0;
				}

				currCell.setCellStyle(currStyle);
				currCell.setCellValue(d);
				rowNumber++;
				if (counter > 65533) {
					flag = true;
					break;
				}
			}
			if (flag) {
				final Row currRow = worksheet.createRow(rowNumber);

				// Create COL-A from ROW-1 and set data
				final Cell currCell = currRow.createCell(0);

				currCell.setCellStyle(currStyle);
				currCell.setCellValue("Maximum number of values now printed.");
			}
			for (int i = 0; i < 15; i++) {
				worksheet.autoSizeColumn(i);
			}
			// Save the workbook in .xls file
			workbook.write(outputStream);
			workbook.close();
			outputStream.flush();
			outputStream.close();
		}
		catch (final Exception e) {
			this.printErr(e);
		}
	}

	/**
	 * Output XLSX fitness per iteration detailed.
	 *
	 * @param fileName
	 *            the file name
	 */
	public void outputXLSXFitnessPerIterationDetailed(final String fileName) {
		try {

			final String path = this.path + fileName + ".xlsx";
			// setup
			final FileOutputStream outputStream = new FileOutputStream(path);
			final Workbook workbook = new XSSFWorkbook();
			final Sheet worksheet = workbook.createSheet("Results");

			// Create ROW-1
			final Row row1 = worksheet.createRow(0);

			// Create COL-A from ROW-1 and set data
			final Cell cellA1 = row1.createCell(0);
			cellA1.setCellValue("Iteration");

			final Cell cellB1 = row1.createCell(1);
			cellB1.setCellValue("SC1");

			final Cell cellD1 = row1.createCell(2);
			cellD1.setCellValue("SC2");

			final Cell cellF1 = row1.createCell(3);
			cellF1.setCellValue("Average");

			this.function.makeRowBold(workbook, row1, false);

			int rowNumber = 1;
			final CellStyle currStyle = workbook.createCellStyle();
			currStyle.setDataFormat(workbook.createDataFormat().getFormat("0.000%"));

			for (final Entry<Integer, Fitness> iteration : this.sA.getFitnessObjectOverTime().entrySet()) {
				// iteration number
				final Row row = worksheet.createRow(rowNumber);
				Cell cell = row.createCell(0);
				cell.setCellValue(iteration.getKey());

				final Fitness fitness = iteration.getValue();

				// sc1
				cell = row.createCell(1);
				cell.setCellStyle(currStyle);
				cell.setCellValue(fitness.getFitnessesPerConstraintWithWeightingsApplied().get("SC1"));

				// sc2
				cell = row.createCell(2);
				cell.setCellStyle(currStyle);
				cell.setCellValue(fitness.getFitnessesPerConstraintWithWeightingsApplied().get("SC2"));

				// average
				cell = row.createCell(3);
				cell.setCellStyle(currStyle);
				cell.setCellValue(fitness.getOverallFitness());

				rowNumber++;
				if (rowNumber > 65532) {
					break;
				}
			}
			final Fitness bestFitness = new Fitness(this.ctx, this.currentSolution);
			final Map<String, Double> bestSolutionFitnessAndConstraints = bestFitness.getFitnessesPerConstraintWithWeightingsApplied();

			// best one
			Row row = worksheet.createRow(rowNumber);
			Cell cell = row.createCell(0);
			cell.setCellStyle(currStyle);
			cell.setCellValue("Best:");

			cell = row.createCell(1);
			cell.setCellStyle(currStyle);
			cell.setCellValue(bestSolutionFitnessAndConstraints.get("SC1"));

			cell = row.createCell(2);
			cell.setCellStyle(currStyle);
			cell.setCellValue(bestFitness.getOverallFitness());

			rowNumber++;
			if (rowNumber > 65532) {
				row = worksheet.createRow(rowNumber);
				cell = row.createCell(0);
				cell.setCellValue("Max number of rows reached. Some results may be omitted.");
			}

			for (int i = 1; i < 15; i++) {
				worksheet.autoSizeColumn(i);
			}
			// Save the workbook in .xls file
			workbook.write(outputStream);
			workbook.close();
			outputStream.flush();
			outputStream.close();
		}
		catch (final Exception e) {
			this.printErr(e);
		}
	}

	/**
	 * Prints the err.
	 *
	 * @param e
	 *            the e
	 */
	private void printErr(final Exception e) {
		System.err.println(e);
		final StackTraceElement[] elements = e.getStackTrace();
		for (int iterator = 1; iterator <= elements.length; iterator++) {
			System.err.println("Class Name:" + elements[iterator - 1].getClassName() + " Method Name:" + elements[iterator - 1].getMethodName()
					+ " Line Number:" + elements[iterator - 1].getLineNumber());
		}
	}
}
