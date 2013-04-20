package com.hiatus.dates;

import static com.hiatus.dates.DateConstants.MSECS_IN_DAY;
import static com.hiatus.dates.DateUtils.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.junit.Test;

public class DateUtilsTest {

	@Test
	public void testGetFormattedTimeNanosDiff() {
		assertThat(getFormattedTimeNanosDiff(100), is("100 nanos"));
		assertThat(getFormattedTimeNanosDiff(50000), is("50 micros"));

		assertThat(getFormattedTimeDiff(1), is("1 msecs"));
		assertThat(getFormattedTimeDiff(1000), is("1 secs"));
		assertThat(getFormattedTimeDiff(1200), is("1.2 secs"));
		assertThat(getFormattedTimeDiff(120000), is("2 mins"));
		assertThat(getFormattedTimeDiff(121000), is("2 mins, 1 secs"));
		assertThat(getFormattedTimeDiff(3600000), is("1 hour"));
		assertThat(getFormattedTimeDiff(3601000), is("1 hour, 1 secs"));
		assertThat(getFormattedTimeDiff(4920000), is("1 hour, 22 mins"));
		assertThat(getFormattedTimeDiff(8920000), is("2 hrs, 28 mins, 40 secs"));
		assertThat(getFormattedTimeDiff(86400000), is("1 day"));
		assertThat(getFormattedTimeDiff(86400001), is("1 day, 0.001 secs"));
		assertThat(getFormattedTimeDiff(86401000), is("1 day, 1 secs"));
		assertThat(getFormattedTimeDiff(86410000, false, false), is("1 day"));
		assertThat(getFormattedTimeDiff(86410000, true, false), is("1 day, 10 secs"));
		assertThat(getFormattedTimeDiff(86410000, false, true), is("1 day"));
		assertThat(getFormattedTimeDiff(86520000), is("1 day, 2 mins"));
		assertThat(getFormattedTimeDiff(92000000), is("1 day, 1 hour, 33 mins, 20 secs"));
		assertThat(getFormattedTimeDiff(172800000), is("2 days"));
	}

	@Test
	public void testgetDatesDifference_ExactDays() {
		final long time = 1366416000000L; // Sat, 20 Apr 2013 00:00:00 GMT
		assertThat(getDatesDifference_ExactDays(new Date(time), new Date(time)), is(0L));
		assertThat(getDatesDifference_ExactDays(new Date(time), new Date(time + MSECS_IN_DAY - 1)), is(0L));
		assertThat(getDatesDifference_ExactDays(new Date(time), new Date(time + MSECS_IN_DAY)), is(1L));
		assertThat(getDatesDifference_ExactDays(new Date(time), new Date(time + MSECS_IN_DAY + 1)), is(1L));
	}

	@Test
	public void testAgeIfBirthdayTodayA() {
		assertThat(getAgeIfBirthdayToday(Locale.UK, new java.sql.Date(System.currentTimeMillis())), is(0));
		assertThat(getAgeIfBirthdayToday(Locale.UK, new java.sql.Date(206982000000L)), is(-1));
		assertThat(getAgeIfBirthdayToday(Locale.UK, new java.sql.Date(198860400000L)), is(37));
	}

	@Test
	public void testAgeIfBirthdayTodayB() {
		assertThat(getAgeIfBirthdayToday(new GregorianCalendar(1976, Calendar.JULY, 23), new GregorianCalendar(2013, Calendar.JULY, 22)), is(-1));
		assertThat(getAgeIfBirthdayToday(new GregorianCalendar(1976, Calendar.JULY, 23), new GregorianCalendar(2013, Calendar.JULY, 23)), is(37));
		assertThat(getAgeIfBirthdayToday(new GregorianCalendar(1976, Calendar.JULY, 23), new GregorianCalendar(2013, Calendar.JULY, 24)), is(-1));
	}
}
