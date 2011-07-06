package it.unipd.math.plg.ui.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author Andrea Burattin
 */
public class DateUtils {
	
	public static final String DATE_FORMAT_NOW = "HH:mm:ss.SSS";

	public static String now() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(cal.getTime());
	}
}
