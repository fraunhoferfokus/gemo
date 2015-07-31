package de.fhg.fokus.mdc.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Simple Valiation tests
 * 
 * @author dsc (Danilo Schmidt, danilo.schmidt@fokus.fraunhofer.de)
 * 
 */
public class SimpleValidator {

	/**
	 * simple validation test against NULL
	 * 
	 * @param value
	 *            to test
	 * @return true or false
	 */
	public static boolean notNull(Object value) {
		return value != null;
	}

	/**
	 * String conversion with nullable objects (an object that is null can't be
	 * converted into String without NullPointer)
	 * 
	 * @param nullableValue
	 * @return
	 */
	public static String toNullString(Object nullableValue) {
		return (nullableValue == null) ? "" : nullableValue.toString();
	}

	/**
	 * DateTime converter for Joda with null protection
	 * 
	 * @param date
	 * @return DateTime or Unix-Date 1970-01-01T01:00:00.000+01:00 if null
	 */
	public static DateTime dateToDateTime(Date date) {
		return (date == null) ? new DateTime(0) : new DateTime(date);
	}

	/**
	 * pattern for date time conversion
	 */
	public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public final static String DATE_TIME_READABLE_PATTERN = "dd.MM.yyyy HH:mm";
	SimpleDateFormat sdf = new SimpleDateFormat();

	/**
	 * Date to DateTime-String converter for Storage timestamp format
	 * 
	 * @param date
	 * @return String yyyy-MM-dd hh:mm:ss
	 */
	public static String dateToDateTimeString(Date date) {
		final DateTimeFormatter formatter = DateTimeFormat
				.forPattern(DATE_TIME_PATTERN);
		return formatter.print(dateToDateTime(date));
	}

	/**
	 * String to Date converter for Storage format
	 * 
	 * @see stringToDateTime(String dateString, Date fallbackDate) for an
	 *      alternative conversion with fall back value
	 * @param dateString
	 *            "yyyy-MM-dd HH:mm:ss"
	 * @return Date or null
	 */
	public static Date stringToDateTime(String dateString) {
		Date returnDate = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_PATTERN);
		try {
			returnDate = dateFormat.parse(dateString);
		} catch (ParseException e) {
			return null;
		}
		return returnDate;
	}

	/**
	 * String to Date converter for Storage format
	 * 
	 * @param dateString
	 *            "yyyy-MM-dd HH:mm:ss"
	 * @param fallbackDate
	 *            Date as default and fall back value
	 * @return
	 */
	public static Date stringToDateTime(String dateString, Date fallbackDate) {
		Date returnDate = fallbackDate;
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_PATTERN);
		// if the dateFormat.timezone is not set, parser assumes that it is in
		// the timezone as the system time.
		if (dateString == null)
			return returnDate;
		try {
			returnDate = dateFormat.parse(dateString);
		} catch (ParseException e) {
			return returnDate;
		} catch (Exception e) {
			return returnDate;
		}
		return returnDate;
	}

	/**
	 * String to Date converter for Storage format
	 * 
	 * @param dateString
	 *            "yyyy-MM-dd HH:mm:ss"
	 * @param fallbackDate
	 *            Date as default and fall back value
	 * @param timeZone
	 *            the time zone of the input dateString , by setting the
	 *            timezone you tell the parser that the string is in UTC
	 *            timezone and the parser parses and converts it to the system
	 *            timezone UTC+1
	 * @return
	 */
	public static Date stringToDateTime(String dateString, Date fallbackDate,
			TimeZone timeZone) {
		Date returnDate = fallbackDate;
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_PATTERN);
		dateFormat.setTimeZone(timeZone);
		// if the dateFormat.timezone is not set, parser assumes that it is in
		// the timezone as the system time.
		if (dateString == null)
			return returnDate;
		try {
			returnDate = dateFormat.parse(dateString);
		} catch (ParseException e) {
			return returnDate;
		} catch (Exception e) {
			return returnDate;
		}
		return returnDate;
	}
}
