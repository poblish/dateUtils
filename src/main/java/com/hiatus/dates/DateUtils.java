package com.hiatus.dates;

import static java.util.Calendar.*;

import java.sql.Timestamp;
import java.text.DateFormatSymbols;
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

	/*******************************************************************************
	 *******************************************************************************/
	public static long checkDateValidity( final int inDay, final int inMonth, final int inYear)
	{
		long theResult = DATE_OK;

		if (inYear < 1900 || inYear > 2010)
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

			final boolean dateWasWrong = theCalendar.get(YEAR) != inYear ||
							theCalendar.get(MONTH) != inMonth ||
							theCalendar.get(DAY_OF_MONTH) != inDay;

			if (dateWasWrong ||
							inDay < theCalendar.getMinimum(DAY_OF_MONTH) || // well, 1
							inDay > theCalendar.getMaximum(DAY_OF_MONTH)) // well, 31
			{
				theResult |= BAD_DATE_DAY;
			}

			if (inMonth < theCalendar.getMinimum(MONTH) || // well, 0
							inMonth > theCalendar.getMaximum(MONTH))
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

		return theCalendarA.get(YEAR) == theCalendarB.get(YEAR) && theCalendarA.get(MONTH) == theCalendarB.get(MONTH) && theCalendarA.get(DAY_OF_MONTH) == theCalendarB.get(DAY_OF_MONTH);
	}

	/*******************************************************************************
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
		final GregorianCalendar calA = LocaleUtils.getGregorianCalendar(Locale.ENGLISH);
		final GregorianCalendar calB = LocaleUtils.getGregorianCalendar(Locale.ENGLISH);

		calA.setTimeInMillis(inA_Msecs);
		calB.setTimeInMillis(inB_Msecs);

		return getCalendarDifference_Years(calA, calB);
	}

	/*******************************************************************************
	 *******************************************************************************/
	public static double getCurrentAge_Years( final GregorianCalendar inBirthDate)
	{
		final GregorianCalendar theCurrDate = LocaleUtils.getGregorianCalendar(Locale.ENGLISH);

		return getCalendarDifference_Years(inBirthDate, theCurrDate);
	}

	/*******************************************************************************
	 *******************************************************************************/
	public static double getCalendarDifference_Years( final GregorianCalendar inCal_A, final GregorianCalendar inCal_B)
	{
		final int doyA = inCal_A.get(DAY_OF_YEAR);
		final int doyB = inCal_B.get(DAY_OF_YEAR);
		final int theYear_A = inCal_A.get(YEAR);
		final int theYear_B = inCal_B.get(YEAR);

		if (inCal_A.get(DAY_OF_MONTH) == inCal_B.get(DAY_OF_MONTH) && inCal_A.get(MONTH) == inCal_B.get(MONTH) || doyA == doyB)
		{
			return Math.abs(theYear_A - theYear_B);
		}
		else
		{
			final double theFractBit = (double) Math.abs(doyA - doyB) / (double) DAYS_IN_YEAR;

			if (theYear_A == theYear_B)
			{
				return theFractBit;
			}

			final double theYearBit = Math.abs(theYear_A - theYear_B);

			if (theYear_A > theYear_B && doyA > doyB || theYear_A < theYear_B && doyA < doyB)
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
		if (inTodaysCalendar.get(DAY_OF_MONTH) == inBirthCalendar.get(DAY_OF_MONTH) && inTodaysCalendar.get(MONTH) == inBirthCalendar.get(MONTH))
		{
			return inTodaysCalendar.get(YEAR) - inBirthCalendar.get(YEAR);
		}

		return -1;
	}

	/*******************************************************************************
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
			if (theTodaysCalendar.get(MONTH) > theBirthCalendar.get(MONTH) ||
							theTodaysCalendar.get(MONTH) == theBirthCalendar.get(MONTH) &&
							theTodaysCalendar.get(DAY_OF_MONTH) >= theBirthCalendar.get(DAY_OF_MONTH))
			{
				return theTodaysCalendar.get(YEAR) - theBirthCalendar.get(YEAR);
			}
			else
			{
				return theTodaysCalendar.get(YEAR) - theBirthCalendar.get(YEAR) - 1;
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

		double theNumDays = (double) inDate.getTime() / (double) MSECS_IN_DAY;

		theNumDays += 719528;

		return (long) theNumDays;
	}

	/*******************************************************************************
	 *******************************************************************************/
	public static long getDaysSinceYearZero()
	{
		// 9 July 2002. Fine - current year definitely after 1970 epoch

		return System.currentTimeMillis() / MSECS_IN_DAY + 719528;
	}

	/*******************************************************************************
	 *******************************************************************************/
	public static String getMonthName( final int inIndex, final Locale inLocale)
	{
		final DateFormatSymbols theSymbols = new DateFormatSymbols(inLocale);

		return theSymbols.getMonths()[inIndex];
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

		return theDifferenceinMsecs / MSECS_IN_DAY;
	}

	/*******************************************************************************
	 *******************************************************************************/
	public static long getDatesDifferenceMsecs( final long inA_Msecs, final long inB_Msecs)
	{
		if (inA_Msecs > inB_Msecs)
		{
			return inA_Msecs - inB_Msecs;
		}

		return inB_Msecs - inA_Msecs;
	}

	/*******************************************************************************
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