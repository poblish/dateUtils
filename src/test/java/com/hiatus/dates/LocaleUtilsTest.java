/**
 * 
 */
package com.hiatus.dates;

import static com.hiatus.dates.LocaleUtils.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import org.junit.Test;

/**
 * TODO
 * 
 * @author andrewregan
 * 
 */
public class LocaleUtilsTest {

	@Test
	public void testDateOrder() {
		assertThat(getLocaleDateOrder(Locale.UK), is(DateOrder.ORDER_DMY));
		assertThat(getLocaleDateOrder(Locale.US), is(DateOrder.ORDER_MDY));
		assertThat(getLocaleDateOrder(Locale.CANADA), is(DateOrder.ORDER_DMY));
		assertThat(getLocaleDateOrder(Locale.FRANCE), is(DateOrder.ORDER_DMY));
		assertThat(getLocaleDateOrder(Locale.GERMANY), is(DateOrder.ORDER_DMY));
		assertThat(getLocaleDateOrder(Locale.CHINA), is(DateOrder.ORDER_YMD));
		assertThat(getLocaleDateOrder(Locale.JAPAN), is(DateOrder.ORDER_YMD));
	}

	@Test
	public void testBestTimeZones() {
		// testBestTimeZoneFor(null, null, "Europe/London");
		// testBestTimeZoneFor("", null, "Europe/London");
		// testBestTimeZoneFor(null, "", "Europe/London");
		// testBestTimeZoneFor("en", null, "Europe/London");
		// testBestTimeZoneFor(null, "GB", "Europe/London");
		testBestTimeZoneFor("en", "", "Europe/London");
		testBestTimeZoneFor("en", "GB", "Europe/London");
		testBestTimeZoneFor("en", "IE", "Europe/Dublin");
		testBestTimeZoneFor("en", "ie", "Europe/Dublin");
		testBestTimeZoneFor("ga", "", "Europe/Dublin");
		testBestTimeZoneFor("en", "ie.utf-8", "Europe/Dublin");

		testBestTimeZoneFor("fi", "", "Europe/Helsinki");
		testBestTimeZoneFor("fi", "FI", "Europe/Helsinki");
		testBestTimeZoneFor("sv", "FI", "Europe/Helsinki");
		testBestTimeZoneFor("sv", "", "Europe/Stockholm");

		testBestTimeZoneFor("", "BE", "Europe/Brussels");
		testBestTimeZoneFor("be", "BE", "Europe/Brussels");
		testBestTimeZoneFor("en", "BE", "Europe/Brussels");

		testBestTimeZoneFor("af", "", "Africa/Johannesburg");
		testBestTimeZoneFor("af", "GB", "Africa/Johannesburg");
		testBestTimeZoneFor("", "ZA", "Africa/Johannesburg");
		testBestTimeZoneFor("en", "ZA", "Africa/Johannesburg");

		testBestTimeZoneFor("tr", "", "Europe/Istanbul");

		testBestTimeZoneFor("xx", "", "");
	}

	private void testBestTimeZoneFor( final String lang, final String country, final String expectedResult) {
		assertThat(getBestTimeZoneString(lang, country), is(expectedResult));
		assertThat(getBestTimeZoneStr(new Locale(lang, country)), is(expectedResult));
	}
}
