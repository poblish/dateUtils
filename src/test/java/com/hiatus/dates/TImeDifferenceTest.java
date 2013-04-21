package com.hiatus.dates;

import static com.hiatus.dates.TimeDifference.getFormattedTimeDiff;
import static com.hiatus.dates.TimeDifference.getFormattedTimeNanosDiff;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class TImeDifferenceTest {

	@Test
	public void testGetFormattedTimeNanosDiff() {
		assertThat(getFormattedTimeNanosDiff(100), is("100 nanos"));
		assertThat(getFormattedTimeNanosDiff(50000), is("50 micros"));
		assertThat(getFormattedTimeNanosDiff(5000000), is("5 msecs"));

		assertThat(getFormattedTimeDiff(1), is("1 msecs"));
		assertThat(getFormattedTimeDiff(737), is("737 msecs"));
		assertThat(getFormattedTimeDiff(737, false, false), is("< 1 second"));
		assertThat(getFormattedTimeDiff(1000), is("1 second"));
		assertThat(getFormattedTimeDiff(1200), is("1.2 seconds"));
		assertThat(getFormattedTimeDiff(2000, true, false), is("2 seconds"));
		assertThat(getFormattedTimeDiff(60000), is("1 minute"));
		assertThat(getFormattedTimeDiff(120000), is("2 mins"));
		assertThat(getFormattedTimeDiff(121000), is("2 mins, 1 sec"));
		assertThat(getFormattedTimeDiff(122000, true, false), is("2 mins, 2 secs"));
		assertThat(getFormattedTimeDiff(3600000), is("1 hour"));
		assertThat(getFormattedTimeDiff(3601000), is("1 hour, 1 sec"));
		assertThat(getFormattedTimeDiff(4920000), is("1 hour, 22 mins"));
		assertThat(getFormattedTimeDiff(8920000), is("2 hrs, 28 mins, 40 secs"));
		assertThat(getFormattedTimeDiff(86400000), is("1 day"));
		assertThat(getFormattedTimeDiff(86400001), is("1 day, 0.001 secs"));
		assertThat(getFormattedTimeDiff(86401000), is("1 day, 1 sec"));
		assertThat(getFormattedTimeDiff(86410000, false, false), is("1 day"));
		assertThat(getFormattedTimeDiff(86410000, true, false), is("1 day, 10 secs"));
		assertThat(getFormattedTimeDiff(86410000, false, true), is("1 day"));
		assertThat(getFormattedTimeDiff(86520000), is("1 day, 2 mins"));
		assertThat(getFormattedTimeDiff(92000000), is("1 day, 1 hour, 33 mins, 20 secs"));
		assertThat(getFormattedTimeDiff(172800000), is("2 days"));
	}
}