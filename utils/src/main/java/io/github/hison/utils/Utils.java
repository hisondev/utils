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
 * The Utils class encapsulates a collection of utility methods designed for development use, offering functionalities across various domains.
 * The class includes methods for identifying boolean values, manipulating dates, manipulating numbers, manipulating strings, and retrieving properties.
 *
 * <p>Functionality provided by the Utils class covers a broad spectrum of common programming tasks:</p>
 * <ul>
 * <li><strong>Boolean Identification:</strong> Methods for determining boolean values based on specific criteria.</li>
 * <li><strong>Date Manipulation:</strong> Methods for performing operations on dates, such as addition, subtraction, and formatting.</li>
 * <li><strong>Number Manipulation:</strong> Methods for formatting and processing numerical values, including default patterns for number formatting.</li>
 * <li><strong>String Manipulation:</strong> Methods for altering and evaluating strings, including extracting file names or extensions from paths.</li>
 * <li><strong>Property Retrieval:</strong> Methods for accessing configuration settings from properties files, allowing dynamic adjustments to application behavior.</li>
 * </ul>
 *
 * <p>personal preferences by simply updating the "hison-utils-config.properties" file.</p>
 * <p>Key fields and their purposes include:</p>
 * <ul>
 * <li>DATE_FORMATTER: Default pattern for date formatting.</li>
 * <li>DATETIME_FORMATTER: Default pattern for datetime formatting.</li>
 * <li>ADD_TYPE, DIFF_TYPE, DAY_OF_WEEK_TYPE: Identifiers for addition, subtraction, and day of the week calculations.</li>
 * <li>LESSOREQ_0X7FF_BYTE, LESSOREQ_0XFFFF_BYTE, GREATER_0XFFFF_BYTE: Thresholds for byte size calculations.</li>
 * <li>NUMBER_FORMATTER: Default pattern for number formatting.</li>
 * <li>PROPERTIE_FILE_PATH: Base path for locating the properties file.</li>
 * </ul>
 *
 * <p>By allowing external customization through "hison-utils-config.properties", the Utils class provides flexibility in how default configurations
 * are applied across different environments or applications, fostering a more adaptable and maintainable codebase.</p>
 *
 * <p>By providing a wide range of utility methods, the Utils class serves as a comprehensive toolkit for developers, facilitating common programming
 * tasks and enabling easy access to application configurations.</p>
 * 
 * @author Hani son
 * @version 1.0.1
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
        try (InputStream input = Utils.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) {
                prop.load(input);
            } else {
                System.err.println("application.properties not found.");
            }
        } catch (IOException ex) {
            System.err.println("Failed to load application.properties: " + ex.getMessage());
        }
    
        DATE_FORMATTER = prop.getProperty("hison.utils.format.date", DATE_FORMATTER);
        DATETIME_FORMATTER = prop.getProperty("hison.utils.format.datetime", DATETIME_FORMATTER);
        ADD_TYPE = prop.getProperty("hison.utils.type.date-add", ADD_TYPE);
        DIFF_TYPE = prop.getProperty("hison.utils.type.date-diff", DIFF_TYPE);
        DAY_OF_WEEK_TYPE = prop.getProperty("hison.utils.type.dayofweek", DAY_OF_WEEK_TYPE);
        LESSOREQ_0X7FF_BYTE = Integer.parseInt(prop.getProperty("hison.utils.charbyte.less2047", String.valueOf(LESSOREQ_0X7FF_BYTE)));
        LESSOREQ_0XFFFF_BYTE = Integer.parseInt(prop.getProperty("hison.utils.charbyte.less65535", String.valueOf(LESSOREQ_0XFFFF_BYTE)));
        GREATER_0XFFFF_BYTE = Integer.parseInt(prop.getProperty("hison.utils.charbyte.greater65535", String.valueOf(GREATER_0XFFFF_BYTE)));
        NUMBER_FORMATTER = prop.getProperty("hison.utils.format.number", NUMBER_FORMATTER);
        PROPERTIE_FILE_PATH = prop.getProperty("hison.utils.propertie.file.path", PROPERTIE_FILE_PATH);
    }

    /**********************************************************************
     * for boolean
     **********************************************************************/
    /**
     * <p>Checks if the input string consists only of English alphabet characters.</p>
     * <p>
     * This method verifies that a given string {@code s} contains only characters
     * from 'A' to 'Z' and 'a' to 'z'. It returns {@code true} if the string is
     * exclusively composed of English alphabet characters, otherwise {@code false}.
     * An empty string or a {@code null} input will also return {@code false}.
     * </p>
     * @param s the string to be checked
     * @return {@code true} if {@code s} contains only English alphabet characters,
     *         {@code false} otherwise
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
     * <p>Checks if the input string consists only of English alphabet characters and digits.</p>
     * <p>
     * This method verifies that a given string {@code s} contains only characters
     * from 'A' to 'Z', 'a' to 'z', and digits from '0' to '9'. It returns {@code true} 
     * if the string is exclusively composed of English alphabet characters and digits, 
     * otherwise {@code false}. An empty string or a {@code null} input will also return 
     * {@code false}.
     * </p>
     * <p>
     * This utility is useful for validating input data such as identifiers, usernames,
     * or any other strings that require alphanumeric characters only.
     * </p>
     *
     * @param s the string to be checked
     * @return {@code true} if {@code s} contains only English alphabet characters and digits,
     *         {@code false} otherwise
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
     * <p>Checks if the input string consists only of digits.</p>
     * <p>
     * This method verifies that a given string {@code s} contains only digit characters
     * from '0' to '9'. It returns {@code true} if the string is exclusively composed of digits,
     * otherwise {@code false}. An empty string or a {@code null} input will also return 
     * {@code false}.
     * </p>
     * <p>
     * This utility is particularly useful for validating input data that is expected to be numeric,
     * such as phone numbers, ID numbers, or any other numerical identifiers.
     * </p>
     *
     * @param s the string to be checked
     * @return {@code true} if {@code s} contains only digits,
     *         {@code false} otherwise
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
     * <p>Checks if the input string consists only of digits and specified special characters.</p>
     * <p>
     * This method verifies that a given string {@code s} contains only digits (0-9) and 
     * the following special characters: !@#$%^&amp;*()_+-=[]{};':"\\|,.&lt;&gt;/?~. It returns {@code true}
     * if the string exclusively consists of these characters, otherwise {@code false}. 
     * An empty string or a {@code null} input will also return {@code false}.
     * </p>
     * <p>
     * This utility is especially useful for validating input data that requires a mixture of
     * numeric and special characters, such as passwords or encrypted data strings.
     * </p>
     *
     * @param s the string to be checked
     * @return {@code true} if {@code s} contains only digits and allowed special characters,
     *         {@code false} otherwise
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
     * <p>Checks if the input string contains any special characters.</p>
     * <p>
     * This method verifies that a given string {@code s} contains at least one of the 
     * following special characters: !@#$%^&amp;*()_+-=[]{};':"\\|,.&lt;&gt;/?~. It returns {@code true}
     * if any of these characters are present in the string, otherwise {@code false}. 
     * An empty string or a {@code null} input will also return {@code false}.
     * </p>
     * <p>
     * This utility can be particularly useful for validating strings that should not contain
     * special characters, such as user names or plain text inputs.
     * </p>
     *
     * @param s the string to be checked for the presence of special characters
     * @return {@code true} if {@code s} contains any special characters, {@code false} otherwise
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
     * <p>Checks if the input string consists only of lowercase English alphabet characters.</p>
     * <p>
     * This method verifies that a given string {@code s} is exclusively composed of lowercase
     * letters from 'a' to 'z'. It returns {@code true} if the string does not contain any 
     * uppercase letters, digits, special characters, or whitespace, otherwise {@code false}. 
     * An empty string or a {@code null} input will also return {@code false}.
     * </p>
     * <p>
     * This utility can be particularly useful for validating strings that are expected to 
     * adhere to formats requiring lowercase characters only, such as certain types of 
     * identifiers or codes.
     * </p>
     *
     * @param s the string to be checked
     * @return {@code true} if {@code s} contains only lowercase English alphabet characters,
     *         {@code false} otherwise
     */
    public static boolean isLowerAlpha(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        return s.matches("[a-z]+");
    }

    /**
     * <p>Checks if the input string consists only of lowercase English alphabet characters and digits.</p>
     * <p>
     * This method verifies that a given string {@code s} contains only lowercase letters
     * from 'a' to 'z' and digits from '0' to '9'. It returns {@code true} if the string
     * does not contain any uppercase letters, special characters, or whitespace, 
     * otherwise {@code false}. An empty string or a {@code null} input will also return 
     * {@code false}.
     * </p>
     * <p>
     * This utility is particularly useful for validating strings that need to adhere to
     * specific formats, such as passwords, user IDs, or system-generated codes, where
     * only lowercase letters and numbers are permitted.
     * </p>
     *
     * @param s the string to be checked
     * @return {@code true} if {@code s} contains only lowercase English alphabet characters and digits,
     *         {@code false} otherwise
     */
    public static boolean isLowerAlphaNumber(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        return s.matches("[a-z0-9]+");
    }
    
    /**
     * <p>Checks if the input string consists only of uppercase English alphabet characters.</p>
     * <p>
     * This method verifies that a given string {@code s} is exclusively composed of uppercase
     * letters from 'A' to 'Z'. It returns {@code true} if the string does not contain any 
     * lowercase letters, digits, special characters, or whitespace, otherwise {@code false}. 
     * An empty string or a {@code null} input will also return {@code false}.
     * </p>
     * <p>
     * This utility is particularly useful for validating strings that are expected to 
     * adhere to formats requiring uppercase characters only, such as acronyms, constants,
     * or certain types of identifiers.
     * </p>
     *
     * @param s the string to be checked
     * @return {@code true} if {@code s} contains only uppercase English alphabet characters,
     *         {@code false} otherwise
     */
    public static boolean isUpperAlpha(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        return s.matches("[A-Z]+");
    }

    /**
     * <p>Checks if the input string consists only of uppercase English alphabet characters and digits.</p>
     * <p>
     * This method verifies that a given string {@code s} contains only uppercase letters
     * from 'A' to 'Z' and digits from '0' to '9'. It returns {@code true} if the string
     * does not contain any lowercase letters, special characters, or whitespace, 
     * otherwise {@code false}. An empty string or a {@code null} input will also return 
     * {@code false}.
     * </p>
     * <p>
     * This utility is especially useful for validating strings that need to adhere to
     * specific formats, such as serial numbers, product codes, or any other identifiers
     * that require a combination of uppercase letters and numbers.
     * </p>
     *
     * @param s the string to be checked
     * @return {@code true} if {@code s} contains only uppercase English alphabet characters and digits,
     *         {@code false} otherwise
     */
    public static boolean isUpperAlphaNumber(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        return s.matches("[A-Z0-9]+");
    }

    /**
     * <p>Checks if the input string represents a valid numeric value.</p>
     * <p>
     * This method verifies that a given string {@code s} is a valid representation of a numeric value.
     * It supports both integers and floating-point numbers, including negative values. The method
     * returns {@code true} if the string is a valid numeric format, otherwise {@code false}.
     * An empty string or a {@code null} input will also return {@code false}.
     * </p>
     * <p>
     * This utility is particularly useful for validating input data that is expected to be in a numeric
     * format before attempting to parse it or use it in calculations. It helps in avoiding parsing errors
     * or exceptions when dealing with user input or data from external sources.
     * </p>
     *
     * @param s the string to be checked for numeric validity
     * @return {@code true} if {@code s} represents a valid numeric value, {@code false} otherwise
     */
    public static boolean isNumeric(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        return s.matches("-?\\d+(\\.\\d+)?");
    }

    /**
     * <p>Checks if the input string represents a valid integer value.</p>
     * <p>
     * This method verifies whether a given string {@code s} can be parsed as a double and then checks
     * if it is equivalent to its integer form. It is designed to accurately identify strings that
     * represent integer values, including those that may be expressed in formats that include decimal
     * points but equate to whole numbers (e.g., "4.0").
     * </p>
     * <p>
     * The method returns {@code true} if the string represents an integer value (without leading zeros
     * or decimal points that denote fractional parts), otherwise {@code false}. An empty string or a
     * {@code null} input will also result in {@code false}.
     * </p>
     * <p>
     * Note: This method utilizes parsing to a double as an intermediate step to accommodate potential
     * decimal representations of integers. It is not intended for validating large numbers where
     * precision beyond the floating-point limit could cause inaccuracies.
     * </p>
     *
     * @param s the string to be checked for integer validity
     * @return {@code true} if {@code s} represents a valid integer value, {@code false} otherwise
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
     * <p>Checks if the provided double value represents an integer.</p>
     * <p>
     * This method determines whether a given double value {@code d} is effectively an integer by
     * comparing it to its casted integer form. It returns {@code true} if the double value does not
     * have a fractional part, indicating it is an integer; otherwise, it returns {@code false}.
     * </p>
     * <p>
     * This utility is useful for cases where it's necessary to ensure that a floating-point number
     * does not include any decimal values and can be considered as an integer.
     * </p>
     *
     * @param d the double value to be checked for integer equivalency
     * @return {@code true} if {@code d} represents an integer, {@code false} otherwise
     */
    public static boolean isInteger(double d) {
        return d == (int) d;
    }
    /**
     * <p>Checks if the provided float value represents an integer.</p>
     * <p>
     * This method evaluates whether a given float value {@code f} qualifies as an integer by
     * comparing it with its casted integer form. If the float value equals its integer counterpart,
     * indicating there is no fractional part, the method returns {@code true}; otherwise, it returns
     * {@code false}.
     * </p>
     * <p>
     * It is particularly useful in contexts where distinguishing between integer and non-integer
     * floating-point numbers is crucial, such as in numerical computations or data validation.
     * </p>
     *
     * @param f the float value to be checked for integer equivalency
     * @return {@code true} if {@code f} represents an integer, {@code false} otherwise
     */
    public static boolean isInteger(float f) {
        return f == (int) f;
    }

    /**
     * <p>Checks if the input string represents a positive integer.</p>
     * <p>
     * This method determines whether a given string {@code s} can be parsed as an integer and
     * if that integer is greater than zero. It is designed to accurately identify strings that
     * represent positive integer values. The method returns {@code true} if the string is a valid
     * representation of a positive integer, otherwise {@code false}.
     * </p>
     * <p>
     * An empty string or a {@code null} input will also return {@code false}. This check includes
     * handling {@link NumberFormatException} to ensure that the input string is indeed numeric and
     * represents a positive integer value.
     * </p>
     * <p>
     * This utility is particularly useful for validating numeric inputs where only positive integers
     * are acceptable, such as user age, quantity of items, or any other countable number where negative
     * values or zero are not permitted.
     * </p>
     *
     * @param s the string to be checked for being a positive integer
     * @return {@code true} if {@code s} represents a positive integer, {@code false} otherwise
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
     * <p>Checks if the provided double value represents a positive integer.</p>
     * <p>
     * This method evaluates whether a given double value {@code d} is both greater than zero
     * and has no fractional part, indicating it is an integer. It achieves this by first checking
     * if the value is greater than zero and then comparing the value to its casted integer form
     * to ensure there is no fractional part. 
     * </p>
     * <p>
     * This utility is useful for scenarios where it's necessary to validate that numerical inputs 
     * are positive integers, such as in financial calculations, counting operations, or other 
     * contexts where non-integer or negative values are not permissible.
     * </p>
     *
     * @param d the double value to be checked for being a positive integer
     * @return {@code true} if {@code d} represents a positive integer, {@code false} otherwise
     */
    public static boolean isPositiveInteger(double d) {
        return d > 0 && d == (int) d;
    }
    /**
     * <p>Checks if the provided float value represents a positive integer.</p>
     * <p>
     * This method determines whether a given float value {@code f} qualifies as a positive integer
     * by first ensuring the value is greater than zero and then confirming that it does not contain 
     * a fractional part when compared to its integer cast. This method is designed to accurately 
     * identify float values that represent positive integers without any decimal component.
     * </p>
     * <p>
     * Particularly useful for validating numerical inputs in contexts requiring strictly positive 
     * integers, such as user inputs for age, quantity of items, or other numerical parameters where
     * fractions or negative numbers are invalid.
     * </p>
     *
     * @param f the float value to be checked for being a positive integer
     * @return {@code true} if {@code f} represents a positive integer, {@code false} otherwise
     */
    public static boolean isPositiveInteger(float f) {
        return f > 0 && f == (int) f;
    }

    /**
     * <p>Checks if the input string represents a negative integer.</p>
     * <p>
     * This method determines whether a given string {@code s} can be parsed as an integer and
     * if that integer is less than zero. It is designed to accurately identify strings that
     * represent negative integer values. The method returns {@code true} if the string is a valid
     * representation of a negative integer, otherwise {@code false}.
     * </p>
     * <p>
     * An empty string or a {@code null} input will result in {@code false}. This method includes
     * handling of {@link NumberFormatException} to ensure that the input string is indeed numeric
     * and represents a negative integer value.
     * </p>
     * <p>
     * This utility is particularly useful for validating numeric inputs where only negative integers
     * are acceptable, such as in certain financial calculations, temperature scales, or other contexts
     * where positive numbers or zero are not permitted.
     * </p>
     *
     * @param s the string to be checked for being a negative integer
     * @return {@code true} if {@code s} represents a negative integer, {@code false} otherwise
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
     * <p>Checks if the provided double value represents a negative integer.</p>
     * <p>
     * This method evaluates whether a given double value {@code d} is both less than zero
     * and has no fractional part, indicating it is an integer. It achieves this by first checking
     * if the value is less than zero and then comparing the value to its casted integer form
     * to ensure there is no fractional part. 
     * </p>
     * <p>
     * This utility is useful for scenarios where it's necessary to validate that numerical inputs 
     * are negative integers, such as in some financial calculations or contexts where positive 
     * numbers or zero are not applicable.
     * </p>
     *
     * @param d the double value to be checked for being a negative integer
     * @return {@code true} if {@code d} represents a negative integer, {@code false} otherwise
     */
    public static boolean isNegativeInteger(double d) {
        return d < 0 && d == (int) d;
    }
    /**
     * <p>Checks if the provided float value represents a negative integer.</p>
     * <p>
     * This method determines whether a given float value {@code f} qualifies as a negative integer
     * by first ensuring the value is less than zero and then confirming that it does not contain 
     * a fractional part when compared to its integer cast. This method is designed to accurately 
     * identify float values that represent negative integers without any decimal component.
     * </p>
     * <p>
     * Particularly useful for validating numerical inputs in contexts requiring strictly negative 
     * integers, such as adjustments in inventory levels, temperature changes in certain scales, or 
     * other numerical parameters where fractions, positive numbers, or zero are invalid.
     * </p>
     *
     * @param f the float value to be checked for being a negative integer
     * @return {@code true} if {@code f} represents a negative integer, {@code false} otherwise
     */
    public static boolean isNegativeInteger(float f) {
        return f < 0 && f == (int) f;
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
     * <p>Checks if the input string represents a valid date.</p>
     * <p>
     * This method verifies whether a given string {@code date} can be parsed as a {@link LocalDate}
     * using predefined date formats. The supported formats are "uuuuMMdd", "uuuu-MM-dd", and "uuuu/MM/dd".
     * It leverages the {@code getDate} method, which attempts to parse the date string using each of the 
     * formats specified in the order they are defined until it succeeds or exhausts all options.
     * </p>
     * <p>
     * The method returns {@code true} if the string can be successfully parsed into a {@link LocalDate}
     * object using any of the supported formats, indicating it represents a valid date. If the string
     * cannot be parsed using any of the formats or if it is {@code null} or empty, the method returns
     * {@code false}.
     * </p>
     * <p>
     * This utility is particularly useful for validating input data that is expected to represent a date,
     * ensuring it conforms to one of the supported date formats before performing operations that require
     * a valid date.
     * </p>
     *
     * @param date the string to be checked for being a valid date
     * @return {@code true} if {@code date} represents a valid date in one of the supported formats,
     *         {@code false} otherwise
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
     * <p>Checks if the input string represents a valid time format.</p>
     * <p>
     * This method verifies whether a given string {@code time} can be parsed as a {@link LocalTime}
     * using predefined time formats. The supported formats are "HH:mm:ss" and "HHmmss". It leverages
     * the {@code getTime} method, which attempts to parse the time string using each of the formats
     * specified in the order they are defined until it succeeds or exhausts all options.
     * </p>
     * <p>
     * The method returns {@code true} if the string can be successfully parsed into a {@link LocalTime}
     * object using any of the supported formats, indicating it represents a valid time. If the string
     * cannot be parsed using any of the formats or if it is {@code null} or empty, the method returns
     * {@code false}.
     * </p>
     * <p>
     * This utility is particularly useful for validating input data that is expected to represent a time,
     * ensuring it conforms to one of the supported time formats before performing operations that require
     * a valid time.
     * </p>
     *
     * @param time the string to be checked for being a valid time
     * @return {@code true} if {@code time} represents a valid time in one of the supported formats,
     *         {@code false} otherwise
     */
    public static boolean isTime(String time) {
        return getTime(time) != null;
    }

    /**
     * <p>Checks if the input string represents a valid date and time format.</p>
     * <p>
     * This method determines whether a given string {@code datetime} can be parsed as a valid
     * date and optionally a time. The string is expected to be in one of the following formats:
     * </p>
     * <ul>
     * <li>"YYYY-MM-DD" for date only.</li>
     * <li>"YYYY-MM-DD HH:mm:ss" for date and time.</li>
     * </ul>
     * <p>
     * The method first splits the input string based on a whitespace delimiter to separate date
     * and time components. It then validates the date component using the {@code isDate} method.
     * If a time component is present, it validates the time using the {@code isTime} method.
     * </p>
     * <p>
     * The method returns {@code true} if the input string is a valid representation of a date
     * or a date and time according to the specified formats. Otherwise, it returns {@code false}.
     * This validation does not account for strings with more than two parts or formats not
     * covered by the {@code isDate} and {@code isTime} methods.
     * </p>
     * <p>
     * This utility is particularly useful for validating datetime inputs in forms, configurations,
     * or any other context where precise adherence to datetime formatting standards is required.
     * </p>
     *
     * @param datetime the string to be checked for being a valid date and/or time
     * @return {@code true} if {@code datetime} represents a valid date and/or time format,
     *         {@code false} otherwise
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
     * <p>Checks if the input string represents a valid email address.</p>
     * <p>
     * This method determines whether a given string {@code email} conforms to a common
     * pattern for email addresses. The pattern supports email addresses that:
     * </p>
     * <ul>
     * <li>Start with alphanumeric characters, underscores (_), pluses (+), ampersands (&amp;), asterisks (*), or hyphens (-).</li>
     * <li>May contain dots (.) separating alphanumeric and allowed special characters, but not consecutively.</li>
     * <li>Include an @ symbol separating the user name from the domain name.</li>
     * <li>Have a domain name that includes alphanumeric characters or hyphens (-) and at least one dot (.) separating domain levels.</li>
     * <li>End with a top-level domain (TLD) that is 2 to 7 characters long.</li>
     * </ul>
     * <p>
     * The method returns {@code true} if the email address matches this pattern, indicating it is
     * considered valid according to the specified criteria. Otherwise, it returns {@code false}.
     * </p>
     * <p>
     * This utility is useful for validating user input in forms, ensuring email addresses provided by
     * users adhere to a standard format before submission or processing.
     * </p>
     *
     * @param email the string to be checked for being a valid email address
     * @return {@code true} if {@code email} represents a valid email address according to the defined pattern,
     *         {@code false} otherwise
     */
    public static boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.matches(emailPattern, email);
    }

    /**
     * <p>Checks if the input string represents a valid URL.</p>
     * <p>
     * This method determines whether a given string {@code url} conforms to a commonly accepted
     * pattern for URLs. This pattern supports both HTTP and HTTPS protocols and optionally includes
     * "www." at the beginning. The URL must contain a domain name that consists of alphanumeric
     * characters, hyphens (-), and may include various subdomains separated by dots (.). The domain
     * name should end with a top-level domain (TLD) that is 2 to 6 characters long. URLs may also
     * include paths, query parameters, and fragments.
     * </p>
     * <p>
     * The method returns {@code true} if the string matches the URL pattern, indicating it is
     * considered a valid URL according to the specified criteria. Otherwise, it returns {@code false}.
     * </p>
     * <p>
     * This utility is useful for validating user input in forms, ensuring URLs provided by users
     * adhere to a standard format before submission or processing. It helps in avoiding errors
     * when using the URLs for web requests or storing them in databases.
     * </p>
     *
     * @param url the string to be checked for being a valid URL
     * @return {@code true} if {@code url} represents a valid URL according to the defined pattern,
     *         {@code false} otherwise
     */
    public static boolean isValidURL(String url) {
        String urlPattern = "^https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)";
        return Pattern.matches(urlPattern, url);
    }

    /**
     * <p>Checks if the input string represents a valid IPv4 address.</p>
     * <p>
     * This method determines whether a given string {@code ip} conforms to the standard IPv4 address format.
     * An IPv4 address consists of four octets separated by dots ('.'). Each octet should be a decimal number
     * ranging from 0 to 255. The method uses a regular expression to validate the IPv4 address format, ensuring
     * that each octet falls within the correct range.
     * </p>
     * <p>
     * The method returns {@code true} if the string matches the IPv4 pattern, indicating it is considered
     * a valid IPv4 address. If the string does not match the pattern or is {@code null}, the method returns
     * {@code false}.
     * </p>
     * <p>
     * This utility is particularly useful for validating network configuration inputs, ensuring that IP addresses
     * provided by users or obtained from external sources are in a correct and usable format before attempting
     * network connections or configurations.
     * </p>
     *
     * @param ip the string to be checked for being a valid IPv4 address
     * @return {@code true} if {@code ip} represents a valid IPv4 address according to the defined pattern,
     *         {@code false} otherwise
     */
    public static boolean isValidIPv4(String ip) {
        String ipPattern = "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$";
        
        return ip != null && ip.matches(ipPattern);
    }

    /**
     * <p>Checks if the input string represents a valid IPv6 address.</p>
     * <p>
     * This method determines whether a given string {@code ip} conforms to the standard IPv6 address format.
     * IPv6 addresses consist of eight groups of four hexadecimal digits, but various shorthand notations are
     * also allowed. These notations include omitting leading zeros, using "::" to denote a series of zero
     * groups, and mixing IPv4 and IPv6 formats for IPv4-mapped IPv6 addresses.
     * </p>
     * <p>
     * The supported IPv6 formats by this method are extensive and include:
     * </p>
     * <ul>
     * <li>Full 8-group notation (e.g., "2001:0db8:85a3:0000:0000:8a2e:0370:7334").</li>
     * <li>Shorthand notation with "::" replacing one or more groups of zeros.</li>
     * <li>IPv4-mapped IPv6 addresses (e.g., "::ffff:192.168.1.1").</li>
     * <li>Link-local addresses with zone indices (e.g., "fe80::1%eth0").</li>
     * </ul>
     * <p>
     * The method returns {@code true} if the string matches any of the supported IPv6 patterns, indicating
     * it is considered a valid IPv6 address. If the string does not match the pattern, is {@code null}, or
     * is empty, the method returns {@code false}.
     * </p>
     * <p>
     * This utility is particularly useful for validating IP address inputs in network configurations, ensuring
     * that IPv6 addresses provided by users or obtained from external sources are in a correct and usable
     * format before attempting network connections or configurations.
     * </p>
     *
     * @param ip the string to be checked for being a valid IPv6 address
     * @return {@code true} if {@code ip} represents a valid IPv6 address according to the defined pattern,
     *         {@code false} otherwise
     */
    public static boolean isValidIPv6(String ip) {
        String ipv6Pattern = "^(" +
                "([0-9a-fA-F]{1,4}:){7}([0-9a-fA-F]{1,4}|:)|" +
                "([0-9a-fA-F]{1,4}:){1,7}:|" +
                "([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|" +
                "([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|" +
                "([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|" +
                "([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|" +
                "([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|" +
                "([0-9a-fA-F]{1,4}:){1}(:[0-9a-fA-F]{1,4}){1,6}|" +
                ":((:[0-9a-fA-F]{1,4}){1,7}|:)|" +
                "fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|" +
                "::(ffff(:0{1,4}){0,1}:){0,1}" +
                "((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\." +
                "(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\." +
                "(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\." +
                "(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|" +
                "([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\." +
                "(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\." +
                "(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\." +
                "(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])" +
                ")%[0-9a-zA-Z]{1,}$";
        
        return ip != null && ip.matches(ipv6Pattern);
    }

    /**
     * <p>Checks if the input string conforms to the specified mask format.</p>
     * <p>
     * This method determines whether a given string {@code value} matches a specified mask {@code mask}
     * where certain characters in the mask dictate the type of characters allowed at their respective
     * positions in the input string. The mask can specify:
     * </p>
     * <ul>
     * <li>'A' to denote an uppercase letter (A-Z) is required at this position.</li>
     * <li>'a' to denote a lowercase letter (a-z) is required at this position.</li>
     * <li>'9' to denote a digit (0-9) is required at this position.</li>
     * <li>Any other character represents itself and must match exactly at this position.</li>
     * </ul>
     * <p>
     * The method returns {@code true} if the input string {@code value} exactly matches the mask
     * {@code mask}, with each character in {@code value} meeting the requirements specified by the
     * corresponding character in {@code mask}. Otherwise, it returns {@code false}.
     * </p>
     * <p>
     * This utility is useful for validating string inputs against specific formatting requirements,
     * such as serial numbers, phone numbers, or other standardized identifiers. It ensures that the
     * input not only matches the length of the mask but also conforms to the character-type requirements
     * at each position.
     * </p>
     *
     * @param value the string to be checked against the mask
     * @param mask the mask format that {@code value} is expected to conform to
     * @return {@code true} if {@code value} conforms to the specified {@code mask}, {@code false} otherwise
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
     * <p>Adds a specified amount of days to a given date or datetime string, represented as a string.</p>
     * <p>
     * This method is a convenience overload of {@link #addDate(String, int, String, String)},
     * with the addition type defaulted to days and both the addition value and format are specified as strings.
     * </p>
     *
     * @param datetime The date or datetime string to which the days are to be added.
     * @param addValue The number of days to add, represented as a string.
     * @return A string representing the datetime after the specified number of days has been added.
     */
    public static String addDate(String datetime, String addValue) {
        return addDate(datetime, addValue, "");
    }
    /**
     * <p>Adds a specified amount of time to a given date or datetime string, according to the specified type of addition, both represented as strings.</p>
     * <p>
     * This method is a convenience overload of {@link #addDate(String, int, String, String)},
     * allowing the addition of time to a datetime string with the addition value and type specified as strings and the format defaulted.
     * </p>
     *
     * @param datetime The date or datetime string to which the time is to be added.
     * @param addValue The amount of time to add, represented as a string.
     * @param addType The type of time to add (e.g., 'y', 'M', 'd', 'h', 'm', 's'), as a string.
     * @return A string representing the datetime after the specified amount of time has been added.
     */
    public static String addDate(String datetime, String addValue, String addType) {
        return addDate(datetime, addValue, addType, "");
    }
    /**
     * <p>Adds a specified amount of time to a given date or datetime string, allowing detailed specification of addition value, type, and output format.</p>
     * <p>
     * This method enables adding a precise amount of time to a datetime string where the addition value, type, and desired output format are specified as strings.
     * The {@code addValue} is converted to an integer, and the method proceeds to add the specified time to the datetime. If the {@code addValue} cannot be
     * converted to an integer, a {@link UtilsException} is thrown.
     * </p>
     *
     * @param datetime The date or datetime string to which the time is to be added.
     * @param addValue The amount of time to add, represented as a string. Must be convertible to an integer.
     * @param addType The type of time to add (e.g., 'y', 'M', 'd', 'h', 'm', 's'), as a string.
     * @param format The desired output format for the resulting datetime string.
     * @return A string representing the datetime after the specified amount of time has been added, formatted according to the {@code format} parameter.
     * @throws UtilsException If the {@code addValue} string cannot be converted into a valid integer value.
     */
    public static String addDate(String datetime, String addValue, String addType, String format) {
        if(!isInteger(addValue)) {
            throw new UtilsException("Please enter a valid addValue(Integer).");
        }
        int add = Integer.parseInt(addValue);
        return addDate(datetime, add, addType, format);
    }
    /**
     * <p>Adds a specified amount of days to a given date or datetime string.</p>
     * <p>
     * This method is a convenience overload of {@link #addDate(String, int, String, String)},
     * with the addition type defaulted to days and format defaulted to the class's default date or datetime format.
     * </p>
     *
     * @param datetime The date or datetime string to which the days are to be added.
     * @param addValue The number of days to add.
     * @return A string representing the datetime after the specified number of days has been added, formatted according to the class's default formatting.
     */
    public static String addDate(String datetime, int addValue) {
        return addDate(datetime, addValue, "");
    }
    /**
     * <p>Adds a specified amount of time to a given date or datetime string, according to the specified type of addition.</p>
     * <p>
     * This method is a convenience overload of {@link #addDate(String, int, String, String)},
     * with the format defaulted to the class's default date or datetime format. It allows adding years, months, days, hours, minutes, or seconds.
     * </p>
     *
     * @param datetime The date or datetime string to which the time is to be added.
     * @param addValue The amount of time to add.
     * @param addType The type of time to add ('y' for years, 'M' for months, 'd' for days, 'h' for hours, 'm' for minutes, 's' for seconds).
     *                If empty, defaults to adding days.
     * @return A string representing the datetime after the specified amount of time has been added, formatted according to the class's default formatting.
     */
    public static String addDate(String datetime, int addValue, String addType) {
        return addDate(datetime, addValue, addType, "");
    }
    /**
     * <p>Adds a specified amount of time to a given datetime string and formats the result according to the specified format.</p>
     * <p>
     * This method allows for adding a specific amount of time (years, months, days, hours, minutes, or seconds) to a datetime
     * string represented in a recognized format. The result is then formatted into a string according to the specified format
     * parameter. If the format parameter is left empty and the input datetime includes time, it defaults to using the class's
     * predefined datetime format. Otherwise, the default date format is applied.
     * </p>
     * <p>
     * This utility is particularly useful for operations that require the manipulation of datetime values for display, storage,
     * or further calculation. It encapsulates the parsing, arithmetic, and formatting operations into a single method call.
     * </p>
     *
     * @param datetime The datetime string to which the time will be added. Must be in a format parsable to {@link LocalDateTime}.
     * @param addValue The integer value representing the amount of time to add.
     * @param addType The type of time unit to be added ('y' for years, 'M' for months, 'd' for days, 'h' for hours, 'm' for minutes, 's' for seconds).
     *                If null or empty, it defaults to days ('d').
     * @param format The desired format for the output datetime string. If empty and input includes time, defaults to the class's datetime format;
     *               otherwise, defaults to the class's date format.
     * @return The resulting datetime as a string formatted according to the specified or default format.
     * @throws UtilsException If the input datetime string is invalid, unparseable, or if an invalid addType is specified.
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

        if ("".equals(format) && datetime.split(" ").length > 1) {
            format = DATETIME_FORMATTER;
        }
        return getDateWithFormat(dt, format);
    }

    /**
     * <p>Calculates the difference in days between two dates or datetimes.</p>
     * <p>
     * This method is a convenience overload of {@link #getDateDiff(String, String, String)},
     * defaulting the difference calculation to days. It calculates the difference between two datetime
     * strings, assuming the desired unit of measurement is days. This is particularly useful for scenarios
     * where the precise number of days between two dates is needed, such as calculating age, tenure, or
     * the duration between events.
     * </p>
     *
     * @param datetime1 The first date or datetime string from which the difference is to be calculated.
     * @param datetime2 The second date or datetime string to which the difference is calculated.
     * @return The difference between the two datetime strings in days, as an integer.
     */
    public static int getDateDiff(String datetime1, String datetime2) {
        return getDateDiff(datetime1, datetime2, "");
    }
    /**
     * <p>Calculates the difference between two dates or datetimes in the specified unit.</p>
     * <p>
     * This method calculates the difference between two datetime strings, returning the result
     * as an integer in the unit specified by {@code diffType}. The difference can be calculated
     * in years, months, days, hours, minutes, or seconds. The datetime strings must be in a valid
     * format that can be parsed into {@link java.time.LocalDateTime} objects. If either datetime
     * string is invalid, a {@link UtilsException} is thrown.
     * </p>
     * <p>
     * This utility is useful for calculating age, tenure, durations between events, and other
     * time-based metrics that require precise quantification of time intervals.
     * </p>
     *
     * @param datetime1 The first date or datetime string from which the difference is to be calculated.
     * @param datetime2 The second date or datetime string to which the difference is calculated.
     * @param diffType The type of difference to calculate ('y' for years, 'M' for months, 'd' for days,
     *                 'h' for hours, 'm' for minutes, 's' for seconds). If null or empty, defaults
     *                 to days.
     * @return The difference between the two datetime strings in the unit specified by {@code diffType},
     *         as an integer.
     * @throws UtilsException If either {@code datetime1} or {@code datetime2} is not a valid datetime string.
     * @throws IllegalArgumentException If {@code diffType} is not one of the valid options.
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
     * <p>Returns the full name of the specified month based on a string representation of the month number.</p>
     * <p>
     * This method simply calls {@link #getMonthName(int, boolean)} with {@code isFullName} set to {@code true}.
     * </p>
     *
     * @param month The string representation of the month number (1 for January, 12 for December).
     * @return The full name of the month.
     * @throws UtilsException If the provided {@code month} value is not a valid integer representation of a month.
     */
    public static String getMonthName(String month) {
        return getMonthName(month, true);
    }
    /**
     * <p>Returns the name of the specified month based on a string representation of the month number,
     * allowing for either the full name or abbreviated form.</p>
     * <p>
     * This method first validates that the input string represents a valid integer and then converts it
     * to an integer to retrieve the month name. It allows the name to be returned in either its full form
     * or its abbreviated form, depending on the {@code isFullName} parameter.
     * </p>
     *
     * @param month The string representation of the month number (1 for January, 12 for December).
     * @param isFullName A boolean flag indicating whether to return the full name of the month
     *                   ({@code true}) or its abbreviated form ({@code false}).
     * @return The name of the month in either full or abbreviated form.
     * @throws UtilsException If the provided {@code month} string is not a valid integer representation of a month.
     */
    public static String getMonthName(String month, boolean isFullName) {
        if(!isInteger(month)) {
            throw new UtilsException("Please enter a valid month.");
        }
        int mon = Integer.parseInt(month);

        return getMonthName(mon, isFullName);
    }
    /**
     * <p>Returns the full name of the specified month based on its numeric representation.</p>
     * <p>
     * This method is a convenience overload of {@link #getMonthName(int, boolean)}, defaulting to
     * returning the full name of the month.
     * </p>
     *
     * @param month The numeric representation of the month (1 for January, 12 for December).
     * @return The full name of the month.
     */
    public static String getMonthName(int month) {
        return getMonthName(month, true);
    }
    /**
     * <p>Returns the name of the specified month.</p>
     * <p>
     * This method provides the name of the month corresponding to the given numeric value. The name can be
     * returned in either its full form (e.g., "January") or its abbreviated form (e.g., "Jan"), depending on
     * the value of the {@code isFullName} parameter.
     * </p>
     * <p>
     * It is designed to support internationalization by utilizing the {@link java.time.Month} class from the
     * Java Date-Time API and can easily be adapted to support locales other than English by modifying the 
     * {@link java.util.Locale} parameter.
     * </p>
     * <p>
     * This utility is particularly useful for converting numeric month values into human-readable string 
     * representations, facilitating easier display and interpretation in user interfaces or reports.
     * </p>
     *
     * @param month The numeric representation of the month (1 for January, 12 for December).
     * @param isFullName A boolean flag indicating whether to return the full name of the month
     *                   ({@code true}) or its abbreviated form ({@code false}).
     * @return The name of the month corresponding to the given numeric value, in either full or abbreviated
     *         form based on the {@code isFullName} parameter.
     * @throws UtilsException If the provided {@code month} value is outside the valid range of 1 to 12,
     *                        indicating an invalid month.
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
     * <p>Formats a datetime string using the default date format.</p>
     * <p>
     * This method is a convenience overload, formatting the given datetime string using the class's
     * default date format. It's useful for quickly formatting datetime strings without specifying a custom format.
     * </p>
     *
     * @param datetime The datetime string to be formatted.
     * @return The formatted date as a string.
     */
    public static String getDateWithFormat(String datetime) {
        return getDateWithFormat(datetime, "");
    }
    /**
     * <p>Formats a datetime string using a specified format.</p>
     * <p>
     * This method formats a given datetime string according to the specified format string. If the format
     * is not specified (empty string), the class's default date format is used. This method allows for
     * flexible formatting of datetime strings according to various patterns.
     * </p>
     *
     * @param datetime The datetime string to be formatted.
     * @param format The format string to be applied. If empty, the class's default date format is used.
     * @return The formatted date as a string.
     */
    public static String getDateWithFormat(String datetime, String format) {
        return getDateWithFormat(getDatetime(datetime), format);
    }
    /**
     * <p>Formats a {@link LocalDateTime} object using the default date format.</p>
     * <p>
     * This method is a convenience overload, formatting the given {@link LocalDateTime} object using the class's
     * default date format. It's useful for quickly formatting {@link LocalDateTime} objects without specifying a custom format.
     * </p>
     *
     * @param datetime The {@link LocalDateTime} object to be formatted.
     * @return The formatted date as a string.
     */
    public static String getDateWithFormat(LocalDateTime datetime) {
        return getDateWithFormat(datetime, "");
    }
    /**
     * <p>Formats a {@link LocalDateTime} object using a specified format.</p>
     * <p>
     * This method formats a given {@link LocalDateTime} object according to the specified format string.
     * If the format is not specified (empty string), the class's default date format is used. It throws
     * a {@link UtilsException} if the datetime object is {@code null} or if the format is invalid.
     * This utility is essential for converting {@link LocalDateTime} objects into human-readable string representations
     * according to custom or default patterns.
     * </p>
     *
     * @param datetime The {@link LocalDateTime} object to be formatted.
     * @param format The format string to be applied. If empty, the class's default date format is used.
     * @return The formatted date as a string.
     * @throws UtilsException If the datetime object is {@code null} or if the format string is invalid.
     */
    public static String getDateWithFormat(LocalDateTime datetime, String format) {
        if(datetime == null) throw new UtilsException("Please enter a valid date.");
        if("".equals(format)) format = DATE_FORMATTER;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return datetime.format(formatter);
        } catch (DateTimeParseException e) {
            throw new UtilsException("Invalid format.");
        }
    }

    /**
     * <p>Returns the day of the week for a given date string using the default day type.</p>
     * <p>
     * This method is a convenience overload, providing the day of the week for a given date string.
     * The day of the week is calculated using the class's default day of week type.
     * </p>
     *
     * @param date The date string for which the day of the week is to be calculated.
     * @return The day of the week represented as a string according to the default day type.
     */
    public static String getDayOfWeek(String date) {
        return getDayOfWeek(date, "");
    }
    /**
     * <p>Returns the day of the week for a given {@link LocalDate} using the default day type.</p>
     * <p>
     * This method is a convenience overload, providing the day of the week for a given {@link LocalDate}.
     * The day of the week is calculated using the class's default day of week type.
     * </p>
     *
     * @param date The {@link LocalDate} for which the day of the week is to be calculated.
     * @param dayOfWeekType The type of day of week representation ('d' for numeric, 'dy' for abbreviated name in US locale,
     *                      'day' for full name in US locale, 'kdy' for abbreviated name in Korean locale, 'kday' for full name in Korean locale).
     * @return The day of the week represented as a string according to the default day type.
     */
    public static String getDayOfWeek(String date, String dayOfWeekType) {
        return getDayOfWeek(getDate(date), dayOfWeekType);
    }
    /**
     * <p>Returns the day of the week for a given date string according to a specified day of week type.</p>
     * <p>
     * This method formats the day of the week of a given date string according to the specified day of week type.
     * It supports various representations such as numeric day (e.g., 1 for Monday), abbreviated day names (e.g., "Mon"),
     * and full day names (e.g., "Monday"), in both US and Korean locales.
     * </p>
     *
     * @param date The date string for which the day of the week is to be calculated.
     * @return The day of the week represented according to the specified day type.
     */
    public static String getDayOfWeek(LocalDate date) {
        return getDayOfWeek(date, "");
    }
    /**
     * <p>Returns the day of the week for a given {@link LocalDate} according to a specified day of week type.</p>
     * <p>
     * This method calculates the day of the week for a given {@link LocalDate} and formats it according to the
     * specified day of week type. It allows for flexible representation of the day of the week, supporting
     * numeric, abbreviated, and full names in both US and Korean locales. This is useful for applications
     * requiring locale-specific or specific format representations of the day of the week.
     * </p>
     *
     * @param date The {@link LocalDate} for which the day of the week is to be calculated.
     * @param dayOfWeekType The type of day of week representation ('d' for numeric, 'dy' for abbreviated name in US locale,
     *                      'day' for full name in US locale, 'kdy' for abbreviated name in Korean locale, 'kday' for full name in Korean locale).
     *                      If null or empty, defaults to the class's default day of week type.
     * @return The day of the week represented according to the specified day type.
     * @throws UtilsException If the input date is null or the dayOfWeekType is invalid.
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
     * <p>Returns the last day of the month for a given {@link LocalDate}.</p>
     * <p>
     * This method calculates the last day of the month for the specified date. It is useful for determining the end of the month,
     * which can be particularly helpful in date calculations involving billing cycles, scheduling, and period-end processing.
     * </p>
     *
     * @param date The {@link LocalDate} representing the date within the month of interest.
     * @return The day of the month corresponding to the last day in the same month as the specified date.
     * @throws UtilsException If the input date is {@code null}.
     */
    public static int getLastDay(LocalDate date) {
        if (date == null) {
            throw new UtilsException("Please enter a valid date.");
        }
        return date.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
    }
    /**
     * <p>Returns the last day of the month for a given year and month string.</p>
     * <p>
     * This method calculates the last day of the month for the specified year and month. The input string can be in various
     * formats, but it must represent a valid year and month. The method attempts to parse the string to a {@link LocalDate},
     * adding a default day if necessary, to facilitate the calculation. This is convenient for processing dates in various
     * string formats without requiring a full date specification.
     * </p>
     * <p>
     * If the input string is in an unrecognized format or does not represent a valid date, an attempt is made to correct
     * it by appending a default day value. If this corrected date is still invalid, an {@link UtilsException} is thrown.
     * </p>
     *
     * @param yearMonth The string representing the year and month (e.g., "2020-01", "202001").
     * @return The day of the month corresponding to the last day in the specified month.
     * @throws UtilsException If the input string cannot be parsed into a valid {@link LocalDate}.
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
     * <p>Returns the current system date and time in the default datetime format.</p>
     * <p>
     * This convenience method fetches the current system date and time, formatting it according to
     * the class's predefined default datetime format. It simplifies obtaining a formatted string
     * representation of the current moment without specifying a custom format.
     * </p>
     *
     * @return A string representing the current system date and time, formatted according to the
     *         class's default datetime format.
     */
    public static String getSysDatetime() {
        return getSysDatetime("");
    }
    /**
     * <p>Returns the current system date and time in a specified format.</p>
     * <p>
     * This method fetches the current system date and time, formatting it according to the specified
     * format string. If no format is specified (i.e., an empty string is passed), it defaults to using
     * the class's predefined datetime format. This allows for flexibility in representing the current
     * moment in various textual formats, suitable for logging, display, or further processing.
     * </p>
     * <p>
     * The format string should follow the patterns defined by {@link java.time.format.DateTimeFormatter}.
     * If the specified format is invalid, a {@link UtilsException} might be thrown by the underlying
     * formatting method.
     * </p>
     *
     * @param format The format string to be applied to the current datetime. If empty, the class's default
     *               datetime format is used.
     * @return A string representing the current system date and time, formatted according to the specified
     *         or default format.
     */
    public static String getSysDatetime(String format) {
        if("".equals(format)) format = DATETIME_FORMATTER;
        return getDateWithFormat(LocalDateTime.now(), format);
    }

    /**
     * <p>Returns the current system day of the week using the default day type.</p>
     * <p>
     * This method fetches the current system day and formats it according to the class's default day type,
     * which could be a numeric representation, a short name, or a full name of the day of the week. It provides
     * a straightforward way to obtain a string representation of the current day of the week without specifying
     * a custom day type.
     * </p>
     *
     * @return A string representing the current day of the week, formatted according to the class's default day type.
     */
    public static String getSysDayOfWeek() {
        return getSysDayOfWeek("");
    }
    /**
     * <p>Returns the current system day of the week in a specified day type.</p>
     * <p>
     * This method fetches the current system day and formats it according to the specified day type parameter.
     * The day type can be specified to return the day in various forms, such as numeric ('d'), abbreviated ('dy' or 'kdy' for Korean),
     * or full name ('day' or 'kday' for Korean) of the week. If no day type is specified (i.e., an empty string is passed),
     * it defaults to using the class's predefined day of week type. This allows for flexibility in representing the current day
     * of the week in different textual formats, suitable for diverse applications like scheduling, reporting, or user interfaces.
     * </p>
     *
     * @param dayType The type of day of week representation ('d' for numeric, 'dy' for abbreviated name in US locale,
     *                'day' for full name in US locale, 'kdy' for abbreviated name in Korean locale, 'kday' for full name in Korean locale).
     *                If empty, defaults to the class's default day of week type.
     * @return A string representing the current day of the week, formatted according to the specified or default day type.
     */
    public static String getSysDayOfWeek(String dayType) {
        return getDayOfWeek(LocalDate.now(), dayType);
    }

    /**********************************************************************
     * for Number
     **********************************************************************/
    /**
     * <p>Calculates the ceiling of a numeric string to the nearest whole number.</p>
     * <p>
     * This method attempts to convert the given string {@code num} representing a numeric value
     * to double and then rounds it up to the nearest whole number. It effectively delegates to
     * {@code getCeil(num, 0)}, ensuring the string is parsed and rounded up accurately, provided
     * it represents a valid numeric value.
     * </p>
     * <p>
     * If {@code num} is not a valid numeric string, this method throws a {@code UtilsException}.
     * This ensures that only valid numeric strings are processed, maintaining the integrity of
     * numerical operations.
     * </p>
     *
     * @param num the numeric string to be rounded up
     * @return The numeric value represented by {@code num} rounded up to the nearest whole number.
     * @throws UtilsException if {@code num} is not a valid numeric string.
     */
    public static double getCeil(String num) {
        return getCeil(num, 0);
    }
    /**
     * <p>Calculates the ceiling of a numeric string to a specified precision.</p>
     * <p>
     * This method first verifies that the input string {@code num} is a valid representation of
     * a numeric value using the {@code isNumeric} method. It then parses the string to a double
     * and applies the ceiling operation based on the specified precision {@code precision}, allowing
     * for consistent rounding logic across various forms of numeric input.
     * </p>
     * <p>
     * If {@code num} is null or does not represent a valid numeric value, a {@code UtilsException}
     * is thrown, emphasizing the requirement for input validation. This approach ensures that rounding
     * operations are only performed on strings that can be accurately converted to numeric values.
     * </p>
     *
     * @param num the numeric string to be rounded up
     * @param precision the number of decimal places to round up to
     * @return The numeric value represented by {@code num} rounded up to the specified {@code precision}.
     * @throws UtilsException if {@code num} is not a valid numeric string.
     */
    public static double getCeil(String num, int precision) {
        if(num == null || !isNumeric(num)) {
            throw new UtilsException("Please enter a valid number.");
        }
        return getCeil(Double.parseDouble(num), precision);
    }
    /**
     * <p>Calculates the ceiling of an integer to the nearest whole number.</p>
     * <p>
     * This method converts the given integer number {@code num} to double and applies the ceiling
     * operation to round it up to the nearest whole number. It effectively delegates to 
     * {@code getCeil((double) num, 0)}, ensuring that any integer is accurately represented as 
     * a double value and then rounded up, even though the input is inherently already at whole 
     * number precision.
     * </p>
     * <p>
     * This approach maintains consistency in rounding operations across different numeric types 
     * and simplifies scenarios where numeric values of mixed types are rounded using a uniform 
     * method signature.
     * </p>
     *
     * @param num the integer number to be rounded up
     * @return The integer number {@code num} rounded up to the nearest whole number, represented as a double.
     */
    public static double getCeil(int num) {
        return getCeil((double) num, 0);
    }
    /**
     * <p>Calculates the ceiling of an integer to a specified precision.</p>
     * <p>
     * This method converts the given integer number {@code num} to double and rounds it up to the
     * nearest value based on the specified precision {@code precision}. By calling 
     * {@code getCeil((double) num, precision)}, it allows for the consistent application of rounding 
     * logic across various numeric inputs, including those where a fractional precision is desired 
     * for an integer value.
     * </p>
     * <p>
     * Specifying a precision effectively enables rounding to a certain number of decimal places 
     * in a floating-point representation, accommodating use cases such as adjustments in measurements 
     * or unit conversions that require precision beyond whole numbers.
     * </p>
     *
     * @param num the integer number to be rounded up
     * @param precision the number of decimal places to round up to
     * @return The integer number {@code num} rounded up to the specified {@code precision}, represented as a double.
     */
    public static double getCeil(int num, int precision) {
        return getCeil((double) num, precision);
    }
    /**
     * <p>Calculates the ceiling of a long number to the nearest whole number.</p>
     * <p>
     * This method converts the given long number {@code num} to double and rounds it up
     * to the nearest whole number by invoking {@code getCeil((double) num, 0)}.
     * The conversion to double and subsequent rounding ensure that the result is accurately
     * represented as the smallest integer greater than or equal to {@code num}, despite the 
     * input being already an integer type.
     * </p>
     * <p>
     * This utility is useful for cases where a long integer needs to be operated on or compared
     * within the context of floating-point arithmetic or where consistency in method signatures
     * and rounding behaviors is desired across different numeric types.
     * </p>
     *
     * @param num the long number to be rounded up
     * @return The long number {@code num} rounded up to the nearest whole number, represented as a double.
     */
    public static double getCeil(long num) {
        return getCeil((double) num, 0);
    }
    /**
     * <p>Calculates the ceiling of a long number to a specified precision.</p>
     * <p>
     * Although the input is a long integer and inherently lacks a fractional component, this method
     * provides a unified interface for ceiling operations across numeric types. It converts the given 
     * long number {@code num} to double and rounds it up to the nearest value dictated by the specified
     * precision {@code precision} by calling {@code getCeil((double) num, precision)}. This operation
     * is particularly useful for applying consistent rounding logic across various numeric inputs or for
     * preparing long values for display or further calculation in contexts that do incorporate fractional
     * precision.
     * </p>
     * <p>
     * For instance, specifying a precision greater than 0 effectively rounds the number to a specified
     * number of decimal places in a floating-point representation, which might be useful in scenarios 
     * involving scale conversions or unit adjustments.
     * </p>
     *
     * @param num the long number to be rounded up
     * @param precision the number of decimal places to round up to, represented as a double
     * @return The long number {@code num} rounded up to the specified {@code precision}, represented as a double.
     */
    public static double getCeil(long num, int precision) {
        return getCeil((double) num, precision);
    }
    /**
     * <p>Calculates the ceiling of a float number to the nearest whole number.</p>
     * <p>
     * This method converts the given float number {@code num} to double and rounds it up
     * to the nearest whole number by calling {@code getCeil((double) num, 0)}.
     * It effectively disregards any fractional part of the number, ensuring the result
     * is the smallest integer greater than or equal to {@code num}.
     * </p>
     * <p>
     * This utility simplifies rounding float numbers up to whole values, commonly used in scenarios
     * where fractional parts are not relevant or desired.
     * </p>
     *
     * @param num the float number to be rounded up
     * @return The float number {@code num} rounded up to the nearest whole number.
     */
    public static double getCeil(float num) {
        return getCeil((double) num, 0);
    }
    /**
     * <p>Calculates the ceiling of a float number to a specified precision.</p>
     * <p>
     * This method converts the given float number {@code num} to double and rounds it up
     * to the nearest value based on the specified precision {@code precision} by calling
     * {@code getCeil((double) num, precision)}. Precision refers to the number of digits
     * to the right of the decimal point to consider for rounding up the number.
     * </p>
     * <p>
     * For example, if {@code num} is 123.456f and {@code precision} is 2, the method returns 123.46.
     * This utility is particularly useful for financial calculations, measurements, or anywhere precise
     * control over numerical rounding is required.
     * </p>
     *
     * @param num the float number to be rounded up
     * @param precision the number of decimal places to round up to
     * @return The float number {@code num} rounded up to the specified {@code precision}.
     */
    public static double getCeil(float num, int precision) {
        return getCeil((double) num, precision);
    }
    /**
     * <p>Calculates the ceiling of a number to the nearest whole number.</p>
     * <p>
     * This method rounds up the given number {@code num} to the nearest whole number by calling
     * {@code getCeil(num, 0)}. It effectively disregards any fractional part of the number,
     * ensuring the result is the smallest integer greater than or equal to {@code num}.
     * </p>
     * <p>
     * This utility simplifies rounding numbers up to whole values, commonly used in scenarios where
     * fractional parts are not relevant or desired.
     * </p>
     *
     * @param num the number to be rounded up
     * @return The number {@code num} rounded up to the nearest whole number.
     */
    public static double getCeil(double num) {
        return getCeil(num, 0);
    }
    /**
     * <p>Calculates the ceiling of a number to a specified precision.</p>
     * <p>
     * This method rounds up the given number {@code num} to the nearest value based on the specified
     * precision {@code precision}. Precision refers to the number of digits to the right of the decimal
     * point to consider for rounding up the number. The method uses the {@code Math.ceil} function after
     * scaling the number by a factor of 10 raised to the power of the specified precision.
     * </p>
     * <p>
     * For example, if {@code num} is 123.45678 and {@code precision} is 2, the method returns 123.46.
     * </p>
     * <p>
     * This utility is particularly useful for financial calculations, measurements, or anywhere precise
     * control over numerical rounding is required.
     * </p>
     *
     * @param num the number to be rounded up
     * @param precision the number of decimal places to round up to
     * @return The number {@code num} rounded up to the specified {@code precision}.
     */
    public static double getCeil(double num, int precision) {
        double factor = Math.pow(10, precision);
        return Math.ceil(num * factor) / factor;
    }

    /**
     * <p>Calculates the floor of a numeric string to the nearest whole number.</p>
     * <p>
     * This method rounds down the numeric value represented by the string {@code num} to the nearest whole number.
     * It acts as a convenience wrapper by converting the string to a double and calling {@code getFloor(Double.parseDouble(num), 0)}.
     * This operation is useful for handling numeric calculations on string inputs without manual conversion.
     * </p>
     * <p>
     * It assumes that {@code num} is a valid numeric string. If {@code num} is not a valid numeric string,
     * a {@code UtilsException} will be thrown, indicating the need for a valid numeric input.
     * </p>
     *
     * @param num the numeric string to be rounded down
     * @return The numeric value of {@code num} rounded down to the nearest whole number, returned as a double.
     * @throws UtilsException if {@code num} is null or not a valid numeric string.
     */
    public static double getFloor(String num) {
        return getFloor(num, 0);
    }
    /**
     * <p>Calculates the floor of a numeric string to a specified precision.</p>
     * <p>
     * This method rounds down the numeric value represented by the string {@code num} based on the specified
     * precision {@code precision}. The string is first validated to ensure it represents a valid numeric value
     * and then parsed to a double for the rounding operation. Precision determines the number of digits to the
     * right of the decimal point to consider for rounding down.
     * </p>
     * <p>
     * For example, if {@code num} is "123.456" and {@code precision} is 2, the method returns 123.45.
     * This utility is particularly useful for precise numerical operations on string-based numeric inputs.
     * </p>
     *
     * @param num the numeric string to be rounded down
     * @param precision the number of decimal places to round down to
     * @return The numeric value of {@code num} rounded down to the specified {@code precision}, returned as a double.
     * @throws UtilsException if {@code num} is null or not a valid numeric string.
     */
    public static double getFloor(String num, int precision) {
        if(num == null || !isNumeric(num)) {
            throw new UtilsException("Please enter a valid number.");
        }
        return getFloor(Double.parseDouble(num), precision);
    }
    /**
     * <p>Calculates the floor of an integer number to the nearest whole number.</p>
     * <p>
     * This method rounds down the given integer number {@code num} to the nearest whole number by converting
     * it to double and calling {@code getFloor((double) num, 0)}. Since {@code num} is already an integer, this
     * operation effectively returns {@code num} without any change, but in a double format.
     * </p>
     * <p>
     * This utility is particularly useful for ensuring consistency in data types during numerical operations, 
     * especially when integrating integer values with calculations that require floating-point precision.
     * </p>
     *
     * @param num the integer number to be rounded down
     * @return The integer number {@code num} as a double, without any change to its value.
     */
    public static double getFloor(int num) {
        return getFloor((double) num, 0);
    }
    /**
     * <p>Calculates the floor of an integer number to a specified precision.</p>
     * <p>
     * This method rounds down the given integer number {@code num} to the nearest value based on the specified
     * precision {@code precision}. While the input is an integer and typically does not involve decimal points, 
     * converting it to double and applying precision effectively simulates the rounding down operation to a 
     * specified number of decimal places.
     * </p>
     * <p>
     * This is particularly useful for calculations that require the integer to be treated as a fixed-point number
     * or when scaling integer values for integration with floating-point arithmetic operations.
     * </p>
     *
     * @param num the integer number to be conceptually rounded down
     * @param precision the number of decimal places to consider for the rounding down operation
     * @return The integer number {@code num} rounded down to the specified {@code precision}, returned as a double.
     */
    public static double getFloor(int num, int precision) {
        return getFloor((double) num, precision);
    }
    /**
     * <p>Calculates the floor of a long number to the nearest whole number.</p>
     * <p>
     * This method rounds down the given long number {@code num} to the nearest whole number by converting
     * it to double and calling {@code getFloor((double) num, 0)}. It ensures that the result is the largest 
     * integer less than or equal to {@code num}, effectively disregarding any need for fractional parts, 
     * which are not applicable for long values.
     * </p>
     * <p>
     * This method simplifies the process of converting long integers to their equivalent double representation 
     * for rounding down to the nearest whole number, especially useful in scenarios requiring numerical operations 
     * on long integers.
     * </p>
     *
     * @param num the long number to be rounded down
     * @return The long number {@code num} rounded down to the nearest whole number, returned as a double.
     */
    public static double getFloor(long num) {
        return getFloor((double) num, 0);
    }
    /**
     * <p>Calculates the floor of a long number to a specified precision.</p>
     * <p>
     * Despite the fact that long integers do not have fractional parts, this method allows for the conceptual 
     * application of precision to a long number {@code num} by converting it to double and rounding it down 
     * to a specified number of decimal places {@code precision}. This can be useful in scenarios where long 
     * integers are being used in calculations that benefit from or require the simulation of precision control.
     * </p>
     * <p>
     * For example, treating {@code num} as a fixed-point number where precision may simulate fractions of units 
     * or for adjusting scale in computational contexts.
     * </p>
     * <p>
     * Note: While the input is a long integer and typically does not involve fractions, this method provides a 
     * way to apply rounding down operations with conceptual decimal precision.
     * </p>
     *
     * @param num the long number to be conceptually rounded down
     * @param precision the conceptual number of decimal places to apply in the rounding down process
     * @return The conceptually rounded down value of {@code num} to the specified {@code precision}, returned as a double.
     */
    public static double getFloor(long num, int precision) {
        return getFloor((double) num, precision);
    }
    /**
     * <p>Calculates the floor of a float number to the nearest whole number.</p>
     * <p>
     * This method rounds down the given float number {@code num} to the nearest whole number by converting
     * it to double and calling {@code getFloor((double) num, 0)}. It effectively disregards any fractional 
     * part of the number, ensuring the result is the largest integer less than or equal to {@code num}.
     * </p>
     * <p>
     * This utility is useful in scenarios where fractional parts of a float number are not relevant or desired.
     * </p>
     *
     * @param num the float number to be rounded down
     * @return The float number {@code num} rounded down to the nearest whole number, returned as a double.
     */
    public static double getFloor(float num) {
        return getFloor((double) num, 0);
    }
    /**
     * <p>Calculates the floor of a float number to a specified precision.</p>
     * <p>
     * This method rounds down the given float number {@code num} to the nearest value based on the specified 
     * precision {@code precision}. The number is converted to double for precise calculations. Precision refers 
     * to the number of digits to the right of the decimal point to consider for rounding down the number. 
     * </p>
     * <p>
     * For example, if {@code num} is 123.456f and {@code precision} is 2, the method returns 123.45.
     * </p>
     * <p>
     * This utility is especially useful for financial calculations, measurements, or anywhere precise control 
     * over numerical rounding of float numbers is required.
     * </p>
     *
     * @param num the float number to be rounded down
     * @param precision the number of decimal places to round down to
     * @return The float number {@code num} rounded down to the specified {@code precision}, returned as a double.
     */
    public static double getFloor(float num, int precision) {
        return getFloor((double) num, precision);
    }
    /**
     * <p>Calculates the floor of a number to the nearest whole number.</p>
     * <p>
     * This method rounds down the given number {@code num} to the nearest whole number by calling
     * {@code getFloor(num, 0)}. It effectively disregards any fractional part of the number,
     * ensuring the result is the largest integer less than or equal to {@code num}.
     * </p>
     * <p>
     * This utility simplifies rounding numbers down to whole values, commonly used in scenarios where
     * fractional parts are not relevant or desired.
     * </p>
     *
     * @param num the number to be rounded down
     * @return The number {@code num} rounded down to the nearest whole number.
     */
    public static double getFloor(double num) {
        return getFloor(num, 0);
    }
    /**
     * <p>Calculates the floor of a number to a specified precision.</p>
     * <p>
     * This method rounds down the given number {@code num} to the nearest value based on the specified
     * precision {@code precision}. Precision refers to the number of digits to the right of the decimal
     * point to consider for rounding down the number. The method uses the {@code Math.floor} function after
     * scaling the number by a factor of 10 raised to the power of the specified precision.
     * </p>
     * <p>
     * For example, if {@code num} is 123.45678 and {@code precision} is 2, the method returns 123.45.
     * </p>
     * <p>
     * This utility is particularly useful for financial calculations, measurements, or anywhere precise
     * control over numerical rounding is necessary, especially when it's important to avoid overestimating
     * values.
     * </p>
     *
     * @param num the number to be rounded down
     * @param precision the number of decimal places to round down to
     * @return The number {@code num} rounded down to the specified {@code precision}.
     */
    public static double getFloor(double num, int precision) {
        double factor = Math.pow(10, precision);
        return Math.floor(num * factor) / factor;
    }

    /**
     * <p>Calculates the rounding of a numeric string to the nearest whole number.</p>
     * <p>
     * This method rounds the numeric value represented by the string {@code num} to the nearest whole number.
     * It is a convenience method that parses the string as a double and delegates to {@code getRound(double num, int precision)}
     * with a precision of 0. It ensures that numeric strings can be rounded without manual conversion.
     * </p>
     * <p>
     * If {@code num} is not a valid numeric string, a {@code UtilsException} is thrown to indicate the error.
     * This ensures that only valid numeric strings are processed for rounding.
     * </p>
     *
     * @param num the numeric string to be rounded
     * @return The numeric value of {@code num} rounded to the nearest whole number, returned as a double.
     * @throws UtilsException if {@code num} is null or not a valid numeric string.
     */
    public static double getRound(String num) {
        return getRound(num, 0);
    }
    /**
     * <p>Calculates the rounding of a numeric string to a specified precision.</p>
     * <p>
     * This method rounds the numeric value represented by the string {@code num} based on the specified
     * precision {@code precision}. The string is parsed to a double, and rounding is performed to the nearest
     * value dictated by the precision. This method allows for precise control over the rounding of numeric strings.
     * </p>
     * <p>
     * An {@code UtilsException} is thrown if {@code num} is null or not a numeric string, ensuring that the method
     * operates on valid numeric inputs only.
     * </p>
     *
     * @param num the numeric string to be rounded
     * @param precision the number of decimal places to round to
     * @return The numeric value of {@code num} rounded to the specified {@code precision}, returned as a double.
     * @throws UtilsException if {@code num} is null or not a valid numeric string.
     */
    public static double getRound(String num, int precision) {
        if(num == null || !isNumeric(num)) {
            throw new UtilsException("Please enter a valid number.");
        }
        return getRound(Double.parseDouble(num), precision);
    }
    /**
     * <p>Calculates the rounding of an integer number to the nearest whole number.</p>
     * <p>
     * Rounds the given integer {@code num} to the nearest whole number. This method is effectively redundant for
     * integer inputs but is provided for consistency in method signatures and to facilitate rounding when the same
     * operation is needed across various numeric types.
     * </p>
     *
     * @param num the integer number to be rounded
     * @return The integer {@code num} as a double, without change.
     */
    public static double getRound(int num) {
        return getRound((double) num, 0);
    }
    /**
     * <p>Calculates the rounding of an integer number to a specified precision.</p>
     * <p>
     * This method rounds the given integer number {@code num} to a precision specified by {@code precision}. While
     * integers inherently lack decimal places, this method facilitates operations requiring the adjustment of scale
     * or the simulation of precision in integer calculations by converting to double.
     * </p>
     *
     * @param num the integer number to be rounded
     * @param precision the conceptual number of decimal places to apply in the rounding process
     * @return The integer {@code num} rounded to the specified {@code precision}, returned as a double.
     */
    public static double getRound(int num, int precision) {
        return getRound((double) num, precision);
    }
    /**
     * <p>Calculates the rounding of a long number to the nearest whole number.</p>
     * <p>
     * This method rounds the given long number {@code num} to the nearest whole number by converting
     * it to double and calling {@code getRound((double) num, 0)}. It is useful for rounding long numbers
     * to whole values without losing precision in the conversion process.
     * </p>
     *
     * @param num the long number to be rounded
     * @return The long number {@code num} rounded to the nearest whole number, returned as a double.
     */
    public static double getRound(long num) {
        return getRound((double) num, 0);
    }
    /**
     * <p>Calculates the rounding of a long number to a specified precision.</p>
     * <p>
     * This method rounds the given long number {@code num} to the nearest value based on the specified
     * precision {@code precision} by converting it to double. This allows for precise control over rounding
     * long numbers, even when dealing with decimal precision.
     * </p>
     *
     * @param num the long number to be rounded
     * @param precision the number of decimal places to round to
     * @return The long number {@code num} rounded to the specified {@code precision}, returned as a double.
     */
    public static double getRound(long num, int precision) {
        return getRound((double) num, precision);
    }
    /**
     * <p>Calculates the rounding of a float number to the nearest whole number.</p>
     * <p>
     * This method rounds the given float number {@code num} to the nearest whole number by converting
     * it to double and calling {@code getRound((double) num, 0)}. It ensures accuracy in rounding float
     * numbers to whole values.
     * </p>
     *
     * @param num the float number to be rounded
     * @return The float number {@code num} rounded to the nearest whole number, returned as a double.
     */
    public static double getRound(float num) {
        return getRound((double) num, 0);
    }
    /**
     * <p>Calculates the rounding of a float number to a specified precision.</p>
     * <p>
     * This method rounds the given float number {@code num} to the nearest value based on the specified
     * precision {@code precision} by converting it to double. This is particularly useful for achieving
     * precise rounding results with float numbers.
     * </p>
     *
     * @param num the float number to be rounded
     * @param precision the number of decimal places to round to
     * @return The float number {@code num} rounded to the specified {@code precision}, returned as a double.
     */
    public static double getRound(float num, int precision) {
        return getRound((double) num, precision);
    }
    /**
     * <p>Calculates the rounding of a number to the nearest whole number.</p>
     * <p>
     * This method rounds the given number {@code num} to the nearest whole number by calling
     * {@code getRound(num, 0)}. It effectively rounds the number to the closest integer, handling
     * both positive and negative numbers appropriately.
     * </p>
     * <p>
     * This utility simplifies rounding numbers to whole values, commonly used in general arithmetic
     * operations, reporting, or displaying numbers where fractional parts are not desired.
     * </p>
     *
     * @param num the number to be rounded
     * @return The number {@code num} rounded to the nearest whole number.
     */
    public static double getRound(double num) {
        return getRound(num, 0);
    }
    /**
     * <p>Calculates the rounding of a number to a specified precision.</p>
     * <p>
     * This method rounds the given number {@code num} to the nearest value based on the specified
     * precision {@code precision}. Precision refers to the number of digits to the right of the decimal
     * point to consider for rounding. The method uses mathematical rounding after scaling the number
     * by a factor of 10 raised to the power of the specified precision.
     * </p>
     * <p>
     * For example, if {@code num} is 123.45678 and {@code precision} is 2, the method returns 123.46.
     * </p>
     * <p>
     * This utility is particularly useful for financial calculations, measurements, or any scenario
     * where precise control over numerical rounding is required.
     * </p>
     *
     * @param num the number to be rounded
     * @param precision the number of decimal places to round to
     * @return The number {@code num} rounded to the specified {@code precision}.
     */
    public static double getRound(double num, int precision) {
        double factor = Math.pow(10, precision);
        return Math.round(num * factor) / factor;
    }

    /**
     * <p>Truncates a numeric string to the nearest whole number.</p>
     * <p>
     * This method parses the numeric string {@code num} and truncates it to the nearest whole number.
     * It is a convenience wrapper that converts the string to a double and calls
     * {@code getTrunc(Double.parseDouble(num), 0)}. This method ensures that numeric strings can be
     * truncated without manual conversion, simplifying operations on string-based numeric data.
     * </p>
     * <p>
     * Throws a {@code UtilsException} if {@code num} is null or not a valid numeric string, ensuring
     * that the operation is performed on valid numeric inputs only.
     * </p>
     *
     * @param num the numeric string to be truncated
     * @return The numeric value of {@code num} truncated to the nearest whole number, returned as a double.
     * @throws UtilsException if {@code num} is null or not a valid numeric string.
     */
    public static double getTrunc(String num) {
        return getTrunc(num, 0);
    }
    /**
     * <p>Truncates a numeric string to a specified precision.</p>
     * <p>
     * This method truncates the numeric value represented by the string {@code num} based on the specified
     * precision {@code precision}. After validating and parsing {@code num} as a double, the method truncates
     * it to retain a specified number of decimal places, discarding the rest. This utility is especially useful
     * for precise numerical operations on string-based inputs where truncation without rounding is required.
     * </p>
     * <p>
     * An {@code UtilsException} is thrown if {@code num} is null or fails to be recognized as a numeric string,
     * ensuring the integrity of the operation.
     * </p>
     *
     * @param num the numeric string to be truncated
     * @param precision the number of decimal places to retain before truncating
     * @return The numeric value of {@code num} truncated to the specified {@code precision}, returned as a double.
     * @throws UtilsException if {@code num} is null or not a valid numeric string.
     */
    public static double getTrunc(String num, int precision) {
        if(num == null || !isNumeric(num)) {
            throw new UtilsException("Please enter a valid number.");
        }
        return getTrunc(Double.parseDouble(num), precision);
    }
    /**
     * <p>Truncates an integer number to the nearest whole number.</p>
     * <p>
     * Rounds down the given integer {@code num} to the nearest whole number. This operation effectively
     * returns {@code num} as it is, but converts it to a double for consistency with other numeric types.
     * It simplifies the handling of integer numbers when a uniform operation across various data types is required.
     * </p>
     *
     * @param num the integer number to be truncated
     * @return The integer {@code num} represented as a double.
     */
    public static double getTrunc(int num) {
        return getTrunc((double) num, 0);
    }
    /**
     * <p>Truncates an integer number to a specified precision.</p>
     * <p>
     * This method applies the concept of truncation to an integer {@code num} by converting it to double
     * and truncating it to a specified precision. Although integers do not have decimal parts, this method
     * allows for consistent treatment of numeric types in operations requiring a uniform approach to truncation.
     * </p>
     *
     * @param num the integer number to be truncated
     * @param precision the conceptual number of decimal places for the operation
     * @return The integer {@code num} truncated to the specified {@code precision}, returned as a double.
     */
    public static double getTrunc(int num, int precision) {
        return getTrunc((double) num, precision);
    }
    /**
     * <p>Truncates a long number to the nearest whole number.</p>
     * <p>
     * Truncates the given long number {@code num} by converting it to double and calling
     * {@code getTrunc((double) num, 0)}. Since {@code num} is already a whole number, this method
     * effectively returns {@code num} without any modification but in double format.
     * </p>
     *
     * @param num the long number to be truncated
     * @return The long number {@code num} as a double.
     */
    public static double getTrunc(long num) {
        return getTrunc((double) num, 0);
    }
    /**
     * <p>Truncates a long number to a specified precision.</p>
     * <p>
     * Although truncating a long number to a specified precision might not seem applicable due to the
     * absence of fractional parts in long values, this method allows for conceptual truncation by converting
     * {@code num} to double and applying the specified precision. It facilitates operations that might require
     * treating the long value as a fixed-point number.
     * </p>
     *
     * @param num the long number to be truncated
     * @param precision the conceptual number of decimal places to truncate to
     * @return The truncated value of {@code num}, as a double, to the specified {@code precision}.
     */
    public static double getTrunc(long num, int precision) {
        return getTrunc((double) num, precision);
    }
    /**
     * <p>Truncates a float number to the nearest whole number.</p>
     * <p>
     * This method truncates the given float number {@code num} to the nearest whole number by converting
     * it to double and calling {@code getTrunc((double) num, 0)}. It removes the fractional part of the
     * float number, returning the result as a double.
     * </p>
     *
     * @param num the float number to be truncated
     * @return The float number {@code num} truncated to the nearest whole number, as a double.
     */
    public static double getTrunc(float num) {
        return getTrunc((double) num, 0);
    }
    /**
     * <p>Truncates a float number to a specified precision.</p>
     * <p>
     * This method truncates the given float number {@code num} to the nearest value based on the specified
     * precision {@code precision} by converting it to double. This allows for precise control over the truncation
     * of float numbers, maintaining the specified number of decimal places while discarding the rest.
     * </p>
     *
     * @param num the float number to be truncated
     * @param precision the number of decimal places to retain before truncating
     * @return The float number {@code num} truncated to the specified {@code precision}, as a double.
     */
    public static double getTrunc(float num, int precision) {
        return getTrunc((double) num, precision);
    }
    /**
     * <p>Truncates a number to the nearest whole number.</p>
     * <p>
     * This method truncates the given number {@code num} to the nearest whole number by discarding any
     * fractional part. It calls {@code getTrunc(num, 0)}, effectively simplifying the operation to remove
     * the decimal part of the number.
     * </p>
     * <p>
     * This utility is useful for operations where the exact integer part of a number is needed without
     * rounding up or down the fractional part.
     * </p>
     *
     * @param num the number to be truncated
     * @return The number {@code num} truncated to the nearest whole number.
     */
    public static double getTrunc(double num) {
        return getTrunc(num, 0);
    }
    /**
     * <p>Truncates a number to a specified precision.</p>
     * <p>
     * This method truncates the given number {@code num} to the nearest value based on the specified
     * precision {@code precision}. Precision refers to the number of digits to the right of the decimal
     * point to retain before truncating the remaining fractional part. The operation is performed by scaling
     * the number by a factor of 10 raised to the power of the specified precision, truncating the result
     * using {@code Math.floor}, and then scaling back.
     * </p>
     * <p>
     * For example, if {@code num} is 123.45678 and {@code precision} is 2, the method returns 123.45.
     * This utility is especially useful in financial calculations, measurements, or any scenario where
     * removing the fractional part beyond a certain precision without rounding is required.
     * </p>
     *
     * @param num the number to be truncated
     * @param precision the number of decimal places to retain before truncating
     * @return The number {@code num} truncated to the specified {@code precision}.
     */
    public static double getTrunc(double num, int precision) {
        double factor = Math.pow(10, precision);
        return Math.floor(num * factor) / factor;
    }

    /**********************************************************************
     * for String
     **********************************************************************/
    /**
     * <p>Calculates the byte length of a given string based on UTF-8 encoding.</p>
     * <p>
     * This method determines the byte length of the specified string when encoded in UTF-8. Different characters
     * consume a varying number of bytes depending on their code point value. ASCII characters (code points up to 0x7F)
     * consume one byte each, characters with code points up to 0x7FF consume a number of bytes as defined by
     * {@code LESSOREQ_0X7FF_BYTE}, characters with code points up to 0xFFFF consume bytes as defined by
     * {@code LESSOREQ_0XFFFF_BYTE}, and characters with code points above 0xFFFF consume bytes as defined by
     * {@code GREATER_0XFFFF_BYTE}. This method is useful for understanding the storage requirements of a string
     * in systems where UTF-8 encoding is used.
     * </p>
     *
     * @param str The string whose byte length is to be calculated.
     * @return The byte length of the specified string when encoded in UTF-8.
     */
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

    /**
     * <p>Truncates a string to ensure its byte length does not exceed a specified limit when encoded in UTF-8.</p>
     * <p>
     * This method calculates the byte length of a string as it would be encoded in UTF-8 and truncates it
     * to ensure that its size does not exceed a specified byte length ({@code cutByte}). The method accounts
     * for the variable byte length of UTF-8 characters: ASCII characters (up to 0x7F) consume one byte, while
     * characters with higher code points may consume two, three, or four bytes, as defined by the constants
     * {@code LESSOREQ_0X7FF_BYTE}, {@code LESSOREQ_0XFFFF_BYTE}, and {@code GREATER_0XFFFF_BYTE}, respectively.
     * If the string needs to be truncated, it is cut at the character that would cause the byte length to exceed
     * the specified limit, ensuring the result fits within the desired byte size.
     * </p>
     * <p>
     * This utility is useful in scenarios where string length must be controlled strictly according to byte size,
     * such as data storage, network transmission, or when interacting with systems or protocols that impose byte-length
     * limits on strings.
     * </p>
     *
     * @param str The string to be truncated.
     * @param cutByte The maximum byte length of the string when encoded in UTF-8.
     * @return A substring of the original string, truncated such that its UTF-8 encoded byte length does not exceed
     *         the specified limit. Returns an empty string if the original string is {@code null}.
     */
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

    /**
     * <p>Equalizes the spacing within a string to meet a specified total length.</p>
     * <p>
     * This method adjusts the spacing within a given string to ensure its total length matches a specified value.
     * If the original string's length is already greater than or equal to the specified length, the string is returned
     * as is. Otherwise, spaces are evenly distributed between characters to extend the string's length to the specified
     * value. The algorithm calculates the number of spaces to add between each character, including additional spaces
     * to distribute as evenly as possible if the total number of spaces cannot be divided equally.
     * </p>
     * <p>
     * This utility can be useful in formatting output for console applications, reports, or any other scenarios where
     * uniform string length or alignment is desirable. It ensures that strings are presented in a more visually consistent
     * manner, improving readability and aesthetic appeal.
     * </p>
     *
     * @param str The string to be formatted.
     * @param length The desired total length of the string after formatting.
     * @return A new string with spaces added between characters to achieve the specified total length. If the original string
     *         is {@code null}, {@code null} is returned. If the original string's length meets or exceeds the specified length,
     *         the original string is returned without modification.
     */
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

    /**
     * <p>Prepends padding to a string to reach a specified total length.</p>
     * <p>
     * This method prepends the specified pad string to the beginning of the given string until the total length
     * of the resulting string matches the specified length. If the original string is {@code null}, it is treated
     * as an empty string. The pad string must not be {@code null} or empty; if it is, an {@code IllegalArgumentException}
     * is thrown. If the length of the original string equals or exceeds the specified length, the original string
     * is returned without any padding.
     * </p>
     * <p>
     * This utility is particularly useful for text formatting where alignment or specific string lengths are required,
     * such as in reports, console outputs, or when generating fixed-width records.
     * </p>
     *
     * @param str The string to be padded.
     * @param padStr The string used for padding. Must not be {@code null} or empty.
     * @param length The desired total length of the padded string.
     * @return A new string with {@code padStr} prepended to {@code str} until the total length is {@code length}.
     *         If {@code str} is already equal to or longer than {@code length}, {@code str} is returned as is.
     * @throws IllegalArgumentException If {@code padStr} is null or empty.
     */
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

    /**
     * <p>Appends padding to the right of a string to achieve a specified total length.</p>
     * <p>
     * This method appends the specified pad string to the end of the given string until the total length
     * of the resulting string reaches the specified length. If the original string is {@code null}, it is
     * treated as an empty string. The method requires a non-null and non-empty pad string; otherwise, an
     * {@code IllegalArgumentException} is thrown. If the original string's length is already equal to or
     * exceeds the specified length, the original string is returned without any modifications.
     * </p>
     * <p>
     * This utility is particularly valuable for aligning text output or ensuring strings meet a minimum
     * length requirement, commonly needed in formatting for display, reports, or data files with fixed-width fields.
     * </p>
     *
     * @param str The string to be padded.
     * @param padStr The string used for padding. It must not be {@code null} or empty.
     * @param length The desired total length of the padded string.
     * @return A new string with {@code padStr} appended to the right of {@code str} until the total length
     *         reaches {@code length}. If the original string's length is equal to or greater than the specified
     *         length, the original string is returned as is.
     * @throws IllegalArgumentException If {@code padStr} is null or empty.
     */
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

    /**
     * <p>Returns the default value if the provided value is {@code null}.</p>
     * <p>
     * This method checks if the given value is {@code null} and returns a specified default value in that case. 
     * If the provided value is not {@code null}, it returns the value itself. This utility is particularly useful 
     * for preventing {@code NullPointerException} by ensuring that operations or further method calls are performed 
     * on non-null objects. It simplifies code by reducing the need for explicit null checks and conditional statements.
     * </p>
     * <p>
     * The method is generic, allowing for use with objects of any type. This versatility makes it a valuable tool 
     * for default value assignment across a wide range of programming scenarios, including handling optional 
     * configuration settings, default method parameters, or fallback values in data processing.
     * </p>
     *
     * @param <T> The type of the value and default value.
     * @param value The value to check for {@code null}.
     * @param defaultValue The default value to return if {@code value} is {@code null}.
     * @return The original value if it is not {@code null}; otherwise, the default value.
     */
    public static <T> T nvl(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

    /**
     * <p>Formats an integer value as a string using a default format pattern.</p>
     * <p>
     * This method is a simplified version of {@code getNumberFormat(String value, String format)} that takes an integer value.
     * It converts the integer value to a string and formats it using the default format pattern defined in {@code NUMBER_FORMATTER}.
     * </p>
     * 
     * @param value The integer value to format.
     * @return A formatted string representation of the integer value using the default format pattern.
     */
    public static String getNumberFormat(int value) {
        return getNumberFormat(String.valueOf(value), "");
    }
    /**
     * <p>Formats an integer value as a string based on the specified format pattern.</p>
     * <p>
     * This method is an overload of {@code getNumberFormat(String value, String format)} that accepts an integer value and a format pattern.
     * It converts the integer value to a string and formats it according to the specified format pattern.
     * </p>
     * 
     * @param value The integer value to format.
     * @param format The format pattern to apply to the number.
     * @return A formatted string representation of the integer value according to the specified format pattern.
     */
    public static String getNumberFormat(int value, String format) {
        return getNumberFormat(String.valueOf(value), format);
    }
    /**
     * <p>Formats a long integer value as a string using a default format pattern.</p>
     * <p>
     * This method converts a long integer value to a string and formats it using the default format pattern specified by {@code NUMBER_FORMATTER}.
     * It is a convenient way to format long integer values without specifying a custom format pattern, leveraging the default formatting settings.
     * </p>
     *
     * @param value The long integer value to format.
     * @return A formatted string representation of the long integer value using the default format pattern.
     */
    public static String getNumberFormat(long value) {
        return getNumberFormat(String.valueOf(value), "");
    }
    /**
     * <p>Formats a long integer value as a string based on the specified format pattern.</p>
     * <p>
     * This method provides a way to format a long integer value according to a specified format pattern. It converts the long integer value to a string 
     * and then applies the given format, allowing for customized formatting of long integer numbers. This is particularly useful when specific formatting
     * rules are required, such as including thousand separators, specifying decimal precision, or adding prefixes or suffixes.
     * </p>
     *
     * @param value The long integer value to format.
     * @param format The format pattern to apply to the number.
     * @return A formatted string representation of the long integer value according to the specified format pattern.
     */
    public static String getNumberFormat(long value, String format) {
        return getNumberFormat(String.valueOf(value), format);
    }
    /**
     * <p>Formats a float value as a string using a default format pattern.</p>
     * <p>
     * This method converts a float value to a string and formats it using the default format pattern specified by {@code NUMBER_FORMATTER}.
     * It provides a straightforward approach to format float values for display without requiring a custom format pattern, using predefined formatting settings.
     * </p>
     *
     * @param value The float value to format.
     * @return A formatted string representation of the float value using the default format pattern.
     */
    public static String getNumberFormat(float value) {
        return getNumberFormat(String.valueOf(value), "");
    }
    /**
     * <p>Formats a float value as a string based on the specified format pattern.</p>
     * <p>
     * This method enables formatting of a float value according to a specified format pattern. By converting the float value to a string and applying the given format, 
     * it allows for custom formatting of float numbers. This flexibility is useful for applications requiring specific number formats, such as scientific notation, fixed decimal places, 
     * or inclusion of thousand separators.
     * </p>
     *
     * @param value The float value to format.
     * @param format The format pattern to apply to the number.
     * @return A formatted string representation of the float value according to the specified format pattern.
     */
    public static String getNumberFormat(float value, String format) {
        return getNumberFormat(String.valueOf(value), format);
    }
    /**
     * <p>Formats a double value as a string using a default format pattern.</p>
     * <p>
     * Converts a double precision floating-point number to a string and formats it using the default format pattern specified by {@code NUMBER_FORMATTER}.
     * This method simplifies the formatting of double values for textual display, adhering to predefined formatting standards without requiring a custom format pattern.
     * </p>
     *
     * @param value The double value to format.
     * @return A formatted string representation of the double value using the default format pattern.
     */
    public static String getNumberFormat(double value) {
        return getNumberFormat(String.valueOf(value), "");
    }
    /**
     * <p>Formats a double value as a string based on the specified format pattern.</p>
     * <p>
     * Allows for the custom formatting of a double precision floating-point number according to a specified format pattern. By converting the double value to a string 
     * and then applying the given format, it offers tailored formatting solutions for double numbers, accommodating needs for specific numerical representations such as 
     * scientific notation, precise decimal control, or formatting with thousand separators.
     * </p>
     *
     * @param value The double value to format.
     * @param format The format pattern to apply to the number, allowing for custom numerical representations.
     * @return A formatted string representation of the double value according to the specified format pattern.
     */
    public static String getNumberFormat(double value, String format) {
        return getNumberFormat(String.valueOf(value), format);
    }
    /**
     * <p>Formats a string representing a numeric value using a default format pattern.</p>
     * <p>
     * This method formats a string that represents a numeric value by using the default format pattern specified by {@code NUMBER_FORMATTER}.
     * It is designed to format numeric strings without the need for specifying a custom format pattern, relying on predefined formatting settings
     * to ensure consistency and readability of numeric data presented in textual form.
     * </p>
     * <p>
     * This utility is particularly useful for formatting strings containing numeric information when the format pattern does not need to be customized,
     * such as displaying numbers in a user interface or preparing data for reports where standard numeric formatting is required.
     * </p>
     *
     * @param value The string representing the numeric value to format.
     * @return A formatted string representation of the numeric value using the default format pattern.
     */
    public static String getNumberFormat(String value) {
        return getNumberFormat(value, "");
    }
    /**
     * <p>Formats a numeric value as a string based on the specified format pattern.</p>
     * <p>
     * This method converts a string representation of a number into a formatted string according to the given format pattern. 
     * The format pattern can include prefixes, numeric patterns, and suffixes. If the format pattern is invalid or not provided, 
     * a default format specified by {@code NUMBER_FORMATTER} is used. The method supports formatting for integer and decimal parts, 
     * as well as handling special cases like percentages.
     * </p>
     * <p>
     * The method ensures that the numeric string is formatted precisely, with control over the display of decimal points, 
     * thousand separators, and padding with zeros. It throws {@code IllegalArgumentException} if the format pattern is invalid, 
     * or if there is an attempt to mix '#' and '0' in the decimal format.
     * </p>
     * <p>
     * This utility is particularly valuable for preparing numeric data for display in user interfaces, reports, or data exports, 
     * allowing for consistent and readable number formatting across different parts of an application or dataset.
     * </p>
     *
     * @param value The numeric value to format, represented as a string.
     * @param format The format pattern to apply to the number. It supports prefixes, numeric patterns including '#' for optional digits and '0' for padding, and suffixes.
     * @return A formatted string representation of the number according to the specified format pattern. If the input value is null or empty, and the format pattern indicates zero padding, '0' is returned; otherwise, an empty string is returned.
     * @throws IllegalArgumentException If the format pattern is invalid or mixes '#' and '0' in the decimal format.
     */
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
        decimalFormat.setMaximumFractionDigits(0);
        return decimalFormat.format(number);
    }

    private static String formatDecimalPart(BigDecimal number, String format, int scale) {
        if (format.isEmpty()) return "";

        number = number.setScale(scale, RoundingMode.HALF_UP);
        String numberAsString = number.toPlainString();

        int dotIndex = numberAsString.indexOf('.');
        String decimalPart = dotIndex != -1 ? numberAsString.substring(dotIndex + 1) : "";

        if (format.contains("#")) {
            while (decimalPart.endsWith("0")) {
                decimalPart = decimalPart.substring(0, decimalPart.length() - 1);
            }
            return decimalPart;
        } else if (format.contains("0")) {
            return String.format("%-" + scale + "s", decimalPart).replace(' ', '0');
        }
        return decimalPart;
    }

    /**
     * <p>Removes all characters from a string except digits.</p>
     * <p>
     * This method processes a given string and removes all characters that are not numeric digits (0-9).
     * It is useful for extracting purely numeric data from strings that may contain a mix of letters, 
     * punctuation, and other non-numeric characters. If the input string is {@code null}, the method returns an empty string.
     * </p>
     * <p>
     * This utility can be particularly beneficial in scenarios where numeric values are embedded within 
     * text or when preparing strings for numeric operations or comparisons. By ensuring that the resulting 
     * string contains only digits, it simplifies further numeric processing or validation tasks.
     * </p>
     *
     * @param str The string from which to remove all non-numeric characters.
     * @return A string containing only the numeric digits found in the input string. Returns an empty string if the input is {@code null}.
     */
    public static String getRemoveExceptNumbers(String str) {
        if (str == null) return "";
        return str.replaceAll("[^0-9]", "");
    }

    /**
     * <p>Removes all numeric digits from a string.</p>
     * <p>
     * This method processes a given string and removes all numeric digits (0-9), leaving behind a string consisting only of non-numeric characters.
     * It is particularly useful for scenarios where numeric information needs to be stripped from text data, such as when cleaning up or formatting strings
     * for textual analysis, logging, or displaying information that should not include numbers.
     * </p>
     * <p>
     * The utility ensures that any numeric data embedded within the input string is effectively removed, facilitating the management of strings where
     * numeric data is irrelevant or should be excluded. If the input string is {@code null}, the method gracefully handles it by returning an empty string.
     * </p>
     *
     * @param str The string from which to remove all numeric digits.
     * @return A string devoid of any numeric digits, containing only the remaining characters from the input string. Returns an empty string if the input is {@code null}.
     */
    public static String getRemoveNumbers(String str) {
        if (str == null) return "";
        return str.replaceAll("[0-9]", "");
    }

    /**
     * <p>Returns the reversed version of the input string.</p>
     * <p>
     * This method takes a given string and returns a new string that is the reverse of the input. 
     * It is useful for various text processing tasks where the order of characters in a string needs to be inverted,
     * such as in certain cryptographic applications, palindromic checks, or simply for creating reverse-effect text displays.
     * </p>
     * <p>
     * The method handles {@code null} inputs gracefully by returning an empty string, ensuring that the operation does not result in an exception.
     * This makes it a safe and reliable utility for reversing strings in any context where the input might be uncertain.
     * </p>
     *
     * @param str The string to be reversed.
     * @return A new string that is the reverse of the input string. If the input is {@code null}, returns an empty string.
     */
    public static String getReverse(String str) {
        if (str == null) return "";
        return new StringBuilder(str).reverse().toString();
    }

    /**********************************************************************
     * etc
     **********************************************************************/
    /**
     * <p>Returns the client's IP address from an HttpServletRequest.</p>
     * <p>
     * This method extracts the client's IP address from a given HttpServletRequest object. It checks several HTTP headers that may contain
     * the client's IP address, considering scenarios where the client is connecting through a proxy or a load balancer. The method searches
     * through a predefined list of headers, preferring headers that are more likely to contain the original client IP address (e.g., "X-Forwarded-For").
     * </p>
     * <p>
     * In environments where requests pass through proxies or load balancers, the client's IP address might be contained in one of several
     * headers, depending on the configuration. This method attempts to accommodate various configurations by checking multiple headers in a
     * specific order. If the IP address is found in any of these headers, it is returned; otherwise, the method falls back to using
     * HttpServletRequest's getRemoteAddr(), which should return the direct connection IP address.
     * </p>
     * <p>
     * Note: In cases where the "X-Forwarded-For" header contains multiple IP addresses, separated by commas, this method returns the first
     * IP address in the list, which is typically the original client's IP address.
     * </p>
     *
     * @param request The HttpServletRequest from which to extract the client's IP address.
     * @return The client's IP address as a string. If no IP address is found in the headers, returns the IP address from getRemoteAddr().
     */
    public static String getClientIpAddress(HttpServletRequest request) {
        String[] headers = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
        };

        for (String header : headers) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                return ip.split(",")[0].trim();
            }
        }

        return request.getRemoteAddr();
    }

    /**
     * <p>Extracts the file extension from a URL or file name.</p>
     * <p>
     * This method parses a given string representing a file path or URL and extracts the file extension, if present. 
     * It identifies the file extension as the part of the string that follows the last period ('.') character. This utility 
     * is useful for determining the type of a file based on its extension, which can be critical for file handling, 
     * validation, and processing routines.
     * </p>
     * <p>
     * The method checks for the presence of a period in the string to determine if an extension exists. If no period is found,
     * or if the period is the last character in the string (indicating no extension), the method returns an empty string. 
     * Otherwise, it returns the substring following the last period, which represents the file extension.
     * </p>
     *
     * @param path The file path or URL from which to extract the file extension.
     * @return The extracted file extension as a string. Returns an empty string if no extension is found.
     */
    public static String getFileExtension(String path) {
        int lastIndex = path.lastIndexOf(".");
        
        if (lastIndex == -1 || lastIndex == path.length() - 1) {
            return "";
        }
        
        return path.substring(lastIndex + 1);
    }

    /**
     * <p>Extracts the file name without its extension from a URL or file path.</p>
     * <p>
     * This method parses a given string representing a file path or URL and extracts the file name, excluding the extension if present. 
     * The method first isolates the portion of the string following the last slash ('/') character, which is considered the file name with 
     * its extension. It then removes the extension by extracting the part of the file name before the last period ('.') character.
     * </p>
     * <p>
     * This utility is particularly valuable for processing file names for display, logging, or any scenario where the file extension is 
     * irrelevant or should not be included. It supports file paths and URLs that include directories or paths represented by slash characters.
     * </p>
     * <p>
     * If the input string does not contain a period indicating an extension, or if there are no slash characters indicating directory paths,
     * the method returns the string as is, assuming it represents a file name without an extension.
     * </p>
     *
     * @param path The file path or URL from which to extract the file name without the extension.
     * @return The file name extracted from the given path or URL, excluding the extension. If no extension or slash is found, returns the input string.
     */
    public static String getFileName(String path) {
        int lastSlashIndex = path.lastIndexOf("/");
        String fileName = (lastSlashIndex == -1) ? path : path.substring(lastSlashIndex + 1);

        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1) return fileName;

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
    /**
     * <p>Retrieves a property value from a specified property file.</p>
     * <p>
     * This method looks up and returns the value associated with a given key in a specified properties file. 
     * It leverages the {@code loadProperties} method to load the properties file based on the provided file name. 
     * This utility is useful for accessing configuration values stored in properties files, allowing for dynamic 
     * application configurations without hardcoding values.
     * </p>
     *
     * @param propertyFileName The name of the property file from which to read the value (excluding the ".properties" extension).
     * @param key The key whose value is to be fetched from the property file.
     * @return The value associated with the specified key in the property file. Returns {@code null} if the key is not found.
     */
    public static String getPropertyValue(String propertyFileName, String key) {
        Properties prop = loadProperties(propertyFileName);
        return prop.getProperty(key);
    }

    /**
     * <p>Returns the value associated with a specific key in a properties file as a Map.</p>
     * <p>
     * This method fetches the value for a specified key from a given properties file and returns it within a {@link Map}.
     * If the key exists in the properties file, the key-value pair is added to the map; otherwise, an empty map is returned.
     * This utility facilitates the retrieval of configuration values from properties files, providing a simple way to access 
     * specific settings and their values in a structured format.
     * </p>
     * <p>
     * The method uses {@code loadProperties} to load the properties file and then searches for the specified key within that file.
     * This approach allows for dynamic access to configuration values without hardcoding them into the application code, 
     * supporting more flexible and maintainable application configurations.
     * </p>
     *
     * @param propertyFileName The name of the properties file from which to retrieve the value (excluding the ".properties" extension).
     * @param key The key for which the value is to be retrieved.
     * @return A {@link Map} containing the key-value pair if the key exists in the properties file; an empty map otherwise.
     */
    public static Map<String, String> getProperty(String propertyFileName, String key) {
        Properties prop = loadProperties(propertyFileName);
        Map<String, String> resultMap = new HashMap<>();
        String value = prop.getProperty(key);
        if (value != null) {
            resultMap.put(key, value);
        }
        return resultMap;
    }

    /**
     * <p>Returns all keys from a properties file as a Set.</p>
     * <p>
     * This method loads a properties file specified by the file name and returns a {@link Set} containing all keys found within the file.
     * It provides a convenient way to access all the keys in a properties file, which can be useful for iterating over configurations,
     * performing bulk operations, or simply auditing the available settings.
     * </p>
     * <p>
     * The returned set of keys allows for easy traversal and manipulation of property keys, supporting scenarios where an application
     * needs to dynamically query or update its configuration based on the keys present in a properties file.
     * </p>
     *
     * @param propertyFileName The name of the properties file from which to retrieve all keys (excluding the ".properties" extension).
     * @return A {@link Set} containing all the keys in the specified properties file.
     */
    public static Set<String> getPropertyKeys(String propertyFileName) {
        Properties prop = loadProperties(propertyFileName);
        return new HashSet<>(prop.stringPropertyNames());
    }

    /**
     * <p>Returns all key-value pairs from a properties file as a Map.</p>
     * <p>
     * This method loads a properties file specified by the file name and constructs a {@link Map} containing all key-value pairs found within the file.
     * It facilitates accessing the entire set of configurations defined in a properties file, enabling the application to dynamically read and utilize
     * configuration settings.
     * </p>
     * <p>
     * The returned map provides a straightforward means to access and manipulate property values based on their keys, supporting use cases where an application
     * needs to adapt its behavior or settings based on the configurations specified in external properties files. This method simplifies the process of
     * retrieving multiple configurations at once, offering a comprehensive view of the properties defined.
     * </p>
     *
     * @param propertyFileName The name of the properties file from which to retrieve all key-value pairs (excluding the ".properties" extension).
     * @return A {@link Map} containing all key-value pairs in the specified properties file.
     */
    public static Map<String, String> getProperties(String propertyFileName) {
        Properties prop = loadProperties(propertyFileName);
        Map<String, String> map = new HashMap<>();
        for (String key : prop.stringPropertyNames()) {
            map.put(key, prop.getProperty(key));
        }
        return map;
    }

    /**
     * <p>Returns a Map of properties matching a specific key prefix from a properties file.</p>
     * <p>
     * This method loads a properties file specified by the file name and filters the properties based on a key prefix. 
     * It constructs a {@link Map} containing all key-value pairs where the key starts with the specified prefix, 
     * allowing for targeted retrieval of configuration settings grouped by a common prefix. This is particularly useful 
     * for organizing configuration settings into categories or modules and accessing them selectively.
     * </p>
     * <p>
     * The method iterates over all properties in the file, selecting those whose keys match the specified prefix condition ('key%'). 
     * This enables applications to dynamically adjust their configurations or behaviors based on a subset of the properties 
     * without needing to parse and process the entire properties file.
     * </p>
     *
     * @param propertyFileName The name of the properties file from which to retrieve the matching key-value pairs (excluding the ".properties" extension).
     * @param keyPrefix The prefix used to filter the properties by their keys.
     * @return A {@link Map} containing all key-value pairs that match the specified key prefix in the properties file.
     */
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