//  "ULocale2.java". Created by AGR on 6 March 2003

package com.hiatus.dates;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/*
 Really, all dates should be formatted using this method:

 DateFormat	d = DateFormat.getInstance();
 d.setTimeFormat( ULocale.getBestTimeZone(theLocale) );
 println( d.format(theDate) );

 ///////////////////////////////////////////////////////////////

 Ordinarily this will be supplied with the current user Locale,
 but for the Languages page we will be supplying the one for
 the language in question.
 */

/*******************************************************************************
*******************************************************************************/
public class ULocale2
{
	public final static int ORDER_DYM = 0, ORDER_DMY = 1, ORDER_MDY = 2, ORDER_MYD = 3, ORDER_YDM = 4, ORDER_YMD = 5;

	private static String[][] s_LangDefaults = {
					{ "be", "Europe/Minsk" }, // Belarusian default
					{ "bg", "Europe/Sofia" }, // Bulgarian default
					{ "bn", "Asia/Calcutta" }, // Bengali default
					{ "ca", "Europe/Madrid" }, // Catalan (sorry!)
					{ "cs", "Europe/Prague" }, // Czech default
					{ "da", "Europe/Copenhagen" }, // Danish default
					{ "de", "Europe/Berlin" }, // German
					{ "el", "Europe/Athens" }, // Greek default
					{ "en", "Europe/London" }, // English
					{ "es", "Europe/Madrid" }, // Spanish
					{ "et", "Europe/Tallinn" }, // Estonian default
					{ "eu", "Europe/Madrid" }, // Basque (sorry!!)
					{ "fo", "Atlantic/Faeroe" }, // Faeroese default
					{ "fr", "Europe/Paris" }, // French
					{ "he", "Asia/Jerusalem" }, // Hebrew (new) default
					{ "hi", "Asia/Calcutta" }, // Hindi default
					{ "hu", "Europe/Budapest" }, // Hungarian default
					{ "id", "Asia/Jakarta" }, // Indonesian default
					{ "in", "Asia/Jakarta" }, // Indonesian default (2) - which is correct? 19 Jan 2002
					{ "is", "Atlantic/Reykjavik" }, // Icelandic default
					{ "it", "Europe/Rome" }, // Italian
					{ "iw", "Asia/Jerusalem" }, // Hebrew (old) default
					{ "ja", "Asia/Tokyo" }, // Japanese default
					{ "ka", "Asia/Tbilisi" }, // Georgian default
					{ "ko", "Asia/Seoul" }, // Korean default
					{ "lt", "Europe/Vilnius" }, // Lithuanian default
					{ "lv", "Europe/Riga" }, // Latvian default
					{ "ms", "Asia/Kuala_Lumpur" }, // Malay default
					{ "no", "Europe/Oslo" }, // Norwegian languages
					{ "nb", "Europe/Oslo" }, // "
					{ "nn", "Europe/Oslo" }, // "
					{ "nl", "Europe/Amsterdam" }, // Dutch default
					{ "pl", "Europe/Warsaw" }, // Polish default
					{ "pt", "Europe/Lisbon" }, // Portuguese default
					{ "ro", "Europe/Bucharest" }, // Romanian default
					{ "ru", "Europe/Moscow" }, // Russian default
					{ "sq", "Europe/Tirane" }, // Albanian default
					{ "sr", "Europe/Belgrade" }, // Serbian default
					{ "sv", "Europe/Stockholm" }, // Swedish default
					{ "th", "Asia/Bangkok" }, // Thai default
					{ "tr", "Europe/Istanbul" }, // Turkish default
					{ "uk", "Europe/Kiev" }, // Ukrainian default
					{ "ur", "Asia/Karachi" }, // Urdu default (Pakistan)
					{ "vi", "Asia/Saigon" }, // Vietnamese default
					{ "zh", "Asia/Shanghai" } // Chinese default
	};

	private static String[][] s_Overrides = {
					{ "AR", "America/Buenos_Aires" }, // Argentina - override Spanish default
					{ "AT", "Europe/Vienna" }, // Austria - override German default
					{ "AU", "Australia/Sydney" }, // Australia - override English default (wot no Canberra?)
					{ "BE", "Europe/Brussels" }, // Belgium - either French or Dutch
					{ "BR", "America/Sao_Paulo" }, // Brazil - override Portuguese default
					{ "BZ", "America/Belize" }, // Belize - override English default
					{ "CA", "America/Montreal" }, // Canada - either French or English
					{ "CH", "Europe/Zurich" }, // Switzerland - either French/German/Italian
					{ "CI", "Africa/Abidjan" }, // Cote D'Ivoire - override French default
					{ "CL", "America/Santiago" }, // Chile - override Spanish default
					{ "CO", "America/Bogota" }, // Colombia - override Spanish default
					{ "CR", "America/Costa_Rica" }, // Costa Rica - override Spanish default
					{ "EC", "America/Guayaquil" }, // Ecuador - override Spanish default
					{ "EG", "Africa/Cairo" }, // Egypt - override Arabic default
					{ "HK", "Asia/Hong_Kong" }, // Hong Kong - override Chinese default
					{ "JM", "America/Jamaica" }, // Jamaica - override English default
					{ "LI", "Europe/Vaduz" }, // Liechtenstein - either French or German
					{ "LU", "Europe/Luxembourg" }, // Luxembourg - either French or German
					{ "MD", "Europe/Chisinau" }, // Moldova - either Russian or Romanian
					{ "MX", "America/Mexico_City" }, // Mexico - override Spanish default
					{ "NI", "America/Managua" }, // Nicaragua - override Spanish default
					{ "NZ", "Pacific/Auckland" }, // NZ - override English default
					{ "PA", "America/Panama" }, // Panama - override Spanish default
					{ "PE", "America/Lima" }, // Peru - override Spanish default
					{ "SA", "Asia/Riyadh" }, // Saudi Arabia - override Arabic default
					{ "SG", "Asia/Singapore" }, // Singapore - override Chinese default
					{ "TT", "America/Port_of_Spain" }, // Trinidad - override English default
					{ "TW", "Asia/Taipei" }, // Taiwan - override Chinese default
					{ "US", "America/New_York" }, // could be English or Spanish (!)
					{ "VE", "America/Caracas" } // Venezuela - override Spanish default
	};

	private static Map<String, String> s_OverridesMap = new HashMap<String, String>();
	private static Map<String, String> s_LangDefaultsMap = new HashMap<String, String>();

	/*******************************************************************************
	*******************************************************************************/
	static
	{
		int i;

		for (i = 0; i < s_Overrides.length; i++)
		{
			s_OverridesMap.put(s_Overrides[i][0], s_Overrides[i][1]);
		}

		for (i = 0; i < s_LangDefaults.length; i++)
		{
			s_LangDefaultsMap.put(s_LangDefaults[i][0], s_LangDefaults[i][1]);
		}
	}

	/*******************************************************************************
	*******************************************************************************/
	public static DateFormat getClientDateTimeFormat( final Locale inLocale)
	{
		DateFormat theFormat;

		try
		{
			if (inLocale != null)
			{
				theFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT /* MEDIUM */, inLocale);
			} else {
				theFormat = DateFormat.getDateTimeInstance();
			}
		} catch (final Exception e)
		{
			theFormat = DateFormat.getDateTimeInstance();
		}

		theFormat.setTimeZone(getBestTimeZone(inLocale));

		return theFormat;
	}

	/*******************************************************************************
	*******************************************************************************/
	public static DateFormat getClientDateTimeFormat( final Locale inLocale, final int inTimeFormat)
	{
		DateFormat theFormat;

		try
		{
			if (inLocale != null)
			{
				theFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, inTimeFormat, inLocale);
			} else {
				theFormat = DateFormat.getDateTimeInstance();
			}
		} catch (final Exception e)
		{
			theFormat = DateFormat.getDateTimeInstance();
		}

		theFormat.setTimeZone(getBestTimeZone(inLocale));

		return theFormat;
	}

	/*******************************************************************************
	*******************************************************************************/
	public static Locale getCorrectedLocale( final Locale inLocale)
	{
		final String theLang = inLocale.getLanguage(); // Under some circumstances this could be wrong, eg "en_gb"
														// instead of "en"
		// The "en_gb" problem was noticed using OmniWeb 4 on Mac OS X - May 2001

		if ((theLang.length() >= 5) && (inLocale.getCountry().length() < 1))
		{
			final int theUScorePos = theLang.indexOf('_'); // eg. "en_gb"

			if (theUScorePos >= 2)
			{
				return new Locale(theLang.substring(0, theUScorePos), theLang.substring(theUScorePos + 1));
			}
		}

		return inLocale;
	}

	/*******************************************************************************
	 * 29 May 2002
	 *******************************************************************************/
	public static String getLocaleHttpHeaderString( final Locale inLocale)
	{
		if (inLocale != null)
		{
			final String theLang = inLocale.getLanguage();

			if (!theLang.isEmpty())
			{
				final String theCountry = inLocale.getCountry();

				if (!theCountry.isEmpty())
				{
					return (theLang + "-" + theCountry.toLowerCase());
				} else {
					return theLang;
				}
			}
		}

		return "en";
	}

	/*******************************************************************************
	*******************************************************************************/
	public static String myGetDisplayLanguage( final Locale inLocale)
	{
		final Locale theLoc = getCorrectedLocale(inLocale);
		return theLoc.getDisplayLanguage(theLoc);
	}

	/*******************************************************************************
	*******************************************************************************/
	public static Locale stringToLocale( final String inString)
	{
		if ( inString != null && inString.length() >= 2)
		{
			final String theLang = inString.substring(0, 2);
			final int thePos = inString.indexOf("-");
			final String theCountry = (thePos >= 0) ? inString.substring(thePos + 1) : "";

			return new Locale(theLang, theCountry);
		}

		return Locale.US;
	}

	/*******************************************************************************
	 * should return GMT if no match
	 *******************************************************************************/
	public static String getBestTimeZoneStr( final Locale inLocale)
	{
		final String theLang = inLocale.getLanguage();
		final String theCountry = inLocale.getCountry();

		return getBestTimeZoneString(theLang, theCountry);
	}

	/*******************************************************************************
	 * 20 May 2002
	 *******************************************************************************/
	public static String getCapitalCityString( final Locale inLocale)
	{
		final String theZoneStr = getBestTimeZoneString(inLocale.getLanguage(), inLocale.getCountry());
		final int theDashPos = theZoneStr.indexOf('/');

		if (theDashPos >= 0)
		{
			return theZoneStr.substring(theDashPos + 1).replace('_', ' ');
		}

		return theZoneStr.replace('_', ' ');
	}

	/*******************************************************************************
	 * should return GMT if no match
	 *******************************************************************************/
	public static TimeZone getBestTimeZone( final Locale inLocale)
	{
		return TimeZone.getTimeZone(getBestTimeZoneStr(inLocale));
	}

	/*******************************************************************************
	 * 8 April 2002
	 *******************************************************************************/
	public static GregorianCalendar getGregorianCalendar( final Locale inLocale)
	{
		return new GregorianCalendar(getBestTimeZone(inLocale), inLocale);
	}

	/***********************************************************************************
	 * 2 August 2001
	 ***********************************************************************************/
	public static int getLocaleDateOrder( final Locale inLocale)
	{
		final SimpleDateFormat theFmt = (SimpleDateFormat) DateFormat.getDateInstance(DateFormat.SHORT, inLocale);
		final String thePattern = theFmt.toPattern().toLowerCase(Locale.UK); // simple Lowercase
		final int theLoc_D = thePattern.indexOf('d');
		final int theLoc_M = thePattern.indexOf('m');
		final int theLoc_Y = thePattern.indexOf('y');

		if ((theLoc_D < theLoc_Y) && (theLoc_Y < theLoc_M)) {
			return ORDER_DYM;
		} else if ((theLoc_D < theLoc_M) && (theLoc_M < theLoc_Y)) {
			return ORDER_DMY;
		} else if ((theLoc_Y < theLoc_D) && (theLoc_D < theLoc_M)) {
			return ORDER_YDM;
		} else if ((theLoc_Y < theLoc_M) && (theLoc_M < theLoc_D)) {
			return ORDER_YMD;
		} else if ((theLoc_M < theLoc_D) && (theLoc_D < theLoc_Y)) {
			return ORDER_MDY;
		} else if ((theLoc_M < theLoc_Y) && (theLoc_Y < theLoc_D)) {
			return ORDER_MYD;
		} else {
			return ORDER_DYM;
		}
	}

	/*******************************************************************************
	*******************************************************************************/
	public static String getBestTimeZoneString( final String inLang, final String inCountry)
	{
		String theZoneStr;

		if (inCountry.length() > 0) // 8 August 2001 !
		{
			theZoneStr = s_OverridesMap.get(inCountry);
			if (theZoneStr != null)
			{
				return theZoneStr;
			}
		}

		// //////////////////////////////////////////////////////////////////////////////

		if (inCountry.equals("FI") || // definitely Finland
						inLang.equals("fi")) // Finland - could be default or Swedish
		{
			return "Europe/Helsinki";
		}
		else if (inCountry.equals("ZA") || // South Africa (eg. for English)
						inLang.equals("af")) // or Afrikaans
		{
			return "Africa/Johannesburg";
		}
		else if (inCountry.equals("IE") || // Eire - override English default
						inLang.equals("ga")) // or Irish Gaelic
		{
			return "Europe/Dublin";
		}

		// ////////////////////////////////////// Language fall-throughs

		theZoneStr = s_LangDefaultsMap.get(inLang);
		return (theZoneStr != null) ? theZoneStr : "";
	}

	/*******************************************************************************
	 * 2 August 2001
	 *******************************************************************************/
	public static void joinDateEntryParts( final Locale inLocale, final StringBuilder ioBuf,
					final StringBuilder inDBuf, final StringBuilder inMBuf, final StringBuilder inYBuf)
	{
		final int theOrder = getLocaleDateOrder(inLocale);

		if (theOrder == ORDER_DMY) {
			ioBuf.append(inDBuf).append(inMBuf).append(inYBuf);
		} else if (theOrder == ORDER_DYM) {
			ioBuf.append(inDBuf).append(inYBuf).append(inMBuf);
		} else if (theOrder == ORDER_MDY) {
			ioBuf.append(inMBuf).append(inDBuf).append(inYBuf);
		} else if (theOrder == ORDER_MYD) {
			ioBuf.append(inMBuf).append(inYBuf).append(inDBuf);
		} else if (theOrder == ORDER_YDM) {
			ioBuf.append(inYBuf).append(inDBuf).append(inMBuf);
		} else if (theOrder == ORDER_YMD) {
			ioBuf.append(inYBuf).append(inMBuf).append(inDBuf);
		}
	}
}