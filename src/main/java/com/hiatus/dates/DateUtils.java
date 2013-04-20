package com.hiatus.dates;

import static java.util.Calendar.*;

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
public final class DateUtils implements DateConstants
{
	private DateUtils() {
	}

	private static NumberFormat SECONDS_FORMAT;

	/*******************************************************************************
	 * 3 May 2002
	 *******************************************************************************/
	static
	{
		SECONDS_FORMAT = NumberFormat.getNumberInstance(Locale.UK);
		SECONDS_FORMAT.setMaximumFractionDigits(3);
	}

	/*******************************************************************************
	 * 19 June 2001
	 *******************************************************************************/
	public static long checkDateValidity( final int inDay, final int inMonth, final int inYear)
	{
		long theResult = DATE_OK;

		if ((inYear < 1900) || (inYear > 2010))
		{
			// FIXME Logger.getLogger("Main").warn("Bad year specified: " + inYear);

			theResult |= BAD_DATE_YEAR; // 9 August 2001 - not worth checking Day or Month in this case
		}
		else
		{
			final Calendar theCalendar = Calendar.getInstance();

			theCalendar.set(inYear, inMonth, inDay);
			theCalendar.getTime();

			// ////////////////////////////////////// We use this test to catch things like the 31st February!

			final boolean dateWasWrong = ((theCalendar.get(YEAR) != inYear) ||
							(theCalendar.get(MONTH) != inMonth) ||
							(theCalendar.get(DAY_OF_MONTH) != inDay));

			if (dateWasWrong ||
							(inDay < theCalendar.getMinimum(DAY_OF_MONTH)) || // well, 1
							(inDay > theCalendar.getMaximum(DAY_OF_MONTH))) // well, 31
			{
				theResult |= BAD_DATE_DAY;
			}

			if ((inMonth < theCalendar.getMinimum(MONTH)) || // well, 0
							(inMonth > theCalendar.getMaximum(MONTH)))
			{
				theResult |= BAD_DATE_MONTH;
			}
		}

		return theResult;
	}

	/*******************************************************************************
	 *******************************************************************************/
	public static java.sql.Date convertDmyToSqlDate( final int inD, final int inM, final int inY)
	{
		final Calendar theCalendar = Calendar.getInstance();
		theCalendar.set(inY, inM, inD);

		final Date theDate = theCalendar.getTime();

		return new java.sql.Date(theDate.getTime());
	}

	/*******************************************************************************
	 *******************************************************************************/
	public static boolean equalDates( final java.sql.Date inA, final java.sql.Date inB)
	{
		final Calendar theCalendarA = Calendar.getInstance();
		final Calendar theCalendarB = Calendar.getInstance();

		theCalendarA.setTime(inA);
		theCalendarB.setTime(inB);

		return ((theCalendarA.get(YEAR) == theCalendarB.get(YEAR)) && (theCalendarA.get(MONTH) == theCalendarB.get(MONTH)) && (theCalendarA.get(DAY_OF_MONTH) == theCalendarB.get(DAY_OF_MONTH)));
	}

	/*******************************************************************************
	 * 24 March 2002
	 *******************************************************************************/
	public static Timestamp getCurrentTimestamp()
	{
		return new Timestamp(System.currentTimeMillis());
	}

	/*******************************************************************************
	 *******************************************************************************/
	public static long getDatesDifferenceDays( final java.sql.Date inA, final long inB_Msecs)
	{
		return getDatesDifferenceDays(inA.getTime(), inB_Msecs);
	}

	/*******************************************************************************
	 *******************************************************************************/
	public static double getDatesDifferenceYears( final long inA_Msecs, final long inB_Msecs)
	{
		final GregorianCalendar calA = ULocale2.getGregorianCalendar(Locale.ENGLISH);
		final GregorianCalendar calB = ULocale2.getGregorianCalendar(Locale.ENGLISH);

		calA.setTimeInMillis(inA_Msecs);
		calB.setTimeInMillis(inB_Msecs);

		return getCalendarDifference_Years(calA, calB);
	}

	/*******************************************************************************
	 *******************************************************************************/
	public static double getCurrentAge_Years( final java.util.GregorianCalendar inBirthDate)
	{
		final java.util.GregorianCalendar theCurrDate = ULocale2.getGregorianCalendar(Locale.ENGLISH);

		return getCalendarDifference_Years(inBirthDate, theCurrDate);
	}

	/*******************************************************************************
	 *******************************************************************************/
	public static double getCalendarDifference_Years( final java.util.GregorianCalendar inCal_A, final java.util.GregorianCalendar inCal_B)
	{
		final int doyA = inCal_A.get(DAY_OF_YEAR);
		final int doyB = inCal_B.get(DAY_OF_YEAR);
		final int theYear_A = inCal_A.get(YEAR);
		final int theYear_B = inCal_B.get(YEAR);

		if (((inCal_A.get(DAY_OF_MONTH) == inCal_B.get(DAY_OF_MONTH)) && (inCal_A.get(MONTH) == inCal_B.get(MONTH))) || (doyA == doyB))
		{
			return Math.abs(theYear_A - theYear_B);
		}
		else
		{
			final double theFractBit = ((double) Math.abs(doyA - doyB)) / (double) inCal_A.getActualMaximum(DAY_OF_YEAR);

			if (theYear_A == theYear_B)
			{
				return theFractBit;
			}

			final double theYearBit = Math.abs(theYear_A - theYear_B);

			if (((theYear_A > theYear_B) && (doyA > doyB)) || ((theYear_A < theYear_B) && (doyA < doyB)))
			{
				return theYearBit + theFractBit;
			}

			return theYearBit - theFractBit;
		}
	}

	/*******************************************************************************
	 *******************************************************************************/
	public static int getAgeIfBirthdayToday( final Locale inLocale, final java.sql.Date inDate)
	{
		final Calendar theTodaysCalendar = Calendar.getInstance(inLocale);
		final Calendar theBirthCalendar = (Calendar) theTodaysCalendar.clone();

		theBirthCalendar.setTime(inDate);

		return getAgeIfBirthdayToday(theBirthCalendar, theTodaysCalendar);
	}

	/*******************************************************************************
	 *******************************************************************************/
	public static int getAgeIfBirthdayToday( final Calendar inBirthCalendar, final Calendar inTodaysCalendar)
	{
		if ((inTodaysCalendar.get(DAY_OF_MONTH) == inBirthCalendar.get(DAY_OF_MONTH)) && (inTodaysCalendar.get(MONTH) == inBirthCalendar.get(MONTH)))
		{
			return (inTodaysCalendar.get(YEAR) - inBirthCalendar.get(YEAR));
		} else {
			return -1;
		}
	}

	/*******************************************************************************
	 * 1 August 2002
	 *******************************************************************************/
	public static int getActualAgeInYears( final Locale inLocale, final java.sql.Date inDate)
	{
		final Calendar theTodaysCalendar = Calendar.getInstance(inLocale);
		final Calendar theBirthCalendar = (Calendar) theTodaysCalendar.clone();

		theBirthCalendar.setTime(inDate);

		// ////////////////////////////////////

		final int theExactYears = getAgeIfBirthdayToday(theBirthCalendar, theTodaysCalendar);

		if (theExactYears >= 0) // a birthday. return that age
		{
			return theExactYears;
		}
		else
		{
			if ((theTodaysCalendar.get(MONTH) > theBirthCalendar.get(MONTH)) ||
							((theTodaysCalendar.get(MONTH) == theBirthCalendar.get(MONTH)) &&
							(theTodaysCalendar.get(DAY_OF_MONTH) >= theBirthCalendar.get(DAY_OF_MONTH))))
			{
				return (theTodaysCalendar.get(YEAR) - theBirthCalendar.get(YEAR));
			}
			else
			{
				return (theTodaysCalendar.get(YEAR) - theBirthCalendar.get(YEAR) - 1);
			}
		}
	}

	/*******************************************************************************
	 *******************************************************************************/
	public static long getDaysSinceYearZero( final Date inDate)
	{
		// 9 July 2002. Problem was that due to the long integer arithmetic, pre-epoch
		// (1970) values were being rounded the wrong way: -800.8 -> -800, causing
		// the calculation result to be potentially one too high.
		// Introduced fp arithmetic. How does this affect >= 1970 dates, if at all ???

		double theNumDays = ((double) inDate.getTime() / (double) MSECS_IN_DAY);

		theNumDays += 719528;

		return (long) theNumDays;
	}

	/*******************************************************************************
	 *******************************************************************************/
	public static long getDaysSinceYearZero()
	{
		// 9 July 2002. Fine - current year definitely after 1970 epoch

		return (System.currentTimeMillis() / MSECS_IN_DAY) + 719528;
	}

	/*******************************************************************************
	 *******************************************************************************/
	public static String getMonthName( final int inIndex, final Locale inLocale)
	{
		final DateFormatSymbols theSymbols = new DateFormatSymbols(inLocale);

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
	 * 1 May 2002
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
					theBuf.append(SECONDS_FORMAT.format(theSecs)).append(" secs");
				}
				else {
					theBuf.append(Integer.toString((int) theSecs)).append(" secs");
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
	public static long getDatesDifference_ExactDays( final java.util.Date inA, final java.util.Date inB)
	{
		final Calendar theCal_A = getCalendarJustFromDate(inA);
		final Calendar theCal_B = getCalendarJustFromDate(inB);

		return getDatesDifferenceDays(theCal_A.getTime(), theCal_B.getTime());
	}

	/*******************************************************************************
	 *******************************************************************************/
	public static long getDatesDifferenceDays( final java.util.Date inA, final java.util.Date inB)
	{
		return getDatesDifferenceDays(inA.getTime(), inB.getTime());
	}

	/*******************************************************************************
	 *******************************************************************************/
	public static long getDatesDifferenceDays( final long inA_Msecs, final long inB_Msecs)
	{
		final long theDifferenceinMsecs = getDatesDifferenceMsecs(inA_Msecs, inB_Msecs);

		return (theDifferenceinMsecs / MSECS_IN_DAY);
	}

	/*******************************************************************************
	 *******************************************************************************/
	public static long getDatesDifferenceMsecs( final long inA_Msecs, final long inB_Msecs)
	{
		if (inA_Msecs > inB_Msecs)
		{
			return (inA_Msecs - inB_Msecs);
		}

		return (inB_Msecs - inA_Msecs);
	}

	/*******************************************************************************
	 * 4 July 2002
	 *******************************************************************************/
	public static Calendar getCalendarJustFromDate( final java.util.Date inDate)
	{
		final Calendar theCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

		theCal.setTime(inDate);
		theCal.set(HOUR, 0);
		theCal.set(MINUTE, 0);
		theCal.set(SECOND, 0);
		theCal.set(MILLISECOND, 0);

		return theCal;
	}
}