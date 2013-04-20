//  "DateConstants.java". Created by AGR on 6 March 2003

package com.hiatus.dates;

/**
 * TODO
 * 
 * @author andrewregan
 */
interface DateConstants
{
	int MSECS_IN_SECOND = 1000;
	int SECS_IN_MIN = 60;
	int MINS_IN_HR = 60;
	int HRS_IN_DAY = 24;
	int DAYS_IN_WEEK = 7;

	int SECS_IN_DAY = SECS_IN_MIN * MINS_IN_HR * HRS_IN_DAY;
	int MSECS_IN_MINUTE = MSECS_IN_SECOND * SECS_IN_MIN;
	int MSECS_IN_DAY = MSECS_IN_SECOND * SECS_IN_DAY;
	int MSECS_IN_HOUR = MSECS_IN_MINUTE * MINS_IN_HR;
	double DAYS_IN_YEAR = 365.25;

	long DATE_OK = 0;
	long BAD_DATE_DAY = 0x1000;
	long BAD_DATE_MONTH = 0x2000;
	long BAD_DATE_YEAR = 0x4000;
}