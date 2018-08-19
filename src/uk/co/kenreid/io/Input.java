/*
 * Copyright: Ken Reid
 * E: Ken@kenreid.co.uk
 *
 */
package uk.co.kenreid.io;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import uk.co.kenreid.dataobjects.TestParameter;

/**
 * The Class Input. Reads the parameter file, however can be used for reading
 * other data files. E.g. if you were reading a set of employee information from
 * XLSX, JSON etc it's useful to have all input related code in a single class /
 * package / collection.
 */
public class Input {

	/**
	 * First digit.
	 *
	 * @param n
	 *            the n
	 * @return the int
	 */
	public static int firstDigit(int n) {
		while (n < -9 || 9 < n) {
			n /= 10;
		}
		return Math.abs(n);
	}

	/** The path. */
	private final String path;

	/**
	 * Instantiates a new input.
	 *
	 * @param path
	 *            the path
	 */
	public Input(final String path) {
		this.path = path;
	}

	/**
	 * Read cell.
	 *
	 * @param cell
	 *            the cell
	 * @return the string
	 */
	/*
	 * The "I hate XSSF's inability to cope with different data types" lazy method.
	 */
	public String readCell(final XSSFCell cell) {
		if (cell == null) {
			return null;
		}
		String s = "";
		try {
			s = cell.getStringCellValue();
		}
		catch (final Exception e) {
			s = "" + cell.getNumericCellValue();
		}
		return s;
	}

	/**
	 * Read parameters.
	 *
	 * @return the list
	 */
	public List<TestParameter> readParameters() {
		final List<TestParameter> tests = new ArrayList<>();
		try {
			final File file = new File(this.path + "/params.xlsx");
			final FileInputStream fis = new FileInputStream(file);
			final XSSFWorkbook wb = new XSSFWorkbook(fis);
			final XSSFSheet sheet = wb.getSheetAt(0);

			final int numberOfRows = sheet.getPhysicalNumberOfRows();
			for (int rowNum = 1; rowNum < numberOfRows; rowNum++) {
				final XSSFRow row = sheet.getRow(rowNum);

				/*
				 * Read in temp
				 */

				int cellNum = 0;
				XSSFCell cell = row.getCell(cellNum);
				final double temperature = cell.getNumericCellValue();
				cellNum++;

				/*
				 * Read in CR
				 */

				cell = row.getCell(cellNum);
				final double coolingRate = cell.getNumericCellValue();
				cellNum++;

				/*
				 * Read in SC weightings
				 */
				final HashMap<String, Double> weightings = new HashMap<>();
				cell = row.getCell(cellNum);
				weightings.put("SC1", cell.getNumericCellValue());
				cellNum++;
				cell = row.getCell(cellNum);
				weightings.put("SC2", cell.getNumericCellValue());
				cellNum++;

				/*
				 * Read in verbose option
				 */
				cell = row.getCell(cellNum);
				final boolean verbose = cell.getNumericCellValue() == 1.0;

				/*
				 * Read in problem.
				 *
				 * Normally this would be read in, but since this is an example, it's just
				 * hard-coded. Follow the above examples and create a params.xlsx file if you'd
				 * rather have it parameterised.
				 *
				 */

				final String problem = "1-7-3-4-6-7-3-2-1-4-7-6-Charlie-3-2-7-8-9-7-7-7-6-4-3-Tango-7-3-2-Victor-7-3-1-1-7-8-8-8-7-3-2-4-7-6-7-8-9-7-6-4-3-7-6";

				tests.add(new TestParameter(temperature, coolingRate, weightings, problem, verbose));
			}
			wb.close();
			fis.close();

		}
		catch (final Exception e) {
			System.err.println(e);
		}
		return tests;
	}
}
