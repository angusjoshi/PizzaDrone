package uk.ac.ed.inf.order;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;

/**
 * Class with some static methods for parsing date strings into LocalDate types
 */
public class DateParser {
    //To hide the default constructor
    private DateParser(){}

    /**
     * Parses a string in the form "yyyy-MM-dd" to a LocalDate type
     * @param dateString the date string
     * @return the date as a LocalDate
     */
    public static LocalDate parseDateString(String dateString) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateString, formatter);
    }

    /**
     * Parses a credit card expiry date in the form "MM/yy". The day in the date is set to be
     * the first of the month.
     * @param cardExpiryString The credit card expiry date as a string
     * @return The date parsed as a LocalDate
     */
    public static LocalDate parseCreditCardExpiry(String cardExpiryString) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("MM/yy")
                .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                .toFormatter();

        try {
            return LocalDate.parse(cardExpiryString, formatter);
        } catch(DateTimeParseException e) {
            return null;
        }
    }
}
