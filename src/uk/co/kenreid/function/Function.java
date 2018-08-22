/*
 * E: Ken@kenreid.co.uk
 *
 */
package uk.co.kenreid.function;

import java.util.Random;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * The Class Function. Should not hold any data. Function exists as just that, a
 * small collection of useful methods that are used by a variety of classes in
 * order to encourage code reuse.
 */
public class Function {

	/** The random. */
	private final Random random;

	/**
	 * Instantiates a new function.
	 */
	public Function() {
		this.random = new Random();
	}

	/**
	 * Gets the random.
	 *
	 * @return the random
	 */
	public Random getRandom() {
		return this.random;
	}

	/**
	 * Make column bold.
	 *
	 * @param wb
	 *            the wb
	 * @param columnNumber
	 *            the column number
	 * @param worksheet
	 *            the worksheet
	 * @param isPercentage
	 *            the is percentage
	 */
	public void makeColumnBold(final Workbook wb, final int columnNumber, final Sheet worksheet, final boolean isPercentage) {
		final CellStyle style = wb.createCellStyle();// Create style
		final Font font = wb.createFont();// Create font
		font.setBold(true);// Make font bold
		style.setFont(font);// set it to bold

		if (isPercentage) {
			style.setDataFormat(wb.createDataFormat().getFormat("0.000%"));
		}

		final int numberOfRows = worksheet.getPhysicalNumberOfRows();

		for (int i = 0; i < numberOfRows; i++) {// For each cell in the
			final Row row = worksheet.getRow(i);
			final Cell cell = row.getCell(columnNumber);
			if (!(cell == null)) {
				cell.setCellStyle(style);// Set the style
			}
		}
	}

	/**
	 * Make row bold.
	 *
	 * @param wb
	 *            the wb
	 * @param row
	 *            the row
	 * @param isPercentage
	 *            the is percentage
	 */
	public void makeRowBold(final Workbook wb, final Row row, final boolean isPercentage) {
		final CellStyle style = wb.createCellStyle();// Create style
		final Font font = wb.createFont();// Create font
		font.setBold(true);// Make font bold
		style.setFont(font);// set it to bold
		if (isPercentage) {
			style.setDataFormat(wb.createDataFormat().getFormat("0.000%"));
		}

		for (int i = 0; i < row.getLastCellNum(); i++) {// For each cell in the
			// row
			if (!(row.getCell(i) == null)) {
				row.getCell(i).setCellStyle(style);// Set the style
			}
		}
	}

	/**
	 * Sum array of doubles.
	 *
	 * @param doubles
	 *            the doubles
	 * @return the double
	 */
	public double sumArrayOfDoubles(final double[] doubles) {
		double ans = 0;
		for (final double d : doubles) {
			ans += d;
		}
		return ans;
	}

	/**
	 * Sum array of ints.
	 *
	 * @param ints
	 *            the ints
	 * @return the int
	 */
	public int sumArrayOfInts(final int[] ints) {
		int ans = 0;
		for (final int i : ints) {
			ans += i;
		}
		return ans;
	}

	/**
	 * Sum array of ints to integer.
	 *
	 * @param ints
	 *            the ints
	 * @return the integer
	 */
	public Integer sumArrayOfIntsToInteger(final int[] ints) {
		int ans = 0;
		for (final int i : ints) {
			ans += i;
		}
		return ans;
	}
}
