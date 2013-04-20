/**
 * 
 */
package com.hiatus.dates;

import static com.hiatus.dates.LocaleUtils.getBestTimeZoneString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * TODO
 * 
 * @author andrewregan
 * 
 */
public class LocaleUtilsTest {

	@Test
	public void testGetBestTimeZoneString() {
		// assertThat(getBestTimeZoneString(null, null), is("Europe/London"));
		// assertThat(getBestTimeZoneString("", null), is("Europe/London"));
		// assertThat(getBestTimeZoneString(null, ""), is("Europe/London"));
		// assertThat(getBestTimeZoneString("en", null), is("Europe/London"));
		// assertThat(getBestTimeZoneString(null, "GB"), is("Europe/London"));
		assertThat(getBestTimeZoneString("en", ""), is("Europe/London"));
		assertThat(getBestTimeZoneString("en", "GB"), is("Europe/London"));
		assertThat(getBestTimeZoneString("en", "IE"), is("Europe/Dublin"));
		assertThat(getBestTimeZoneString("en", "ie"), is("Europe/Dublin"));
		assertThat(getBestTimeZoneString("ga", ""), is("Europe/Dublin"));
		assertThat(getBestTimeZoneString("en", "ie.utf-8"), is("Europe/Dublin"));

		assertThat(getBestTimeZoneString("fi", ""), is("Europe/Helsinki"));
		assertThat(getBestTimeZoneString("fi", "FI"), is("Europe/Helsinki"));
		assertThat(getBestTimeZoneString("sv", "FI"), is("Europe/Helsinki"));
		assertThat(getBestTimeZoneString("sv", ""), is("Europe/Stockholm"));

		assertThat(getBestTimeZoneString("", "BE"), is("Europe/Brussels"));
		assertThat(getBestTimeZoneString("be", "BE"), is("Europe/Brussels"));
		assertThat(getBestTimeZoneString("en", "BE"), is("Europe/Brussels"));

		assertThat(getBestTimeZoneString("af", ""), is("Africa/Johannesburg"));
		assertThat(getBestTimeZoneString("af", "GB"), is("Africa/Johannesburg"));
		assertThat(getBestTimeZoneString("", "ZA"), is("Africa/Johannesburg"));
		assertThat(getBestTimeZoneString("en", "ZA"), is("Africa/Johannesburg"));

		assertThat(getBestTimeZoneString("tr", ""), is("Europe/Istanbul"));

		assertThat(getBestTimeZoneString("xx", ""), is(""));
	}
}
