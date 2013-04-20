package com.hiatus.dates;

import static com.hiatus.dates.DateUtils.getFormattedTimeDiff;
import static com.hiatus.dates.DateUtils.getFormattedTimeNanosDiff;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class DateUtilsTest {

	@Test
	public void testGetFormattedTimeNanosDiff() {
		assertThat(getFormattedTimeNanosDiff(100).toString(), is("100 nanos"));
		assertThat(getFormattedTimeNanosDiff(50000).toString(), is("50 micros"));

		assertThat(getFormattedTimeDiff(1).toString(), is("1 msecs"));
		assertThat(getFormattedTimeDiff(1000).toString(), is("1 secs"));
		assertThat(getFormattedTimeDiff(1200).toString(), is("1.2 secs"));
		assertThat(getFormattedTimeDiff(120000).toString(), is("2 mins"));
		assertThat(getFormattedTimeDiff(121000).toString(), is("2 mins, 1 secs"));
		assertThat(getFormattedTimeDiff(3600000).toString(), is("1 hour"));
		assertThat(getFormattedTimeDiff(3601000).toString(), is("1 hour, 1 secs"));
		assertThat(getFormattedTimeDiff(4920000).toString(), is("1 hour, 22 mins"));
		assertThat(getFormattedTimeDiff(8920000).toString(), is("2 hrs, 28 mins, 40 secs"));
		assertThat(getFormattedTimeDiff(86400000).toString(), is("1 day"));
		assertThat(getFormattedTimeDiff(86400001).toString(), is("1 day, 0.001 secs"));
		assertThat(getFormattedTimeDiff(86401000).toString(), is("1 day, 1 secs"));
		assertThat(getFormattedTimeDiff(86410000, false, false).toString(), is("1 day"));
		assertThat(getFormattedTimeDiff(86410000, true, false).toString(), is("1 day, 10 secs"));
		assertThat(getFormattedTimeDiff(86410000, false, true).toString(), is("1 day"));
		assertThat(getFormattedTimeDiff(86520000).toString(), is("1 day, 2 mins"));
		assertThat(getFormattedTimeDiff(92000000).toString(), is("1 day, 1 hour, 33 mins, 20 secs"));
		assertThat(getFormattedTimeDiff(172800000).toString(), is("2 days"));
	}

}
