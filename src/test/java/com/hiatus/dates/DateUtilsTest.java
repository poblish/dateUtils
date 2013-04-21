package com.hiatus.dates;

import static com.hiatus.dates.DateConstants.MSECS_IN_DAY;
import static com.hiatus.dates.DateUtils.*;
import static java.util.Calendar.JANUARY;
import static java.util.Calendar.JULY;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.junit.Test;

public class DateUtilsTest {

	@Test
	public void getCurrentTimestamp() {
		assertThat();
		getCurrentTimestamp()
	}
	@Test
	public void testGetDatesDifferenceYears() {
		assertThat(getCalendarDifference_Years(new GregorianCalendar(1976, JULY, 23), new GregorianCalendar(2013, JULY, 25)), is(37.002737850787135));
		assertThat(getCalendarDifference_Years(new GregorianCalendar(1976, JULY, 23), new GregorianCalendar(2013, JULY, 24)), is(37d));  // 37.0 (due to leap years), but not a birthday
		assertThat(getCalendarDifference_Years(new GregorianCalendar(1976, JULY, 23), new GregorianCalendar(2013, JULY, 23)), is(37d));  // 37th birthday
		assertThat(getCalendarDifference_Years(new GregorianCalendar(1976, JULY, 23), new GregorianCalendar(2013, JULY, 22)), is(36.99452429842574));

		assertThat(getCalendarDifference_Years(new GregorianCalendar(1976, JULY, 23), new GregorianCalendar(2012, JULY, 24)), is(36.002737850787135));  // Not 36.0
		assertThat(getCalendarDifference_Years(new GregorianCalendar(1976, JULY, 23), new GregorianCalendar(2012, JULY, 23)), is(36d));  // 36th birthday
		assertThat(getCalendarDifference_Years(new GregorianCalendar(1976, JULY, 23), new GregorianCalendar(2012, JULY, 22)), is(35.997262149212865));
		assertThat(getCalendarDifference_Years(new GregorianCalendar(2013, JANUARY, 1), new GregorianCalendar(2013, JULY, 1)), is(0.49555099247091033));

		double diff1 = 0.038329911019849415;
		assertThat(getCalendarDifference_Years(new GregorianCalendar(2013, JANUARY, 1), new GregorianCalendar(2013, JANUARY, 15)), is(diff1));
		assertThat(getCalendarDifference_Years(new GregorianCalendar(2013, JANUARY, 15), new GregorianCalendar(2013, JANUARY, 1)), is(diff1));

		double diff2 = 1.0383299110198494;
		assertThat(getCalendarDifference_Years(new GregorianCalendar(2013, JANUARY, 15), new GregorianCalendar(2012, JANUARY, 1)), is(diff2));
		assertThat(getCalendarDifference_Years(new GregorianCalendar(2012, JANUARY, 1), new GregorianCalendar(2013, JANUARY, 15)), is(diff2));

		double diff3 = 0.9616700889801506;
		assertThat(getCalendarDifference_Years(new GregorianCalendar(2012, JANUARY, 15), new GregorianCalendar(2013, JANUARY, 1)), is(diff3));
		assertThat(getCalendarDifference_Years(new GregorianCalendar(2013, JANUARY, 1), new GregorianCalendar(2012, JANUARY, 15)), is(diff3));
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

		final Calendar afterCalendar = Calendar.getInstance(Locale.UK);
		afterCalendar.set( Calendar.YEAR, afterCalendar.get(Calendar.YEAR) - 30);
		assertThat(getAgeIfBirthdayToday(Locale.UK, new java.sql.Date( afterCalendar.getTimeInMillis() )), is(30));
	}

	@Test
	public void testAgeIfBirthdayTodayB() {
		assertThat(getAgeIfBirthdayToday(new GregorianCalendar(1976, JULY, 23), new GregorianCalendar(2013, JULY, 22)), is(-1));
		assertThat(getAgeIfBirthdayToday(new GregorianCalendar(1976, JULY, 23), new GregorianCalendar(2013, JULY, 23)), is(37));
		assertThat(getAgeIfBirthdayToday(new GregorianCalendar(1976, JULY, 23), new GregorianCalendar(2013, JULY, 24)), is(-1));
	}
}
