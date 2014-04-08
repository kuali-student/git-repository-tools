/**
 * 
 */
package org.kuali.student.git.utils;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.tz.DateTimeZoneBuilder;

/**
 * @author ocleirig
 *
 */
public final class GitImporterDateUtils {

	/**
	 * 
	 */
	private GitImporterDateUtils() {
	}

	private static DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
	
	public static Date convertDateString(String svnDumpDateFormattedString) {
		
		LocalDateTime ldt = formatter.parseLocalDateTime(svnDumpDateFormattedString);

		DateTime dt = ldt.toDateTime(DateTimeZone.UTC).toDateTime(DateTimeZone.getDefault());
		
		Date d = dt.toDate();
		
		return d;
	}
	
	public static String getTimeZoneShortFormName(Date d) {

		TimeZone tz = extractTimeZone(d);
		
		String tzDisplay = tz.getDisplayName(tz.inDaylightTime(d), TimeZone.SHORT, Locale.US);
		
		return tzDisplay;
	}
	public static TimeZone extractTimeZone (Date d) {
		
		DateTime ldt = new DateTime(d);
		
		DateTimeZone dtz = ldt.getZone();
		
		TimeZone tz = dtz.toTimeZone();
		
		return tz;
		
	}
}
