package io.github.hison.utils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

/**
 * Provides a comprehensive set of utility functions for various common tasks, including 
 * string validation and manipulation, number formatting, date and time operations, 
 * and property file handling. This class is designed to be a singleton and cannot 
 * be instantiated, ensuring that all methods are accessed in a static context.
 *
 * <p>Features:</p>
 * <ul>
 *     <li>String Validation: Offers methods to check if a string is composed of specific character sets, 
 *         such as alphabetic characters, numeric characters, or a combination of both. Additionally, 
 *         includes methods to verify the presence of special characters or if the string matches a 
 *         particular pattern.</li>
 *     <li>Number Validation and Formatting: Provides methods to determine if a string represents a 
 *         valid number, and supports various rounding, ceiling, floor, and truncation operations. 
 *         Also includes functionality to format numbers according to specified patterns.</li>
 *     <li>Date and Time Operations: Facilitates the parsing and formatting of dates and times, 
 *         including adding or subtracting time units, and calculating the difference between dates. 
 *         It can also determine the last day of a month or the day of the week for a given date.</li>
 *     <li>String Manipulation: Includes methods for padding, cutting, and reversing strings, 
 *         as well as calculating byte lengths and ensuring specific string lengths with inserted spaces.</li>
 *     <li>Property File Handling: Simplifies the loading and retrieving of values from property files, 
 *         supporting the retrieval of individual values, entire property sets, or keys matching a prefix.</li>
 * </ul>
 *
 * <p>Example usage of date addition:</p>
 * <pre>
 * String newDate = Utils.addDate("2024-01-01", 5, "d");
 * // Adds 5 days to the given date
 * </pre>
 *
 * <p>Customization:</p>
 * This class supports extensive customization through the configuration file "hison-utils-config.properties". 
 * Users can override default formats and types by specifying their values in the configuration file. 
 * Properties include date and time formats, number formatting patterns, and more.
 *
 * <pre>
 * date.formatter=yyyy/MM/dd
 * datetime.formatter=yyyy/MM/dd HH:mm:ss
 * add.type=d
 * number.formatter=#,##0.00
 * </pre>
 * 
 * <p>Usage:</p>
 * <ul>
 *     <li>All methods in this class are static and can be accessed directly using the class name, 
 *         e.g., <code>Utils.isAlpha("testString")</code>.</li>
 *     <li>For date and time operations, ensure the input strings match the expected format specified 
 *         in the configuration or use default formats.</li>
 * </ul>
 *
 * <p>Dependencies:</p>
 * <ul>
 *     <li>Java Standard Libraries: Utilizes java.time for date and time operations, 
 *         java.util for collections and properties, and javax.servlet for request handling.</li>
 * </ul>
 * 
 * <p>Version History:</p>
 * <ul>
 *     <li>1.0.0: Initial release with basic utility functions.</li>
 * </ul>
 *
 * @author Hani son
 * @version 1.0.0
 */
public final class Utils {
    private Utils() {
        throw new UtilsException("No Utils instances for you!");
    }
    private static String DATE_FORMATTER = "yyyy-MM-dd";
    private static String DATETIME_FORMATTER = "yyyy-MM-dd HH:mm:ss";
    private static String ADD_TYPE = "d";
    private static String DIFF_TYPE = "d";
    private static String DAY_OF_WEEK_TYPE = "d";
    private static int LESSOREQ_0X7FF_BYTE = 2;
    private static int LESSOREQ_0XFFFF_BYTE = 3;
    private static int GREATER_0XFFFF_BYTE = 4;
    private static String NUMBER_FORMATTER = "#,##0.#####";
    private static String PROPERTIE_FILE_PATH = "";

    static {
        Properties prop = new Properties();
        try (InputStream input = Utils.class.getClassLoader().getResourceAsStream("hison-utils-config.properties")) {
            if (input != null) {
                prop.load(input);
            }
        } catch (IOException ex) {
        }
        if(!prop.isEmpty()){
            DATE_FORMATTER = prop.getProperty("date.formatter") != null ? prop.getProperty("date.formatter") : DATE_FORMATTER;
            DATETIME_FORMATTER = prop.getProperty("datetime.formatter") != null ? prop.getProperty("datetime.formatter") : DATETIME_FORMATTER;
            ADD_TYPE = prop.getProperty("add.type") != null ? prop.getProperty("add.type") : ADD_TYPE;
            DIFF_TYPE = prop.getProperty("diff.type") != null ? prop.getProperty("diff.type") : DIFF_TYPE;
            DAY_OF_WEEK_TYPE = prop.getProperty("dayofweek.type") != null ? prop.getProperty("dayofweek.type") : DAY_OF_WEEK_TYPE;
            LESSOREQ_0X7FF_BYTE = prop.getProperty("lessoreq.0x7ff.byte") != null && prop.getProperty("lessoreq.0x7ff.byte").matches("\\d+") ? Integer.parseInt(prop.getProperty("lessoreq.0x7ff.byte")) : LESSOREQ_0X7FF_BYTE;
            LESSOREQ_0XFFFF_BYTE = prop.getProperty("lessoreq.0xffff.byte") != null && prop.getProperty("lessoreq.0xffff.byte").matches("\\d+") ? Integer.parseInt(prop.getProperty("lessoreq.0xffff.byte")) : LESSOREQ_0XFFFF_BYTE;
            GREATER_0XFFFF_BYTE = prop.getProperty("greater.0xffff.byte") != null && prop.getProperty("greater.0xffff.byte").matches("\\d+") ? Integer.parseInt(prop.getProperty("greater.0xffff.byte")) : GREATER_0XFFFF_BYTE;
            PROPERTIE_FILE_PATH = prop.getProperty("propertie.file.path") != null ? prop.getProperty("propertie.file.path") : PROPERTIE_FILE_PATH;
            NUMBER_FORMATTER = prop.getProperty("number.formatter") != null ? prop.getProperty("number.formatter") : NUMBER_FORMATTER;
        }
    }

    /**********************************************************************
     * for boolean
     **********************************************************************/
    /**
     * Checks if the given string contains only alphabetic characters (a-z, A-Z).
     *
     * <p>This method iterates through each character in the provided string to determine if it 
     * is an alphabetic character. It returns {@code true} if all characters in the string are 
     * alphabetic, and {@code false} otherwise.</p>
     *
     * <p><b>Note:</b> This method considers only standard English alphabetic characters and 
     * does not account for accented characters or characters from other languages.</p>
     *
     * @param s The string to be checked.
     * @return {@code true} if the string contains only alphabetic characters, {@code false} otherwise.
     */
    public static boolean isAlpha(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        for (char c : s.toCharArray()) {
            if (!((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the given string contains only alphabetic characters (a-z, A-Z) and digits (0-9).
     *
     * <p>This method iterates through each character in the provided string to determine if it 
     * is either an alphabetic character or a digit. It returns {@code true} if all characters 
     * in the string are alphabetic or numeric, and {@code false} otherwise.</p>
     *
     * <p><b>Note:</b> This method considers only standard English alphabetic characters and 
     * numeric digits, and does not account for accented characters or characters from other languages.</p>
     *
     * @param s The string to be checked.
     * @return {@code true} if the string contains only alphabetic characters and digits, {@code false} otherwise.
     */
    public static boolean isAlphaNumber(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        for (char c : s.toCharArray()) {
            if (!(Character.isDigit(c) || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the given string contains only numeric characters (0-9).
     *
     * <p>This method iterates through each character in the provided string to determine if it 
     * is a numeric character. It returns {@code true} if all characters in the string are 
     * numeric, and {@code false} otherwise.</p>
     *
     * <p><b>Note:</b> This method does not account for numeric characters with decimal points, 
     * negative signs, or other numeric notations. It strictly checks for digits.</p>
     *
     * @param s The string to be checked.
     * @return {@code true} if the string contains only numeric characters, {@code false} otherwise.
     */

    public static boolean isNumber(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        for (char c : s.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the given string contains only numeric characters (0-9) and special symbols.
     *
     * <p>This method uses a regular expression pattern to determine if the provided string 
     * contains only numeric characters and a set of predefined special symbols 
     * (!@#$%^&*()_+\-=[]{};':"\\|,.<>/?~). It returns {@code true} if all characters in the 
     * string match this pattern, and {@code false} otherwise.</p>
     *
     * <p><b>Note:</b> This method strictly checks for the specified special symbols and numeric 
     * characters. Any other characters will cause the method to return {@code false}.</p>
     *
     * @param s The string to be checked.
     * @return {@code true} if the string contains only numeric characters and specified special symbols, {@code false} otherwise.
     */
    public static boolean isNumberSymbols(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?~]+$");
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }

    /**
     * Checks if the given string contains any special symbols.
     *
     * <p>This method uses a regular expression pattern to determine if the provided string 
     * contains any special symbols (!@#$%^&*()_+\-=[]{};':"\\|,.<>/?~). It returns {@code true} 
     * if the string contains at least one of these special symbols, and {@code false} otherwise.</p>
     *
     * <p><b>Note:</b> This method returns {@code true} if there is any match of the special symbols 
     * in the string. It does not check for other character types.</p>
     *
     * @param s The string to be checked.
     * @return {@code true} if the string contains any special symbols, {@code false} otherwise.
     */
    public static boolean isIncludeSymbols(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        Pattern pattern = Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?~]");
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }

    /**
     * Checks if the given string contains only lowercase alphabetic characters (a-z).
     *
     * <p>This method uses a regular expression pattern to determine if the provided string 
     * contains only lowercase alphabetic characters. It returns {@code true} if all characters 
     * in the string are lowercase alphabetic, and {@code false} otherwise.</p>
     *
     * <p><b>Note:</b> This method considers only standard English lowercase alphabetic characters 
     * and does not account for accented characters or characters from other languages.</p>
     *
     * @param s The string to be checked.
     * @return {@code true} if the string contains only lowercase alphabetic characters, {@code false} otherwise.
     */
    public static boolean isLowerAlpha(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        return s.matches("[a-z]+");
    }

    /**
     * Checks if the given string contains only lowercase alphabetic characters (a-z) and digits (0-9).
     *
     * <p>This method uses a regular expression pattern to determine if the provided string 
     * contains only lowercase alphabetic characters and numeric digits. It returns {@code true} 
     * if all characters in the string are lowercase alphabetic or numeric, and {@code false} otherwise.</p>
     *
     * <p><b>Note:</b> This method considers only standard English lowercase alphabetic characters 
     * and numeric digits, and does not account for accented characters or characters from other languages.</p>
     *
     * @param s The string to be checked.
     * @return {@code true} if the string contains only lowercase alphabetic characters and digits, {@code false} otherwise.
     */
    public static boolean isLowerAlphaNumber(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        return s.matches("[a-z0-9]+");
    }
    
    /**
     * Checks if the given string contains only uppercase alphabetic characters (A-Z).
     *
     * <p>This method uses a regular expression pattern to determine if the provided string 
     * contains only uppercase alphabetic characters. It returns {@code true} if all characters 
     * in the string are uppercase alphabetic, and {@code false} otherwise.</p>
     *
     * <p><b>Note:</b> This method considers only standard English uppercase alphabetic characters 
     * and does not account for accented characters or characters from other languages.</p>
     *
     * @param s The string to be checked.
     * @return {@code true} if the string contains only uppercase alphabetic characters, {@code false} otherwise.
     */
    public static boolean isUpperAlpha(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        return s.matches("[A-Z]+");
    }

    /**
     * Checks if the given string contains only uppercase alphabetic characters (A-Z) and digits (0-9).
     *
     * <p>This method uses a regular expression pattern to determine if the provided string 
     * contains only uppercase alphabetic characters and numeric digits. It returns {@code true} 
     * if all characters in the string are uppercase alphabetic or numeric, and {@code false} otherwise.</p>
     *
     * <p><b>Note:</b> This method considers only standard English uppercase alphabetic characters 
     * and numeric digits, and does not account for accented characters or characters from other languages.</p>
     *
     * @param s The string to be checked.
     * @return {@code true} if the string contains only uppercase alphabetic characters and digits, {@code false} otherwise.
     */
    public static boolean isUpperAlphaNumber(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        return s.matches("[A-Z0-9]+");
    }

    /**
     * Checks if the given string represents a valid numeric value.
     *
     * <p>This method uses a regular expression pattern to determine if the provided string 
     * represents a valid number. It considers both integer and decimal numbers, and allows 
     * for an optional leading negative sign.</p>
     *
     * <p>The regular expression used is <code>-?\\d+(\\.\\d+)?</code>, which matches:</p>
     * <ul>
     *     <li>Optional negative sign (-)</li>
     *     <li>One or more digits (\\d+)</li>
     *     <li>Optional decimal point followed by one or more digits (\\.\\d+)</li>
     * </ul>
     *
     * @param s The string to be checked.
     * @return {@code true} if the string represents a valid numeric value, {@code false} otherwise.
     */
    public static boolean isNumeric(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        return s.matches("-?\\d+(\\.\\d+)?");
    }
    /**
     * Checks if the given string represents a valid integer value.
     *
     * <p>This method attempts to parse the provided string as a double to handle potential decimal 
     * points and then compares the parsed value to its integer representation to determine if it is 
     * an integer.</p>
     *
     * <p>If the string can be parsed as a double and its value is equivalent to its integer 
     * representation, the method returns {@code true}. If the string is null, empty, or cannot be 
     * parsed as a double, the method returns {@code false}.</p>
     *
     * @param s The string to be checked.
     * @return {@code true} if the string represents a valid integer value, {@code false} otherwise.
     */
    public static boolean isInteger(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        try {
            double d = Double.parseDouble(s);
            return d == (int) d;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    /**
     * Checks if the given double value is an integer.
     *
     * <p>This method compares the provided double value to its integer representation to determine 
     * if it is an integer. It returns {@code true} if the double value is equivalent to its integer 
     * representation, and {@code false} otherwise.</p>
     *
     * @param d The double value to be checked.
     * @return {@code true} if the double value is an integer, {@code false} otherwise.
     */
    public static boolean isInteger(double d) {
        return d == (int) d;
    }
    /**
     * Checks if the given float value is an integer.
     *
     * <p>This method compares the provided float value to its integer representation to determine 
     * if it is an integer. It returns {@code true} if the float value is equivalent to its integer 
     * representation, and {@code false} otherwise.</p>
     *
     * @param f The float value to be checked.
     * @return {@code true} if the float value is an integer, {@code false} otherwise.
     */
    public static boolean isInteger(float f) {
        return f == (int) f;
    }

    /**
     * Checks if the given string represents a positive integer.
     *
     * <p>This method attempts to parse the provided string as an integer and then checks if the 
     * parsed value is greater than zero. It returns {@code true} if the string can be parsed as 
     * an integer and the value is positive, and {@code false} otherwise.</p>
     *
     * <p>If the string is null, empty, or cannot be parsed as an integer, the method returns {@code false}.</p>
     *
     * @param s The string to be checked.
     * @return {@code true} if the string represents a positive integer, {@code false} otherwise.
     */
    public static boolean isPositiveInteger(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        try {
            int value = Integer.parseInt(s);
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    /**
     * Checks if the given double value is a positive integer.
     *
     * <p>This method checks if the provided double value is greater than zero and if it is 
     * equivalent to its integer representation. It returns {@code true} if both conditions are 
     * met, and {@code false} otherwise.</p>
     *
     * @param d The double value to be checked.
     * @return {@code true} if the double value is a positive integer, {@code false} otherwise.
     */
        public static boolean isPositiveInteger(double d) {
        return d > 0 && d == (int) d;
    }
    /**
     * Checks if the given float value is a positive integer.
     *
     * <p>This method checks if the provided float value is greater than zero and if it is 
     * equivalent to its integer representation. It returns {@code true} if both conditions are 
     * met, and {@code false} otherwise.</p>
     *
     * @param f The float value to be checked.
     * @return {@code true} if the float value is a positive integer, {@code false} otherwise.
     */
    public static boolean isPositiveInteger(float f) {
        return f > 0 && f == (int) f;
    }
    /**
     * Checks if the given int value is a positive integer.
     *
     * <p>This method checks if the provided int value is greater than zero. It returns {@code true} 
     * if the value is positive, and {@code false} otherwise.</p>
     *
     * @param i The int value to be checked.
     * @return {@code true} if the int value is a positive integer, {@code false} otherwise.
     */
    public static boolean isPositiveInteger(int i) {
        return i > 0;
    }

    /**
     * Checks if the given string represents a negative integer.
     *
     * <p>This method attempts to parse the provided string as an integer and then checks if the 
     * parsed value is less than zero. It returns {@code true} if the string can be parsed as 
     * an integer and the value is negative, and {@code false} otherwise.</p>
     *
     * <p>If the string is null, empty, or cannot be parsed as an integer, the method returns {@code false}.</p>
     *
     * @param s The string to be checked.
     * @return {@code true} if the string represents a negative integer, {@code false} otherwise.
     */
    public static boolean isNegativeInteger(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        try {
            int value = Integer.parseInt(s);
            return value < 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    /**
     * Checks if the given double value is a negative integer.
     *
     * <p>This method checks if the provided double value is less than zero and if it is 
     * equivalent to its integer representation. It returns {@code true} if both conditions 
     * are met, and {@code false} otherwise.</p>
     *
     * @param d The double value to be checked.
     * @return {@code true} if the double value is a negative integer, {@code false} otherwise.
     */
    public static boolean isNegativeInteger(double d) {
        return d < 0 && d == (int) d;
    }
    /**
     * Checks if the given float value is a negative integer.
     *
     * <p>This method checks if the provided float value is less than zero and if it is 
     * equivalent to its integer representation. It returns {@code true} if both conditions 
     * are met, and {@code false} otherwise.</p>
     *
     * @param f The float value to be checked.
     * @return {@code true} if the float value is a negative integer, {@code false} otherwise.
     */
    public static boolean isNegativeInteger(float f) {
        return f < 0 && f == (int) f;
    }
    /**
     * Checks if the given int value is a negative integer.
     *
     * <p>This method checks if the provided int value is less than zero. It returns {@code true} 
     * if the value is negative, and {@code false} otherwise.</p>
     *
     * @param i The int value to be checked.
     * @return {@code true} if the int value is a negative integer, {@code false} otherwise.
     */
    public static boolean isNegativeInteger(int i) {
        return i < 0;
    }

    private static LocalDate getDate(String date) {
        date = date.split(" ")[0].trim();
        if (date == null || date.isEmpty()) {
            return null;
        }
        DateTimeFormatter[] formatters = new DateTimeFormatter[]{
            DateTimeFormatter.ofPattern("uuuuMMdd").withResolverStyle(ResolverStyle.STRICT),
            DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT),
            DateTimeFormatter.ofPattern("uuuu/MM/dd").withResolverStyle(ResolverStyle.STRICT)
        };
        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDate.parse(date, formatter);
            } catch (DateTimeParseException e) {
            }
        }
        return null;
    }
    /**
     * Checks if the given string represents a valid date in one of the supported formats.
     *
     * <p>This method uses the {@code getDate} helper method to parse the provided string 
     * and determine if it represents a valid date. It supports multiple date formats including 
     * "uuuuMMdd", "uuuu-MM-dd", and "uuuu/MM/dd".</p>
     *
     * <p>The method returns {@code true} if the string can be parsed as a valid date, 
     * and {@code false} otherwise.</p>
     *
     * @param date The string to be checked.
     * @return {@code true} if the string represents a valid date, {@code false} otherwise.
     */
    public static boolean isDate(String date) {
        return getDate(date) != null;
    }

    private static LocalTime getTime(String time) {
        if (time == null || time.isEmpty()) {
            return null;
        }
        DateTimeFormatter[] formatters = new DateTimeFormatter[]{
            DateTimeFormatter.ofPattern("HH:mm:ss").withResolverStyle(ResolverStyle.STRICT),
            DateTimeFormatter.ofPattern("HHmmss").withResolverStyle(ResolverStyle.STRICT)
        };
        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalTime.parse(time, formatter);
            } catch (DateTimeParseException e) {
            }
        }
        return null;
    }
    /**
     * Checks if the given string represents a valid time in one of the supported formats.
     *
     * <p>This method uses the {@code getTime} helper method to parse the provided string 
     * and determine if it represents a valid time. It supports multiple time formats including 
     * "HH:mm:ss" and "HHmmss".</p>
     *
     * <p>The method returns {@code true} if the string can be parsed as a valid time, 
     * and {@code false} otherwise.</p>
     *
     * @param time The string to be checked.
     * @return {@code true} if the string represents a valid time, {@code false} otherwise.
     */
    public static boolean isTime(String time) {
        return getTime(time) != null;
    }

    /**
     * Checks if the given string represents a valid date and time in the supported formats.
     *
     * <p>This method splits the provided string by spaces to separate the date and time parts. 
     * It uses the {@code isDate} method to check if the date part is valid and the {@code isTime} 
     * method to check if the time part is valid.</p>
     *
     * <p>The method returns {@code true} if the string can be parsed as a valid date and optionally 
     * a valid time, and {@code false} otherwise.</p>
     *
     * @param datetime The string to be checked.
     * @return {@code true} if the string represents a valid date and time, {@code false} otherwise.
     */
    public static boolean isDatetime(String datetime) {
        if (datetime == null || datetime.isEmpty()) {
            return false;
        }

        String[] parts = datetime.split(" ");
        if(!isDate(parts[0])) return false;
        if (parts.length == 2) {
            return isTime(parts[1]);
        }
        return true;
    }

    /**
     * Checks if the given string matches the specified mask format.
     *
     * <p>This method compares each character in the provided value against the corresponding 
     * character in the mask. The mask can contain the following special characters:</p>
     * <ul>
     *     <li>'A' - Matches any uppercase alphabetic character (A-Z).</li>
     * <li>'a' - Matches any lowercase alphabetic character (a-z).</li>
     *     <li>'9' - Matches any numeric digit (0-9).</li>
     *     <li>Any other character - Must match exactly.</li>
     * </ul>
     *
     * <p>The method returns {@code true} if the value matches the mask format, and {@code false} 
     * otherwise. If the value or mask is {@code null}, or if their lengths do not match, the method 
     * returns {@code false}.</p>
     *
     * @param value The string to be checked.
     * @param mask The mask format to check against.
     * @return {@code true} if the value matches the mask format, {@code false} otherwise.
     */
    public static boolean isValidMask(String value, String mask) {
        if (value == null || mask == null || value.length() != mask.length()) {
            return false;
        }
        for (int i = 0; i < value.length(); i++) {
            char charAtStr = value.charAt(i);
            char charAtMask = mask.charAt(i);

            switch (charAtMask) {
                case 'A':
                    if (charAtStr < 'A' || charAtStr > 'Z') return false;
                    break;
                case 'a':
                    if (charAtStr < 'a' || charAtStr > 'z') return false;
                    break;
                case '9':
                    if (!Character.isDigit(charAtStr)) return false;
                    break;
                default:
                    if (charAtStr != charAtMask) return false;
            }
        }
        return true;
    }

    /**********************************************************************
     * for Date
     **********************************************************************/
    private static LocalDateTime getDatetime(String datetime) {
        String[] datetimeArr = datetime.split(" ");
        if (datetimeArr.length > 2) {
            return null;
        }

        LocalDate date = getDate(datetimeArr[0]);
        LocalTime time;
        if(datetimeArr.length > 1) {
            time = getTime(datetimeArr[1]);
        }
        else {
            time = LocalTime.of(0, 0, 0);
        }

        if (date == null || time == null) {
            return null;
        }

        return LocalDateTime.of(date, time);
    }
    /**
     * Adds the specified amount of time to the given date and returns the result as a string.
     *
     * <p>This method adds the specified amount of time to the given date using the default add type 
     * (e.g., days). The amount of time to add is specified as a string. This method calls 
     * {@code addDate(String datetime, String addValue, String addType)} with an empty add type 
     * to use the default add type.</p>
     *
     * @param datetime The original datetime string.
     * @param addValue The amount of time to add, specified as a string.
     * @return The resulting datetime string after the addition.
     */
    public static String addDate(String datetime, String addValue) {
        return addDate(datetime, addValue, "");
    }
    /**
     * Adds the specified amount of time to the given date and returns the result as a string.
     *
     * <p>This method adds the specified amount of time to the given date using the specified add type 
     * (e.g., days, months, years). The amount of time to add is specified as a string. This method calls 
     * {@code addDate(String datetime, String addValue, String addType, String format)} with an empty format 
     * to use the default format.</p>
     *
     * @param datetime The original datetime string.
     * @param addValue The amount of time to add, specified as a string.
     * @param addType The type of time unit to add (e.g., "d" for days, "M" for months, "y" for years).
     * @return The resulting datetime string after the addition.
     */
    public static String addDate(String datetime, String addValue, String addType) {
        return addDate(datetime, addValue, addType, "");
    }
    /**
     * Adds the specified amount of time to the given date and returns the result as a string in the specified format.
     *
     * <p>This method adds the specified amount of time to the given date using the specified add type 
     * (e.g., days, months, years). The amount of time to add is specified as a string. If the add value is not 
     * a valid integer, a {@code UtilsException} is thrown. This method calls 
     * {@code addDate(String datetime, int addValue, String addType, String format)} with the parsed integer 
     * add value.</p>
     *
     * @param datetime The original datetime string.
     * @param addValue The amount of time to add, specified as a string.
     * @param addType The type of time unit to add (e.g., "d" for days, "M" for months, "y" for years).
     * @param format The format of the resulting datetime string.
     * @return The resulting datetime string after the addition.
     * @throws UtilsException If the add value is not a valid integer.
     */
    public static String addDate(String datetime, String addValue, String addType, String format) {
        if(!isInteger(addValue)) {
            throw new UtilsException("Please enter a valid addValue(Integer).");
        }
        int add = Integer.parseInt(addValue);
        return addDate(datetime, add, addType, format);
    }
    /**
     * Adds the specified amount of time to the given date and returns the result as a string.
     *
     * <p>This method adds the specified amount of time to the given date using the default add type 
     * (e.g., days). The amount of time to add is specified as an integer. This method calls 
     * {@code addDate(String datetime, int addValue, String addType)} with an empty add type 
     * to use the default add type.</p>
     *
     * @param datetime The original datetime string.
     * @param addValue The amount of time to add, specified as an integer.
     * @return The resulting datetime string after the addition.
     */
    public static String addDate(String datetime, int addValue) {
        return addDate(datetime, addValue, "");
    }
    /**
     * Adds the specified amount of time to the given date and returns the result as a string.
     *
     * <p>This method adds the specified amount of time to the given date using the specified add type 
     * (e.g., days, months, years). The amount of time to add is specified as an integer. This method calls 
     * {@code addDate(String datetime, int addValue, String addType, String format)} with an empty format 
     * to use the default format.</p>
     *
     * @param datetime The original datetime string.
     * @param addValue The amount of time to add, specified as an integer.
     * @param addType The type of time unit to add (e.g., "d" for days, "M" for months, "y" for years).
     * @return The resulting datetime string after the addition.
     */
    public static String addDate(String datetime, int addValue, String addType) {
        return addDate(datetime, addValue, addType, "");
    }
    /**
     * Adds the specified amount of time to the given date and returns the result as a string in the specified format.
     *
     * <p>This method adds the specified amount of time to the given date using the specified add type 
     * (e.g., years, months, days, hours, minutes, seconds). The amount of time to add is specified as an integer. 
     * If the add type is not provided, the default add type is used. The resulting datetime is formatted 
     * according to the specified format or the default format if none is provided.</p>
     *
     * <p>If the datetime string cannot be parsed, a {@code UtilsException} is thrown. The method supports 
     * various add types including:</p>
     * <ul>
     *     <li>"y" - Years</li>
     *     <li>"M" - Months</li>
     *     <li>"d" - Days</li>
     *     <li>"h" - Hours</li>
     *     <li>"m" - Minutes</li>
     *     <li>"s" - Seconds</li>
     * </ul>
     *
     * @param datetime The original datetime string.
     * @param addValue The amount of time to add, specified as an integer.
     * @param addType The type of time unit to add (e.g., "y" for years, "M" for months, "d" for days, "h" for hours, "m" for minutes, "s" for seconds).
     * @param format The format of the resulting datetime string.
     * @return The resulting datetime string after the addition.
     * @throws UtilsException If the datetime string cannot be parsed or if an invalid add type is provided.
     */
    public static String addDate(String datetime, int addValue, String addType, String format) {
        LocalDateTime dt = getDatetime(datetime);
        if(dt == null) {
            throw new UtilsException("Please enter a valid date.");
        }
        long add = addValue;
        if(addType == null || "".equals(addType)) addType = ADD_TYPE;

        switch (addType) {
            case "y":
                dt = dt.plusYears(add);
                break;
            case "M":
                dt = dt.plusMonths(add);
                break;
            case "d":
                dt = dt.plusDays(add);
                break;
            case "h":
                dt = dt.plusHours(add);
                break;
            case "m":
                dt = dt.plusMinutes(add);
                break;
            case "s":
                dt = dt.plusSeconds(add);
                break;
            default:
                throw new UtilsException("Please enter a valid addType.(y, M, d, h, m, s)");
        }

        if((format == null || "".equals(format))) {
            if (datetime.split(" ").length > 1) {
                format = DATETIME_FORMATTER;
            }
            else {
                format = DATE_FORMATTER;
            }
        }
        return getDatetimeWithFormat(dt, format);
    }

    /**
     * Calculates the difference between two datetime strings and returns the result as an integer.
     *
     * <p>This method calculates the difference between two datetime strings using the default difference type 
     * (e.g., days). It calls {@code getDateDiff(String datetime1, String datetime2, String diffType)} with an empty 
     * difference type to use the default difference type.</p>
     *
     * @param datetime1 The first datetime string.
     * @param datetime2 The second datetime string.
     * @return The difference between the two datetime strings as an integer.
     */
    public static int getDateDiff(String datetime1, String datetime2) {
        return getDateDiff(datetime1, datetime2, "");
    }
    /**
     * Calculates the difference between two datetime strings using the specified difference type and returns the result as an integer.
     *
     * <p>This method calculates the difference between two datetime strings using the specified difference type 
     * (e.g., years, months, days, hours, minutes, seconds). If the difference type is not provided, the default 
     * difference type is used.</p>
     *
     * <p>If either of the datetime strings cannot be parsed, a {@code UtilsException} is thrown. The method supports 
     * various difference types including:</p>
     * <ul>
     *     <li>"y" - Years</li>
     *     <li>"M" - Months</li>
     *     <li>"d" - Days</li>
     *     <li>"h" - Hours</li>
     *     <li>"m" - Minutes</li>
     *     <li>"s" - Seconds</li>
     * </ul>
     *
     * @param datetime1 The first datetime string.
     * @param datetime2 The second datetime string.
     * @param diffType The type of time unit to calculate the difference (e.g., "y" for years, "M" for months, "d" for days, "h" for hours, "m" for minutes, "s" for seconds).
     * @return The difference between the two datetime strings as an integer.
     * @throws UtilsException If either of the datetime strings cannot be parsed.
     * @throws IllegalArgumentException If an invalid difference type is provided.
     */
    public static int getDateDiff(String datetime1, String datetime2, String diffType) {
        LocalDateTime dt1 = getDatetime(datetime1);
        LocalDateTime dt2 = getDatetime(datetime2);
        if(dt1 == null || dt2 == null) {
            throw new UtilsException("Please enter a valid date.");
        }
        if(diffType == null || "".equals(diffType)) diffType = DIFF_TYPE;
        long result;

        switch (diffType) {
            case "y":
                result = ChronoUnit.YEARS.between(dt1, dt2);
                break;
            case "M":
                result =  ChronoUnit.MONTHS.between(dt1, dt2);
                break;
            case "d":
                result =  ChronoUnit.DAYS.between(dt1, dt2);
                break;
            case "h":
                result =  ChronoUnit.HOURS.between(dt1, dt2);
                break;
            case "m":
                result =  ChronoUnit.MINUTES.between(dt1, dt2);
                break;
            case "s":
                result =  ChronoUnit.SECONDS.between(dt1, dt2);
                break;
            default:
                throw new IllegalArgumentException("Please enter a valid diffType.(y, M, d, h, m, s)");
        }
        return Long.valueOf(result).intValue();
    }

    /**
     * Returns the month name for the given month string.
     *
     * <p>This method retrieves the month name for the provided month string. 
     * It calls {@code getMonthName(String month, boolean isFullName)} with {@code true} 
     * to return the full month name.</p>
     *
     * @param month The month string (1-12) for which the name is to be retrieved.
     * @return The full name of the month.
     * @throws UtilsException If the provided month string is not a valid integer.
     */
    public static String getMonthName(String month) {
        return getMonthName(month, true);
    }
    /**
     * Returns the month name for the given month string, with an option to return the full name or short name.
     *
     * <p>This method retrieves the month name for the provided month string. 
     * It converts the string to an integer and then calls {@code getMonthName(int month, boolean isFullName)} 
     * to return either the full month name or the short month name based on the {@code isFullName} parameter.</p>
     *
     * @param month The month string (1-12) for which the name is to be retrieved.
     * @param isFullName If {@code true}, returns the full month name; if {@code false}, returns the short month name.
     * @return The name of the month, either full or short.
     * @throws UtilsException If the provided month string is not a valid integer.
     */
    public static String getMonthName(String month, boolean isFullName) {
        if(!isInteger(month)) {
            throw new UtilsException("Please enter a valid month.");
        }
        int mon = Integer.parseInt(month);

        return getMonthName(mon, isFullName);
    }
    /**
     * Returns the month name for the given month integer.
     *
     * <p>This method retrieves the month name for the provided month integer. 
     * It calls {@code getMonthName(int month, boolean isFullName)} with {@code true} 
     * to return the full month name.</p>
     *
     * @param month The month integer (1-12) for which the name is to be retrieved.
     * @return The full name of the month.
     * @throws UtilsException If the provided month integer is not in the range 1-12.
     */
    public static String getMonthName(int month) {
        return getMonthName(month, true);
    }
    /**
     * Returns the month name for the given month integer, with an option to return the full name or short name.
     *
     * <p>This method retrieves the month name for the provided month integer. 
     * If the month integer is not in the range 1-12, a {@code UtilsException} is thrown. 
     * The method uses {@code TextStyle.FULL} or {@code TextStyle.SHORT} based on the {@code isFullName} parameter 
     * to return either the full month name or the short month name.</p>
     *
     * @param month The month integer (1-12) for which the name is to be retrieved.
     * @param isFullName If {@code true}, returns the full month name; if {@code false}, returns the short month name.
     * @return The name of the month, either full or short.
     * @throws UtilsException If the provided month integer is not in the range 1-12.
     */
    public static String getMonthName(int month, boolean isFullName) {
        if (month < 1 || month > 12) {
            throw new UtilsException("Please enter a valid month.");
        }

        Month m = Month.of(month);
        TextStyle style = isFullName ? TextStyle.FULL : TextStyle.SHORT;
        return m.getDisplayName(style, Locale.ENGLISH);
    }

    /**
     * Returns the given date string formatted according to the specified format.
     *
     * <p>This method formats the provided date string using the specified format. 
     * It calls {@code getDateWithFormat(String date, String format)} with an empty format 
     * to use the default date format.</p>
     *
     * @param date The date string to be formatted.
     * @return The formatted date string.
     * @throws UtilsException If the provided date string is not valid.
     */
    public static String getDateWithFormat(String date) {
        return getDateWithFormat(date, "");
    }
    /**
     * Returns the given date string formatted according to the specified format.
     *
     * <p>This method formats the provided date string using the specified format. 
     * It first converts the date string to a {@code LocalDate} object using the {@code getDate} method. 
     * If the format is not provided or is empty, the default date format is used.</p>
     *
     * @param date The date string to be formatted.
     * @param format The format to apply to the date string.
     * @return The formatted date string.
     * @throws UtilsException If the provided date string is not valid.
     */
    public static String getDateWithFormat(String date, String format) {
        return getDateWithFormat(getDate(date), format);
    }
    /**
     * Returns the given LocalDate formatted according to the specified format.
     *
     * <p>This method formats the provided {@code LocalDate} using the specified format. 
     * It calls {@code getDateWithFormat(LocalDate date, String format)} with an empty format 
     * to use the default date format.</p>
     *
     * @param date The {@code LocalDate} to be formatted.
     * @return The formatted date string.
     * @throws UtilsException If the provided date is not valid.
     */
    public static String getDateWithFormat(LocalDate date) {
        return getDateWithFormat(date, "");
    }
    /**
     * Returns the given LocalDate formatted according to the specified format.
     *
     * <p>This method formats the provided {@code LocalDate} using the specified format. 
     * If the format is not provided or is empty, the default date format is used. 
     * If the {@code LocalDate} is {@code null}, a {@code UtilsException} is thrown. 
     * If the format is invalid, a {@code UtilsException} is thrown.</p>
     *
     * @param date The {@code LocalDate} to be formatted.
     * @param format The format to apply to the date.
     * @return The formatted date string.
     * @throws UtilsException If the provided date is {@code null} or if the format is invalid.
     */
    public static String getDateWithFormat(LocalDate date, String format) {
        if(date == null) throw new UtilsException("Please enter a valid date.");
        if("".equals(format) || format == null) format = DATE_FORMATTER;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return date.format(formatter);
        } catch (DateTimeParseException e) {
            throw new UtilsException("Invalid format.");
        }
    }

    /**
     * Returns the given datetime string formatted according to the specified format.
     *
     * <p>This method formats the provided datetime string using the specified format. 
     * It calls {@code getDatetimeWithFormat(String datetime, String format)} with an empty format 
     * to use the default datetime format.</p>
     *
     * @param datetime The datetime string to be formatted.
     * @return The formatted datetime string.
     * @throws UtilsException If the provided datetime string is not valid.
     */
    public static String getDatetimeWithFormat(String datetime) {
        return getDatetimeWithFormat(datetime, "");
    }
    /**
     * Returns the given datetime string formatted according to the specified format.
     *
     * <p>This method formats the provided datetime string using the specified format. 
     * It first converts the datetime string to a {@code LocalDateTime} object using the {@code getDatetime} method. 
     * If the format is not provided or is empty, the default datetime format is used.</p>
     *
     * @param datetime The datetime string to be formatted.
     * @param format The format to apply to the datetime string.
     * @return The formatted datetime string.
     * @throws UtilsException If the provided datetime string is not valid.
     */
    public static String getDatetimeWithFormat(String datetime, String format) {
        return getDatetimeWithFormat(getDatetime(datetime), format);
    }
    /**
     * Returns the given LocalDateTime formatted according to the specified format.
     *
     * <p>This method formats the provided {@code LocalDateTime} using the specified format. 
     * It calls {@code getDatetimeWithFormat(LocalDateTime datetime, String format)} with an empty format 
     * to use the default datetime format.</p>
     *
     * @param datetime The {@code LocalDateTime} to be formatted.
     * @return The formatted datetime string.
     * @throws UtilsException If the provided datetime is not valid.
     */
    public static String getDatetimeWithFormat(LocalDateTime datetime) {
        return getDatetimeWithFormat(datetime, "");
    }
    /**
     * Returns the given LocalDateTime formatted according to the specified format.
     *
     * <p>This method formats the provided {@code LocalDateTime} using the specified format. 
     * If the format is not provided or is empty, the default datetime format is used. 
     * If the {@code LocalDateTime} is {@code null}, a {@code UtilsException} is thrown. 
     * If the format is invalid, a {@code UtilsException} is thrown.</p>
     *
     * @param datetime The {@code LocalDateTime} to be formatted.
     * @param format The format to apply to the datetime.
     * @return The formatted datetime string.
     * @throws UtilsException If the provided datetime is {@code null} or if the format is invalid.
     */
    public static String getDatetimeWithFormat(LocalDateTime datetime, String format) {
        if(datetime == null) throw new UtilsException("Please enter a valid date.");
        if("".equals(format) || format == null) format = DATETIME_FORMATTER;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return datetime.format(formatter);
        } catch (DateTimeParseException e) {
            throw new UtilsException("Invalid format.");
        }
    }

    /**
     * Returns the day of the week for the given date string.
     *
     * <p>This method retrieves the day of the week for the provided date string. 
     * It calls {@code getDayOfWeek(String date, String dayOfWeekType)} with an empty 
     * day of the week type to use the default day of the week type.</p>
     *
     * @param date The date string for which the day of the week is to be retrieved.
     * @return The day of the week as a string.
     * @throws UtilsException If the provided date string is not valid.
     */
    public static String getDayOfWeek(String date) {
        return getDayOfWeek(date, "");
    }
    /**
     * Returns the day of the week for the given date string, formatted according to the specified day of the week type.
     *
     * <p>This method retrieves the day of the week for the provided date string. 
     * It first converts the date string to a {@code LocalDate} object using the {@code getDate} method. 
     * The day of the week is then formatted according to the specified day of the week type. 
     * If the day of the week type is not provided or is empty, the default day of the week type is used.</p>
     *
     * @param date The date string for which the day of the week is to be retrieved.
     * @param dayOfWeekType The format to apply to the day of the week (e.g., "d" for numeric, "dy" for short name, "day" for full name).
     * @return The day of the week as a string.
     * @throws UtilsException If the provided date string is not valid.
     */
    public static String getDayOfWeek(String date, String dayOfWeekType) {
        return getDayOfWeek(getDate(date), dayOfWeekType);
    }
    /**
     * Returns the day of the week for the given LocalDate, formatted according to the default day of the week type.
     *
     * <p>This method retrieves the day of the week for the provided {@code LocalDate}. 
     * It calls {@code getDayOfWeek(LocalDate date, String dayOfWeekType)} with an empty 
     * day of the week type to use the default day of the week type.</p>
     *
     * @param date The {@code LocalDate} for which the day of the week is to be retrieved.
     * @return The day of the week as a string.
     * @throws UtilsException If the provided date is not valid.
     */
    public static String getDayOfWeek(LocalDate date) {
        return getDayOfWeek(date, "");
    }
    /**
     * Returns the day of the week for the given LocalDate, formatted according to the specified day of the week type.
     *
     * <p>This method retrieves the day of the week for the provided {@code LocalDate}. 
     * The day of the week is formatted according to the specified day of the week type. 
     * If the day of the week type is not provided or is empty, the default day of the week type is used. 
     * If the {@code LocalDate} is {@code null}, a {@code UtilsException} is thrown. 
     * If an invalid day of the week type is provided, a {@code UtilsException} is thrown.</p>
     *
     * <p>The method supports various day of the week types including:</p>
     * <ul>
     *     <li>"d" - Numeric (1-7)</li>
     *     <li>"dy" - Short name (e.g., Mon, Tue)</li>
     *     <li>"day" - Full name (e.g., Monday, Tuesday)</li>
     *     <li>"kdy" - Short name in Korean (e.g., 월, 화)</li>
     *     <li>"kday" - Full name in Korean (e.g., 월요일, 화요일)</li>
     * </ul>
     *
     * @param date The {@code LocalDate} for which the day of the week is to be retrieved.
     * @param dayOfWeekType The format to apply to the day of the week (e.g., "d" for numeric, "dy" for short name, "day" for full name).
     * @return The day of the week as a string.
     * @throws UtilsException If the provided date is {@code null} or if an invalid day of the week type is provided.
     */
    public static String getDayOfWeek(LocalDate date, String dayOfWeekType) {
        if (date == null) {
            throw new UtilsException("Please enter a valid date.");
        }
        if(dayOfWeekType == null || "".equals(dayOfWeekType)) dayOfWeekType = DAY_OF_WEEK_TYPE;
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        
        switch (dayOfWeekType.toLowerCase()) {
            case "d":
                return "" + dayOfWeek.getValue();
            case "dy":
                return dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.US);
            case "day":
                return dayOfWeek.getDisplayName(TextStyle.FULL, Locale.US);
            case "kdy":
                return dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN);
            case "kday":
                return dayOfWeek.getDisplayName(TextStyle.FULL, Locale.KOREAN);
            default:
            throw new UtilsException("Please enter a valid dayType.(d, dy, day, kdy, kday)");
        }
    }

    /**
     * Returns the last day of the month for the given LocalDate.
     *
     * <p>This method retrieves the last day of the month for the provided {@code LocalDate}. 
     * If the {@code LocalDate} is {@code null}, a {@code UtilsException} is thrown.</p>
     *
     * @param date The {@code LocalDate} for which the last day of the month is to be retrieved.
     * @return The last day of the month as an integer.
     * @throws UtilsException If the provided date is {@code null}.
     */
    public static int getLastDay(LocalDate date) {
        if (date == null) {
            throw new UtilsException("Please enter a valid date.");
        }
        return date.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
    }
    /**
     * Returns the last day of the month for the given year and month string.
     *
     * <p>This method retrieves the last day of the month for the provided year and month string. 
     * It first attempts to parse the string directly into a {@code LocalDate}. If that fails, it 
     * appends a day part ("01") to the string and tries to parse it again. If the year and month 
     * string is longer than 6 characters, it checks for date separators ("/" or "-") and appends 
     * the day part accordingly.</p>
     *
     * <p>If the resulting date cannot be parsed, a {@code UtilsException} is thrown.</p>
     *
     * @param yearMonth The year and month string for which the last day of the month is to be retrieved.
     * @return The last day of the month as an integer.
     * @throws UtilsException If the provided year and month string is not valid.
     */
    public static int getLastDay(String yearMonth) {
        LocalDate localDate = getDate(yearMonth);
        if(localDate != null) {
            return getLastDay(localDate);
        }

        String date = yearMonth;
        if(yearMonth.length() > 6) {
            if(yearMonth.contains("/")) {
                date += "/01";
            } else if(yearMonth.contains("-")) {
                date += "-01";
            }
        }
        else {
            date += "01";
        }
        return getLastDay(getDate(date));
    }

    /**
     * Returns the current system date and time formatted according to the default datetime format.
     *
     * <p>This method retrieves the current system date and time and formats it using the default 
     * datetime format. It calls {@code getSysDatetime(String format)} with an empty format to use 
     * the default datetime format.</p>
     *
     * @return The current system date and time as a formatted string.
     */
    public static String getSysDatetime() {
        return getSysDatetime("");
    }
    /**
     * Returns the current system date and time formatted according to the specified format.
     *
     * <p>This method retrieves the current system date and time and formats it using the specified 
     * format. If the format is not provided or is empty, the default datetime format is used.</p>
     *
     * @param format The format to apply to the current system date and time.
     * @return The current system date and time as a formatted string.
     */
    public static String getSysDatetime(String format) {
        if("".equals(format) || format == null) format = DATETIME_FORMATTER;
        return getDatetimeWithFormat(LocalDateTime.now(), format);
    }

    /**
     * Returns the current day of the week formatted according to the default day of the week type.
     *
     * <p>This method retrieves the current day of the week and formats it using the default 
     * day of the week type. It calls {@code getSysDayOfWeek(String dayType)} with an empty 
     * day of the week type to use the default format.</p>
     *
     * @return The current day of the week as a formatted string.
     */
    public static String getSysDayOfWeek() {
        return getSysDayOfWeek("");
    }
    /**
     * Returns the current day of the week formatted according to the specified day of the week type.
     *
     * <p>This method retrieves the current day of the week and formats it using the specified 
     * day of the week type. If the day of the week type is not provided or is empty, the default 
     * day of the week type is used.</p>
     *
     * @param dayType The format to apply to the current day of the week (e.g., "d" for numeric, "dy" for short name, "day" for full name).
     * @return The current day of the week as a formatted string.
     */
    public static String getSysDayOfWeek(String dayType) {
        return getDayOfWeek(LocalDate.now(), dayType);
    }

    /**********************************************************************
     * for Number
     **********************************************************************/
    // 파라메터 값을 파라메터 지정한 위치로 올림한 값을 반환한다.
    public static double getCeil(String num) {
        if(num == null || !isNumeric(num)) {
            throw new UtilsException("Please enter a valid number.");
        }
        return getCeil(Double.parseDouble(num), 0);
    }
    public static double getCeil(String num, int precision) {
        if(num == null || !isNumeric(num)) {
            throw new UtilsException("Please enter a valid number.");
        }
        return getCeil(Double.parseDouble(num), precision);
    }
    public static double getCeil(int num) {
        return getCeil((double) num, 0);
    }
    public static double getCeil(int num, int precision) {
        return getCeil((double) num, precision);
    }
    public static double getCeil(long num) {
        return getCeil((double) num, 0);
    }
    public static double getCeil(long num, int precision) {
        return getCeil((double) num, precision);
    }
    public static double getCeil(float num) {
        return getCeil((double) num, 0);
    }
    public static double getCeil(float num, int precision) {
        return getCeil((double) num, precision);
    }
    public static double getCeil(double num) {
        return getCeil(num, 0);
    }
    public static double getCeil(double num, int precision) {
        double factor = Math.pow(10, precision);
        return Math.ceil(num * factor) / factor;
    }

    // 파라메터 값을 파라메터 지정한 위치로 내림한 값을 반환한다.
    public static double getFloor(String num) {
        if(num == null || !isNumeric(num)) {
            throw new UtilsException("Please enter a valid number.");
        }
        return getFloor(Double.parseDouble(num), 0);
    }
    public static double getFloor(String num, int precision) {
        if(num == null || !isNumeric(num)) {
            throw new UtilsException("Please enter a valid number.");
        }
        return getFloor(Double.parseDouble(num), precision);
    }
    public static double getFloor(int num) {
        return getFloor((double) num, 0);
    }
    public static double getFloor(int num, int precision) {
        return getFloor((double) num, precision);
    }
    public static double getFloor(long num) {
        return getFloor((double) num, 0);
    }
    public static double getFloor(long num, int precision) {
        return getFloor((double) num, precision);
    }
    public static double getFloor(float num) {
        return getFloor((double) num, 0);
    }
    public static double getFloor(float num, int precision) {
        return getFloor((double) num, precision);
    }
    public static double getFloor(double num) {
        return getFloor(num, 0);
    }
    public static double getFloor(double num, int precision) {
        double factor = Math.pow(10, precision);
        return Math.floor(num * factor) / factor;
    }

    // 파라메터 값을 파라메터 지정한 위치로 반올림한 값을 반환한다.
    public static double getRound(String num) {
        if(num == null || !isNumeric(num)) {
            throw new UtilsException("Please enter a valid number.");
        }
        return getRound(Double.parseDouble(num), 0);
    }
    public static double getRound(String num, int precision) {
        if(num == null || !isNumeric(num)) {
            throw new UtilsException("Please enter a valid number.");
        }
        return getRound(Double.parseDouble(num), precision);
    }
    public static double getRound(int num) {
        return getRound((double) num, 0);
    }
    public static double getRound(int num, int precision) {
        return getRound((double) num, precision);
    }
    public static double getRound(long num) {
        return getRound((double) num, 0);
    }
    public static double getRound(long num, int precision) {
        return getRound((double) num, precision);
    }
    public static double getRound(float num) {
        return getRound((double) num, 0);
    }
    public static double getRound(float num, int precision) {
        return getRound((double) num, precision);
    }
    public static double getRound(double num) {
        return getRound(num, 0);
    }
    public static double getRound(double num, int precision) {
        double factor = Math.pow(10, precision);
        return Math.round(num * factor) / factor;
    }

    // 파라메터 값을 파라메터 지정한 위치로 버림한 값을 반환한다.
    public static double getTrunc(String num) {
        if(num == null || !isNumeric(num)) {
            throw new UtilsException("Please enter a valid number.");
        }
        return getTrunc(Double.parseDouble(num), 0);
    }
    public static double getTrunc(String num, int precision) {
        if(num == null || !isNumeric(num)) {
            throw new UtilsException("Please enter a valid number.");
        }
        return getTrunc(Double.parseDouble(num), precision);
    }
    public static double getTrunc(int num) {
        return getTrunc((double) num, 0);
    }
    public static double getTrunc(int num, int precision) {
        return getTrunc((double) num, precision);
    }
    public static double getTrunc(long num) {
        return getTrunc((double) num, 0);
    }
    public static double getTrunc(long num, int precision) {
        return getTrunc((double) num, precision);
    }
    public static double getTrunc(float num) {
        return getTrunc((double) num, 0);
    }
    public static double getTrunc(float num, int precision) {
        return getTrunc((double) num, precision);
    }
    public static double getTrunc(double num) {
        return getTrunc(num, 0);
    }
    public static double getTrunc(double num, int precision) {
        double factor = Math.pow(10, precision);
        return Math.floor(num * factor) / factor;
    }

    /**********************************************************************
     * for String
     **********************************************************************/
    // 문자열의 Byte길이를 반환한다.
    public static int getByteLength(String str) {
        if (str == null) return 0;

        int byteLength = 0;
        for (int i = 0; i < str.length(); i++) {
            int charCode = str.codePointAt(i);

            if (charCode <= 0x7F) {
                byteLength += 1;
            } else if (charCode <= 0x7FF) {
                byteLength += LESSOREQ_0X7FF_BYTE;
            } else if (charCode <= 0xFFFF) {
                byteLength += LESSOREQ_0XFFFF_BYTE;
            } else {
                byteLength += GREATER_0XFFFF_BYTE;
            }
        }
        return byteLength;
    }

    // 파라메터 문자열을 파라메터 Byte로 자른 값을 반환한다.
    public static String getCutByteLength(String str, int cutByte) {
        if (str == null) return "";
        int byteLength = 0;
        int cutIndex = str.length();

        for (int i = 0; i < str.length(); i++) {
            int charCode = str.codePointAt(i);

            if (charCode <= 0x7F) {
                byteLength += 1;
            } else if (charCode <= 0x7FF) {
                byteLength += LESSOREQ_0X7FF_BYTE;
            } else if (charCode <= 0xFFFF) {
                byteLength += LESSOREQ_0XFFFF_BYTE;
            } else {
                byteLength += GREATER_0XFFFF_BYTE;
            }

            if (byteLength > cutByte) {
                cutIndex = i;
                break;
            }
        }
        return str.substring(0, cutIndex);
    }

    // 파라메터 문자열에 사이사이 일정한 공백을 추가하여 길이 규격을 맞춘 값을 반환한다.
    public static String getStringLenForm(String str, int length) {
        if (str == null) return null;
        int strLength = str.length();
        if (strLength >= length) {
            return str;
        }

        int totalSpaces = length - strLength;
        int gaps = strLength - 1;
        int spacePerGap = Math.floorDiv(totalSpaces, gaps);
        int extraSpaces = totalSpaces % gaps;

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < gaps; i++) {
            result.append(str.charAt(i));
            int spacesToAdd = spacePerGap + (i < extraSpaces ? 1 : 0);
            for (int j = 0; j < spacesToAdd; j++) {
                result.append(' ');
            }
        }
        result.append(str.charAt(strLength - 1));
        return result.toString();
    }

    //파라메터 문자열 왼쪽에 파라메터 특정 문자를 반복해 채운 값을 반환한다.
    public static String getLpad(String str, String padStr, int length) {
        if (str == null) str = "";
        if (padStr == null || padStr.isEmpty()) throw new IllegalArgumentException("Pad string cannot be null or empty");

        int padLength = length - str.length();
        if (padLength <= 0) {
            return str;
        }

        StringBuilder pad = new StringBuilder();
        while (pad.length() < padLength) {
            pad.append(padStr);
        }

        return pad.substring(0, padLength) + str;
    }

    //파라메터 문자열 오른쪽에 파라메터 특정 문자를 반복해 채운 값을 반환한다.
    public static String getRpad(String str, String padStr, int length) {
        if (str == null) str = "";
        if (padStr == null || padStr.isEmpty()) throw new IllegalArgumentException("Pad string cannot be null or empty");

        int padLength = length - str.length();
        if (padLength <= 0) {
            return str;
        }

        StringBuilder pad = new StringBuilder();
        while (pad.length() < padLength) {
            pad.append(padStr);
        }

        return str + pad.substring(0, padLength);
    }

    //값이 null인 경우 지정된 값을 반환한다.
    public static <T> T nvl(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static String getNumberFormat(int value) {
        return getNumberFormat(String.valueOf(value), "");
    }
    public static String getNumberFormat(int value, String format) {
        return getNumberFormat(String.valueOf(value), format);
    }
    public static String getNumberFormat(long value) {
        return getNumberFormat(String.valueOf(value), "");
    }
    public static String getNumberFormat(long value, String format) {
        return getNumberFormat(String.valueOf(value), format);
    }
    public static String getNumberFormat(float value) {
        return getNumberFormat(String.valueOf(value), "");
    }
    public static String getNumberFormat(float value, String format) {
        return getNumberFormat(String.valueOf(value), format);
    }
    public static String getNumberFormat(double value) {
        return getNumberFormat(String.valueOf(value), "");
    }
    public static String getNumberFormat(double value, String format) {
        return getNumberFormat(String.valueOf(value), format);
    }
    public static String getNumberFormat(String value) {
        return getNumberFormat(value, "");
    }
    //파라메터 값을 파라메터 숫자 형식으로 변환한 값을 반환한다.
    public static String getNumberFormat(String value, String format) {
        Pattern pattern = Pattern.compile("^(.*?)([#0,.]+)(.*?)$");
        if(format == null || "".equals(format)) format = NUMBER_FORMATTER;
        Matcher matcher = pattern.matcher(format);
    
        if (!matcher.find()) {
            throw new IllegalArgumentException("Invalid format");
        }
    
        String result = "";
        String prefix = matcher.group(1);
        String numberFormat = matcher.group(2);
        String suffix = matcher.group(3);
    
        String[] parts = numberFormat.split("\\.");
        String integerPartFormat = parts[0];
        String decimalPartFormat = parts.length > 1 ? parts[1] : "";
    
        if (decimalPartFormat.contains("#") && decimalPartFormat.contains("0")) {
            throw new IllegalArgumentException("Invalid format: Mixed '#' and '0' in decimal format");
        }
    
        boolean isNull = false;
        boolean doZeroByNull = "0".equals(integerPartFormat.substring(integerPartFormat.length() - 1));
        if("".equals(value) || value == null) {
            isNull = true;
            value = "0";
        }
        BigDecimal number = new BigDecimal(value);
        if (suffix.trim().equals("%")) {
            number = number.multiply(BigDecimal.valueOf(100));
        }
    
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
        String formattedInteger = formatIntegerPart(number, integerPartFormat, symbols);
    
        // 소수 부분 포맷팅 로직을 수정합니다.
        String formattedDecimal = formatDecimalPart(number, decimalPartFormat, decimalPartFormat.length());
    
        if(isNull && !doZeroByNull) {
            result = prefix + "" + (formattedDecimal.isEmpty() ? "" : "." + formattedDecimal) + suffix;
        } else {
            result = prefix + formattedInteger + (formattedDecimal.isEmpty() ? "" : "." + formattedDecimal) + suffix;
        }
    
        return result;
    }

    private static String formatIntegerPart(BigDecimal number, String format, DecimalFormatSymbols symbols) {
        DecimalFormat decimalFormat = new DecimalFormat(format, symbols);
        decimalFormat.setGroupingUsed(format.contains(","));
        // decimalFormat.setMinimumIntegerDigits(format.startsWith("0") ? 1 : 0);
        decimalFormat.setMaximumFractionDigits(0);  // 소수점 없음
        return decimalFormat.format(number);
    }

    private static String formatDecimalPart(BigDecimal number, String format, int scale) {
        if (format.isEmpty()) return "";

        number = number.setScale(scale, RoundingMode.HALF_UP); // 소수점 아래 반올림
        String numberAsString = number.toPlainString();

        int dotIndex = numberAsString.indexOf('.');
        String decimalPart = dotIndex != -1 ? numberAsString.substring(dotIndex + 1) : "";

        if (format.contains("#")) {
            // 불필요한 0 제거
            while (decimalPart.endsWith("0")) {
                decimalPart = decimalPart.substring(0, decimalPart.length() - 1);
            }
            return decimalPart;
        } else if (format.contains("0")) {
            // 필요한 경우 0으로 채움
            return String.format("%-" + scale + "s", decimalPart).replace(' ', '0');
        }
        return decimalPart;
    }

    //파라메터 문자열의 숫자를 제외한 모든 문자를 제거한 값을 반환한다.
    public static String getRemoveExceptNumbers(String str) {
        if (str == null) return "";
        return str.replaceAll("[^0-9]", "");
    }

    //파라메터 문자열의 모든 숫자를 제거한 값을 반환한다.
    public static String getRemoveNumbers(String str) {
        if (str == null) return "";
        return str.replaceAll("[0-9]", "");
    }

    //파라메터 문자열의 반전값을 반환한다.
    public static String getReverse(String str) {
        if (str == null) return "";
        return new StringBuilder(str).reverse().toString();
    }

    /**********************************************************************
     * etc
     **********************************************************************/
    //클라이언트의 IP주소를 리턴한다.
    public static String getClientIpAddress(HttpServletRequest request) {
        // 여러 헤더를 순서대로 확인
        String[] headers = {
            "X-Forwarded-For", // 일반적으로 프록시를 통과하는 IP 주소 목록을 포함 (가장 신뢰할 수 있는 소스)
            "Proxy-Client-IP", // 프록시 서버에서 사용할 수 있는 대체 헤더
            "WL-Proxy-Client-IP", // WebLogic 서버 환경에서 사용되는 헤더
            "HTTP_X_FORWARDED_FOR", // 표준이 아닌, 일부 프록시 서버에서 사용
            "HTTP_X_FORWARDED", // 표준이 아닌, 일부 프록시 서버에서 사용
            "HTTP_X_CLUSTER_CLIENT_IP", // 클러스터 환경에서 사용하는 클라이언트 IP
            "HTTP_CLIENT_IP", // 클라이언트의 IP 주소를 나타내는 일반 헤더
            "HTTP_FORWARDED_FOR", // 원본 IP 주소를 위한 일반 헤더
            "HTTP_FORWARDED", // 원본 IP 주소를 위한 일반 헤더
            "HTTP_VIA", // 프록시 또는 게이트웨이를 통해 전달된 정보
            "REMOTE_ADDR" // 클라이언트의 실제 IP 주소 (프록시 또는 로드 밸런서를 사용하지 않는 경우)
        };

        for (String header : headers) {
            // 각 헤더를 순서대로 확인하여 유효한 IP 주소 찾기
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                // 여러 IP 주소 중 첫 번째 주소 사용 (가장 신뢰할 수 있는 주소)
                return ip.split(",")[0].trim();
            }
        }

        // 헤더에서 유효한 IP 주소를 찾지 못한 경우, getRemoteAddr 사용
        return request.getRemoteAddr();
    }

    // URL 또는 파일명에서 확장자를 추출한다.
    public static String getFileExtension(String path) {
        int lastIndex = path.lastIndexOf(".");
        
        // 점이 없거나 마지막에 위치하는 경우, 확장자가 없음
        if (lastIndex == -1 || lastIndex == path.length() - 1) {
            return "";
        }
        
        // 마지막 점 이후의 문자열을 반환 (확장자)
        return path.substring(lastIndex + 1);
    }

    //URL 또는 파일명에서 확장자를 제외한 파일명을 추출한다.
    public static String getFileName(String path) {
        // 마지막 슬래시('/') 이후의 문자열 추출
        int lastSlashIndex = path.lastIndexOf("/");
        String fileName = (lastSlashIndex == -1) ? path : path.substring(lastSlashIndex + 1);

        // 파일명에서 확장자 제거
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1) return fileName; // 확장자가 없는 경우

        return fileName.substring(0, lastDotIndex);
    }

    private static Properties loadProperties(String propertyFileName) throws UtilsException {
        Properties prop = new Properties();
        String pfn = PROPERTIE_FILE_PATH + propertyFileName + ".properties";

        try (InputStream input = Utils.class.getClassLoader().getResourceAsStream(pfn)) {
            if (input == null) {
                throw new UtilsException("Unable to find " + pfn);
            }
            prop.load(input);
        } catch (IOException ex) {
            throw new UtilsException(ex.getCause());
        }

        return prop;
    }
    // 프로퍼티 값 가져오기
    public static String getPropertyValue(String propertyFileName, String key) {
        Properties prop = loadProperties(propertyFileName);
        return prop.getProperty(key);
    }
    // 특정 키에 해당하는 값을 Map으로 반환
    public static Map<String, String> getProperty(String propertyFileName, String key) {
        Properties prop = loadProperties(propertyFileName);
        Map<String, String> resultMap = new HashMap<>();
        String value = prop.getProperty(key);
        if (value != null) {
            resultMap.put(key, value);
        }
        return resultMap;
    }
    // 모든 키를 Set으로 반환
    public static Set<String> getPropertyKeys(String propertyFileName) {
        Properties prop = loadProperties(propertyFileName);
        return new HashSet<>(prop.stringPropertyNames());
    }
    // 모든 키와 값을 Map으로 반환
    public static Map<String, String> getProperties(String propertyFileName) {
        Properties prop = loadProperties(propertyFileName);
        Map<String, String> map = new HashMap<>();
        for (String key : prop.stringPropertyNames()) {
            map.put(key, prop.getProperty(key));
        }
        return map;
    }
    // 'key%' 조건에 해당하는 프로퍼티들을 Map으로 반환
    public static Map<String, String> getProperties(String propertyFileName, String keyPrefix) {
        Properties prop = loadProperties(propertyFileName);
        Map<String, String> resultMap = new HashMap<>();
        for (String key : prop.stringPropertyNames()) {
            if (key.startsWith(keyPrefix)) {
                resultMap.put(key, prop.getProperty(key));
            }
        }
        return resultMap;
    }
}
