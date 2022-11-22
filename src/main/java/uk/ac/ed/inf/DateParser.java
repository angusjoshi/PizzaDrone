package uk.ac.ed.inf;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;

public class DateParser {

    public static LocalDate parseDateString(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            return LocalDate.parse(dateString, formatter);
        } catch(DateTimeParseException e) {
            return null;
        }
    }
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
