package net.dertod2.SimpleUnlock.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	private static long second = 1000, minute = 60000, hour = 3600000, day = 86400000, month = Long.valueOf("2592000000"), year = Long.valueOf("31104000000");	
	
	private static Pattern urlExpression = Pattern.compile("^((http|https|ftp)\\://)?([a-zA-Z0-9\\.\\-]+(\\:[a-zA-Z0-9\\.&amp;%\\$\\-]+)*@)*" +
			"((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}" +
			"[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]" +
			"{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|localhost|([a-zA-Z0-9\\-]+\\.)*[a-zA-Z0-9\\-]+\\.(com|de|edu|gov|int|mil|net|" +
			"org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(\\:[0-9]+)*(/($|[a-zA-Z0-9\\.\\,\\?\\'\\\\\\+&amp;%\\$#\\=~_\\-]+))*$");
	
	/**
	 * Parses an new String out of an String Array
	 * @param args
	 * @return String
	 */
	public static String getString(String[] args) {
		return StringUtils.getString(args, 0, args.length, "");
	}
	
	/**
	 * Parses an new String out of an String Array
	 * @param args
	 * @param delimeter
	 * @return String
	 */
	public static String getString(String[] args, String delimeter) {
		return StringUtils.getString(args, 0, args.length, delimeter);
	}
	
	/**
	 * Parses an new String out of an String Array<br />
	 * The New String start at the given Index in String[]
	 * @param args
	 * @param beginIndex
	 * @return String
	 */
	public static String getString(String[] args, int beginIndex) {
		return StringUtils.getString(args, beginIndex, args.length, "");
	}
	
	/**
	 * Parses an new String out of an String Array<br />
	 * The New String start at the given Index in String[]<br />
	 * Adds between two String the given delimeter. Not after the last Args Entry
	 * @param args
	 * @param beginIndex
	 * @param delimeter
	 * @return String
	 */
	public static String getString(String[] args, int beginIndex, String delimeter) {
		return StringUtils.getString(args, beginIndex, args.length, delimeter);
	}
	
	/**
	 * Parses an new String out of an String Array<br />
	 * The New String start at the given Index in String[]<br />
	 * The New String ends at the given Index in String[]<br />
	 * Adds between two String the given delimeter. Not after the last Args Entry
	 * @param args
	 * @param beginIndex
	 * @param endIndex
	 * @param delimeter
	 * @return String
	 */
	public static String getString(String[] args, int beginIndex, int endIndex, String delimeter) {
		if (args.length <= 0) return "";
		
		StringBuilder stringBuilder = new StringBuilder("");
		
		for (int i = beginIndex; i < endIndex; i++) {
			stringBuilder.append(args[i]);
			stringBuilder.append(delimeter);
		}
		
		if (stringBuilder.length() == 0) return "";
		if (delimeter.length() > 0) return stringBuilder.delete(stringBuilder.length() - delimeter.length(), stringBuilder.length()).toString().toString();
		return stringBuilder.toString().trim();
	}
	
	public static boolean isUrl(String url) {
		Matcher matcher = urlExpression.matcher(url);
		return matcher.matches();
	}
	
	public static String timeToString(long time) {
		long timeYears = time > year ? time / year : 0;
		time -= timeYears * year;
		long timeMonths = time > month ? time / month : 0;
		time -= timeMonths * month;
		long timeDays = time > day ? time / day : 0;
		time -= timeDays * day;
		long timeHours = time > hour ? time / hour : 0;
		time -= timeHours * hour;
		long timeMinutes = time > minute ? time / minute : 0;
		time -= timeMinutes * minute;
		long timeSeconds = time > second ? time / second : 0;
		time -= timeSeconds * second;
		
		String returnableString = "";
		
		returnableString += timeYears != 0 ? timeYears + " " + (timeYears > 1 ? "Jahre" : "Jahr") + " " : "";
		returnableString += timeMonths != 0 ? timeMonths + " " + (timeMonths > 1 ? "Monate" : "Monat") + " " : "";
		returnableString += timeDays != 0 ? timeDays + " " + (timeDays > 1 ? "Tage" : "Tag") + " " : "";
		
		returnableString += timeHours != 0 ? timeHours + " " + (timeHours > 1 ? "Stunden" : "Stunde") + " " : "";
		returnableString += timeMinutes != 0 ? timeMinutes + " " + (timeMinutes > 1 ? "Minuten" : "Minute") + " " : "";
		returnableString += timeSeconds != 0 ? timeSeconds + " " + (timeSeconds > 1 ? "Sekunden" : "Sekunde") + " " : "";
	
		if (returnableString.length() == 0) returnableString = "< 1 " + "Sekunden";
		return returnableString.trim();
	}
	
	/**
	 * Fetches the Time out of the String.<br />
	 * Can handle modifiers like 's', 'm', 'h', 'd', 'w' and 'y'<br />
	 * to calculate other Times. (ex. m -> minute)<br />
	 * Default handles Input as Milliseconds
	 * @param time
	 * @return
	 */
	public static Long getTime(String time) {
		try {
			if (time.length() == 0) return null;
			if (time.matches("[0-9]+")) return Long.valueOf(time);
			if (time.length() == 1) return null;
			
			long returnTime = 0;
			
			int lastLetter = 0;
			for (int i = 0; i < time.length(); i++) {
				String character = String.valueOf(time.charAt(i)).toLowerCase();
				if (!character.matches("[0-9]")) {
					
					
					Long fetchedTime = Long.valueOf(time.substring(lastLetter, i));
					if (fetchedTime == null) return null;
					
					if (character.equals("s")) {
						 returnTime += (fetchedTime * 1000);
					} else if (character.equals("m")) {
						 returnTime += ((fetchedTime * 60) * 1000);
					} else if (character.equals("h")) {
						 returnTime += ((fetchedTime * 60 * 60) * 1000);
					}  else if (character.equals("d")) {
						 returnTime += ((fetchedTime * 60 * 60 * 24) * 1000);
					}  else if (character.equals("w")) {
						 returnTime += ((fetchedTime * 60 * 60 * 24 * 7) * 1000);		
					}  else if (character.equals("y")) {
						 returnTime += ((fetchedTime * 60 * 60 * 24 * 30 * 12) * 1000);
					} else {
						return null;
					}
					
					lastLetter = i;
				} else {
					if (i == time.length()) {
						returnTime += Long.valueOf(time.substring(lastLetter, i));
					}
				}
			}
			
			return returnTime;
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		
		return null;
	}
}