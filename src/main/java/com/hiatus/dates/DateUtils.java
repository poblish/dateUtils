package com.hiatus.dates;

import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by AGR on 2nd September 2001
 * 
 * @author andrewregan
 *
 */
public class DateUtils implements DateConstants
{
	private DateUtils() {}

	private static NumberFormat	SECONDS_FORMAT;

	/*******************************************************************************
	3 May 2002
	 *******************************************************************************/
	static
	{
		SECONDS_FORMAT = NumberFormat.getNumberInstance( Locale.UK );
		SECONDS_FORMAT.setMaximumFractionDigits(3);
	}

	/*******************************************************************************
	19 June 2001
	 *******************************************************************************/
	public static long checkDateValidity( int inDay, int inMonth, int inYear)
	{
		long	theResult = DATE_OK;

		if ( inYear < 1900 || inYear > 2010)
		{
			// FIXME Logger.getLogger("Main").warn("Bad year specified: " + inYear);

			theResult |= BAD_DATE_YEAR;	// 9 August 2001 - not worth checking Day or Month in this case
		}
		else
		{
			Calendar	theCalendar = Calendar.getInstance();

			theCalendar.set( inYear, inMonth, inDay);
			theCalendar.getTime();

			////////////////////////////////////////  We use this test to catch things like the 31st February!

			boolean		dateWasWrong = (( theCalendar.get(Calendar.YEAR) != inYear) ||
					( theCalendar.get(Calendar.MONTH) != inMonth) ||
					( theCalendar.get(Calendar.DAY_OF_MONTH) != inDay));

			if ( dateWasWrong ||
					inDay < theCalendar.getMinimum(Calendar.DAY_OF_MONTH) ||		// well, 1
					inDay > theCalendar.getMaximum(Calendar.DAY_OF_MONTH))		// well, 31
			{
				theResult |= BAD_DATE_DAY;
			}

			if ( inMonth < theCalendar.getMinimum(Calendar.MONTH) ||		// well, 0
					inMonth > theCalendar.getMaximum(Calendar.MONTH))
			{
				theResult |= BAD_DATE_MONTH;
			}
		}

		return theResult;
	}

	/*******************************************************************************
	 *******************************************************************************/
	public static java.sql.Date DMYtoSqlDate( int inD, int inM, int inY)
	{
		Calendar	theCalendar = Calendar.getInstance();
		theCalendar.set( inY, inM, inD);

		Date		theDate = theCalendar.getTime();

		return new java.sql.Date( theDate.getTime() );
	}

	/*******************************************************************************
	 *******************************************************************************/
	public static boolean EqualDates( java.sql.Date inA, java.sql.Date inB)
	{
		Calendar	theCalendarA = Calendar.getInstance();
		Calendar	theCalendarB = Calendar.getInstance();

		theCalendarA.setTime(inA);
		theCalendarB.setTime(inB);

		return (( theCalendarA.get(Calendar.YEAR) == theCalendarB.get(Calendar.YEAR)) &&
				( theCalendarA.get(Calendar.MONTH) == theCalendarB.get(Calendar.MONTH)) &&
				( theCalendarA.get(Calendar.DAY_OF_MONTH) == theCalendarB.get(Calendar.DAY_OF_MONTH)));
	}

	/*******************************************************************************
	24 March 2002
	 *******************************************************************************/
	public static Timestamp getCurrentTimestamp()
	{
		return new Timestamp( System.currentTimeMillis() );
	}

	/*******************************************************************************
	 *******************************************************************************/
	public static long GetDatesDifference_Days( java.sql.Date inA, long inB_Msecs)
	{
		return GetDatesDifference_Days( inA.getTime(), inB_Msecs);
	}

	/*******************************************************************************
	 *******************************************************************************/
	public static double GetDatesDifference_Years( long inA_Msecs, long inB_Msecs)
	{
		// double	theResult;

		//	if (System.getProperty("java.version").startsWith("1.4"))	// JDK 1.4
		{
			GregorianCalendar	theCalendar_A = ULocale2.getGregorianCalendar( Locale.ENGLISH );
			GregorianCalendar	theCalendar_B = ULocale2.getGregorianCalendar( Locale.ENGLISH );

			theCalendar_A.setTime( new Date(inA_Msecs) );	// yuk
			theCalendar_B.setTime( new Date(inB_Msecs) );	// yuk

			/* theResult = */ return getCalendarDifference_Years( theCalendar_A, theCalendar_B);
		}
		/*		else
	{
		com.ibm.icu.util.Calendar	theCalendar_A = ULocale.getCalendar( Locale.ENGLISH );
		com.ibm.icu.util.Calendar	theCalendar_B = ULocale.getCalendar( Locale.ENGLISH );

		theCalendar_A.setTimeInMillis(inA_Msecs);
		theCalendar_B.setTimeInMillis(inB_Msecs);

		return getCalendarDifference_Years( theCalendar_A, theCalendar_B);
	}
		 */
		//	log_info("(2) new val = " + theResult + ", old was " + ( GetDatesDifference_Days( inA_Msecs, inB_Msecs) / DAYS_IN_YEAR));

		// return theResult;
	}

	/*******************************************************************************
	 *******************************************************************************/
	public static double getCurrentAge_Years( java.util.GregorianCalendar inBirthDate)
	{
		java.util.GregorianCalendar	theCurrDate = ULocale2.getGregorianCalendar( Locale.ENGLISH );

		return getCalendarDifference_Years( inBirthDate, theCurrDate);
	}

	/*******************************************************************************
	 ******************************************************************************
public static double getCurrentAge_Years( com.ibm.icu.util.Calendar inBirthDate)
{
	com.ibm.icu.util.Calendar	theCurrDate = ULocale.getCalendar( Locale.ENGLISH );

	return getCalendarDifference_Years( inBirthDate, theCurrDate);
}*/

	/*******************************************************************************
	 *******************************************************************************/
	public static double getCalendarDifference_Years( java.util.GregorianCalendar inCal_A, java.util.GregorianCalendar inCal_B)
	{
		int	theDOY_A  = inCal_A.get(GregorianCalendar.DAY_OF_YEAR);
		int	theDOY_B  = inCal_B.get(GregorianCalendar.DAY_OF_YEAR);
		int	theYear_A = inCal_A.get(GregorianCalendar.YEAR);
		int	theYear_B = inCal_B.get(GregorianCalendar.YEAR);

		if ((( inCal_A.get(GregorianCalendar.DAY_OF_MONTH) == inCal_B.get(GregorianCalendar.DAY_OF_MONTH)) &&
				( inCal_A.get(GregorianCalendar.MONTH) == inCal_B.get(GregorianCalendar.MONTH))) ||
				( theDOY_A == theDOY_B))
		{
			return (double) Math.abs( theYear_A - theYear_B);
		}
		else
		{
			double	theFractBit = ((double) Math.abs( theDOY_A - theDOY_B)) / (double) inCal_A.getActualMaximum(GregorianCalendar.DAY_OF_YEAR);

			if ( theYear_A == theYear_B)
			{
				return theFractBit;
			}

			double	theYearBit = (double) Math.abs( theYear_A - theYear_B);

			if ((( theYear_A > theYear_B) && ( theDOY_A > theDOY_B)) ||
					(( theYear_A < theYear_B) && ( theDOY_A < theDOY_B)))
			{
				return theYearBit + theFractBit;
			}

			return theYearBit - theFractBit;
		}
	}

	/*******************************************************************************
	 ******************************************************************************
public static double getCalendarDifference_Years( com.ibm.icu.util.Calendar inCal_A, com.ibm.icu.util.Calendar inCal_B)
{
	int	theDOY_A  = inCal_A.get(Calendar.DAY_OF_YEAR);
	int	theDOY_B  = inCal_B.get(Calendar.DAY_OF_YEAR);
	int	theYear_A = inCal_A.get(Calendar.YEAR);
	int	theYear_B = inCal_B.get(Calendar.YEAR);

	if ((( inCal_A.get(Calendar.DAY_OF_MONTH) == inCal_B.get(Calendar.DAY_OF_MONTH)) &&
	     ( inCal_A.get(Calendar.MONTH) == inCal_B.get(Calendar.MONTH))) ||
	     ( theDOY_A == theDOY_B))
	{
		return (double) Math.abs( theYear_A - theYear_B);
	}
	else
	{
		double	theFractBit = ((double) Math.abs( theDOY_A - theDOY_B)) / (double) inCal_A.getActualMaximum(Calendar.DAY_OF_YEAR);

		if ( theYear_A == theYear_B)
		{
			return theFractBit;
		}

		double	theYearBit = (double) Math.abs( theYear_A - theYear_B);

		if ((( theYear_A > theYear_B) && ( theDOY_A > theDOY_B)) ||
		    (( theYear_A < theYear_B) && ( theDOY_A < theDOY_B)))
		{
			return theYearBit + theFractBit;
		}

		return theYearBit - theFractBit;
	}
}*/

	/*******************************************************************************
	 *******************************************************************************/
	public static int getAgeIfBirthdayToday( Locale inLocale, java.sql.Date inDate)
	{
		Calendar	theTodaysCalendar = Calendar.getInstance(inLocale);
		Calendar	theBirthCalendar = (Calendar) theTodaysCalendar.clone();

		theBirthCalendar.setTime(inDate);

		return _getAgeIfBirthdayToday( theBirthCalendar, theTodaysCalendar);
	}

	/*******************************************************************************
	 *******************************************************************************/
	private static int _getAgeIfBirthdayToday( Calendar inBirthCalendar, Calendar inTodaysCalendar)
	{
		if (( inTodaysCalendar.get(Calendar.DAY_OF_MONTH) == inBirthCalendar.get(Calendar.DAY_OF_MONTH)) &&
				( inTodaysCalendar.get(Calendar.MONTH) == inBirthCalendar.get(Calendar.MONTH)))
		{
			return ( inTodaysCalendar.get(Calendar.YEAR) - inBirthCalendar.get(Calendar.YEAR));
		}
		else	return -1;
	}

	/*******************************************************************************
	1 August 2002
	 *******************************************************************************/
	public static int getActualAgeInYears( Locale inLocale, java.sql.Date inDate)
	{
		Calendar	theTodaysCalendar = Calendar.getInstance(inLocale);
		Calendar	theBirthCalendar = (Calendar) theTodaysCalendar.clone();

		theBirthCalendar.setTime(inDate);

		//////////////////////////////////////

		int	theExactYears = _getAgeIfBirthdayToday( theBirthCalendar, theTodaysCalendar);

		if ( theExactYears >= 0)	// a birthday. return that age
		{
			// debug("GAAIY(a): \"" + inDate + "\" ==> " + theExactYears);

			return theExactYears;
		}
		else
		{
			if (( theTodaysCalendar.get(Calendar.MONTH) > theBirthCalendar.get(Calendar.MONTH)) ||
					(( theTodaysCalendar.get(Calendar.MONTH) == theBirthCalendar.get(Calendar.MONTH)) &&
							( theTodaysCalendar.get(Calendar.DAY_OF_MONTH) >= theBirthCalendar.get(Calendar.DAY_OF_MONTH))))
			{
				// debug("GAAIY(b): \"" + inDate + "\" ==> " + ( theTodaysCalendar.get(Calendar.YEAR) - theBirthCalendar.get(Calendar.YEAR)));

				return ( theTodaysCalendar.get(Calendar.YEAR) - theBirthCalendar.get(Calendar.YEAR));
			}
			else
			{
				// debug("GAAIY(c): \"" + inDate + "\" ==> " + ( theTodaysCalendar.get(Calendar.YEAR) - theBirthCalendar.get(Calendar.YEAR) - 1));

				return ( theTodaysCalendar.get(Calendar.YEAR) - theBirthCalendar.get(Calendar.YEAR) - 1);
			}
		}
	}

	/*******************************************************************************
	 *******************************************************************************/
	public static long getDaysSinceYearZero( Date inDate)
	{
		// 9 July 2002. Problem was that due to the long integer arithmetic, pre-epoch
		// (1970) values were being rounded the wrong way: -800.8 -> -800, causing
		// the calculation result to be potentially one too high.
		// Introduced fp arithmetic. How does this affect >= 1970 dates, if at all ???

		double	theNumDays = ((double) inDate.getTime() / (double) MSECS_IN_DAY);

		// debug("//// theNumDays.1 = " + theNumDays);
		theNumDays += 719528;
		// debug("//// theNumDays.2 = " + theNumDays + " !!!");

		return (long) theNumDays;
	}

	/*******************************************************************************
	 *******************************************************************************/
	public static long getDaysSinceYearZero()
	{
		// 9 July 2002. Fine - current year definitely after 1970 epoch

		return ( System.currentTimeMillis() / (long) MSECS_IN_DAY) + 719528;
	}

	/*******************************************************************************
	 *******************************************************************************/
	public static String getMonthName( int inIndex, Locale inLocale)
	{
		DateFormatSymbols	theSymbols = new DateFormatSymbols(inLocale);

		return theSymbols.getMonths()[inIndex];
	}

	public static StringBuilder getFormattedTimeDiff( final long inDiffMSecs) {
		return getFormattedTimeDiff(inDiffMSecs, true, true);
	}

	public static StringBuilder getFormattedTimeNanosDiff( final long inDiffNanos) {
		return getFormattedTimeNanosDiff(inDiffNanos, true, true);
	}

	public static StringBuilder getFormattedTimeNanosDiff( final long inDiffNanos, final boolean inUseSeconds, final boolean inUseMSecs) {
		if (inDiffNanos < 1000L) {
			return new StringBuilder(50).append(inDiffNanos).append(" nanos");
		}

		final long theMicros = inDiffNanos / 1000L;

		if (theMicros < 1000L) {
			return new StringBuilder(50).append(theMicros).append(" micros");
		}

		return getFormattedTimeDiff(theMicros / 1000L, inUseSeconds, inUseMSecs);
	}

	/*******************************************************************************
	1 May 2002
	 *******************************************************************************/
	public static StringBuilder getFormattedTimeDiff( final long inDiffMSecs, final boolean inUseSeconds, final boolean inUseMSecs)
	{
		final StringBuilder theBuf = new StringBuilder(200);

		if (inDiffMSecs >= 1000L)
		{
			double theSecs = inDiffMSecs / 1000.0D;

			if (theSecs >= 60.0D)
			{
				long theMins = (long) (theSecs / 60L);

				theSecs -= theMins * 60L;

				if (theMins >= 60L)
				{
					long theHrs = theMins / 60L;

					theMins %= 60L;

					if (theHrs >= 24L)
					{
						final long theDays = theHrs / 24L;

						if (theDays == 1L)
						{
							theBuf.append("1 day");
						}
						else {
							theBuf.append(theDays).append(" days");
						}

						theHrs %= 24L;
					}

					if ((theHrs >= 1L) && (theBuf.length() > 0))
					{
						theBuf.append(", ");
					}

					if (theHrs == 1L)
					{
						theBuf.append("1 hour");
					}
					else if (theHrs > 1L)
					{
						theBuf.append(theHrs).append(" hrs");
					}

				}

				if ((theMins >= 1L) && (theBuf.length() > 0))
				{
					theBuf.append(", ");
				}

				if (theMins == 1L)
				{
					theBuf.append("1 minute");
				}
				else if (theMins > 1L)
				{
					theBuf.append(theMins).append(" mins");
				}

			}

			if ((inUseSeconds) && (theSecs > 0.0D))
			{
				if (theBuf.length() > 0)
				{
					theBuf.append(", ");
				}

				if (inUseMSecs)
				{
					theBuf.append( SECONDS_FORMAT.format(theSecs)).append(" secs");
				}
				else {
					theBuf.append(Integer.toString((int)theSecs)).append(" secs");
				}
			}

		}
		else if (inUseMSecs)
		{
			theBuf.append(inDiffMSecs).append(" msecs");
		}

		return theBuf;
	}

	/*******************************************************************************
	 *******************************************************************************/
	public static long GetDatesDifference_ExactDays( java.util.Date inA, java.util.Date inB)
	{
		Calendar	theCal_A = getCalendarJustFromDate(inA);
		Calendar	theCal_B = getCalendarJustFromDate(inB);

		//	Logger.getLogger("Main").info("GetDatesDifference_ExactDays: was... " + inA + ", and " + inB);
		//	Logger.getLogger("Main").info("GetDatesDifference_ExactDays:  is... " + theCal_A.getTime() + ", and " + theCal_B.getTime());

		return GetDatesDifference_Days( theCal_A.getTime(), theCal_B.getTime());
	}

	/*******************************************************************************
	 *******************************************************************************/
	public static long GetDatesDifference_Days( java.util.Date inA, java.util.Date inB)
	{
		return GetDatesDifference_Days( inA.getTime(), inB.getTime());
	}

	/*******************************************************************************
	 *******************************************************************************/
	public static long GetDatesDifference_Days( long inA_Msecs, long inB_Msecs)
	{
		long	theDifferenceinMsecs = GetDatesDifference_Msecs( inA_Msecs, inB_Msecs);

		//	Logger.getLogger("Main").info("theDifferenceinMsecs... " + theDifferenceinMsecs);

		return ( theDifferenceinMsecs / MSECS_IN_DAY);
	}

	/*******************************************************************************
	 *******************************************************************************/
	public static long GetDatesDifference_Msecs( long inA_Msecs, long inB_Msecs)
	{
		if ( inA_Msecs > inB_Msecs)
		{
			return ( inA_Msecs - inB_Msecs);
		}

		return ( inB_Msecs - inA_Msecs);
	}

	/*******************************************************************************
	4 July 2002
	 *******************************************************************************/
	public static Calendar getCalendarJustFromDate( java.util.Date inDate)
	{
		Calendar	theCal = Calendar.getInstance( TimeZone.getTimeZone("UTC") );

		//Logger.getLogger("Main").info(">>> getCalendarJustFromDate:: WAS " + theCal);

		theCal.setTime(inDate);
		theCal.set( Calendar.HOUR, 0);
		theCal.set( Calendar.MINUTE, 0);
		theCal.set( Calendar.SECOND, 0);

		//	if ( theCal.get(Calendar.MILLISECOND) != 0)
		{
			// Logger.getLogger("Main").info(">>> cleared MILLISECOND from " + theCal.get(Calendar.MILLISECOND));

			theCal.set( Calendar.MILLISECOND, 0);
		}

		//Logger.getLogger("Main").info(">>> getCalendarJustFromDate:: NOW " + theCal);

		return theCal;
	}
}