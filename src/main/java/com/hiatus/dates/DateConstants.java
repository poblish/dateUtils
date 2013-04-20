//  "DateConstants.java". Created by AGR on 6 March 2003

package com.hiatus.dates;

/*******************************************************************************
*******************************************************************************/
public interface DateConstants
{
	public static final int MSECS_IN_SECOND = 1000;
	public final static int SECS_IN_MIN = 60;
	public final static int MINS_IN_HR = 60;
	public final static int HRS_IN_DAY = 24;
	public final static int DAYS_IN_WEEK = 7;

	public static final int SECS_IN_DAY = SECS_IN_MIN * MINS_IN_HR * HRS_IN_DAY;
	public static final int MSECS_IN_MINUTE = MSECS_IN_SECOND * SECS_IN_MIN;
	public static final int MSECS_IN_DAY = MSECS_IN_SECOND * SECS_IN_DAY;
	public static final int MSECS_IN_HOUR = MSECS_IN_MINUTE * MINS_IN_HR;
	public static final double DAYS_IN_YEAR = 365.25;

	public final static long DATE_OK = 0;
	public final static long BAD_DATE_DAY = 0x1000;
	public final static long BAD_DATE_MONTH = 0x2000;
	public final static long BAD_DATE_YEAR = 0x4000;
}