package com.hiatus.dates;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * TODO
 * 
 * @author andrewregan
 * 
 */
public final class TimeDifference {

	private TimeDifference() {
	}

	private static NumberFormat SECONDS_FORMAT;

	static
	{
		SECONDS_FORMAT = NumberFormat.getNumberInstance(Locale.UK);
		SECONDS_FORMAT.setMaximumFractionDigits(3);
	}

	public static String getFormattedTimeDiff( final long inDiffMSecs) {
		return getFormattedTimeDiff(inDiffMSecs, true, true);
	}

	public static String getFormattedTimeNanosDiff( final long inDiffNanos) {
		return getFormattedTimeNanosDiff(inDiffNanos, true, true);
	}

	public static String getFormattedTimeNanosDiff( final long inDiffNanos, final boolean inUseSeconds, final boolean inUseMSecs) {
		if (inDiffNanos < 1000L) {
			return new StringBuilder(11).append(inDiffNanos).append(" nanos").toString();
		}

		final long theMicros = inDiffNanos / 1000L;

		if (theMicros < 1000L) {
			return new StringBuilder(11).append(theMicros).append(" micros").toString();
		}

		return getFormattedTimeDiff(theMicros / 1000L, inUseSeconds, inUseMSecs);
	}

	/*******************************************************************************
	 *******************************************************************************/
	public static String getFormattedTimeDiff( final long inDiffMSecs, final boolean inUseSeconds, final boolean inUseMSecs)
	{
		final StringBuilder theBuf = new StringBuilder(200);

		if (inDiffMSecs >= 1000L)
		{
			double theSecs = inDiffMSecs / 1000.0D;
			boolean wantMins = theSecs >= 60.0D;

			if (wantMins)
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

					if (theHrs >= 1L && theBuf.length() > 0)
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

				if (theMins >= 1L && theBuf.length() > 0) {
					theBuf.append(", ");
				}

				if (theMins == 1L) {
					theBuf.append("1 minute");
				}
				else if (theMins > 1L) {
					theBuf.append(theMins).append(" mins");
				}

			}

			if (inUseSeconds && theSecs > 0.0D)
			{
				if (theBuf.length() > 0) {
					theBuf.append(", ");
				}

				if (theSecs > 0.999999 && theSecs < 1.000001) {
					theBuf.append( wantMins ? "1 sec" : "1 second");
				}
				else if (inUseMSecs)
				{
					theBuf.append(SECONDS_FORMAT.format(theSecs)).append( wantMins ? " secs" : " seconds");
				}
				else {
					theBuf.append(Integer.toString((int) theSecs)).append( wantMins ? " secs" : " seconds");
				}
			}

		}
		else if (inUseMSecs) {
			theBuf.append(inDiffMSecs).append(" msecs");
		}
		else {
			theBuf.append("< 1 second");
		}

		return theBuf.toString();
	}
}
