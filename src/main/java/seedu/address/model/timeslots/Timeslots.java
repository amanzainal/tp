package seedu.address.model.timeslots;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a Timeslot in the address book.
 * Guarantees: immutable; timeslot is valid as declared in {@link #isValidTimeslot(String)}
 */
public class Timeslots {

    public static final String MESSAGE_CONSTRAINTS = "Timeslot should be of the format: "
        + "DayOfWeek StartTime-EndTime, and adhere to the following constraints:\n"
        + "1. The DayOfWeek is any day from Monday to Sunday.\n"
        + "2. StartTime and EndTime include hours and optional minutes in 12-hour format. \n"
        + "23. StartTime must be earlier than EndTime. "
        + "Minutes, if included, should be separated from hours by a colon. \n"
        + "For example, 'Saturday 4pm-6pm', 'Tuesday 2:30pm-4:30pm'.\n";

    public static final String VALIDATION_REGEX = "^(Monday|Tuesday|Wednesday|Thursday|Friday|Saturday|Sunday) "
        + "(1[012]|[1-9])(:[0-5][0-9])?(am|pm)-(1[012]|[1-9])(:[0-5][0-9])?(am|pm)$";
    private static final DateTimeFormatter[] TIME_FORMATTERS = {
            DateTimeFormatter.ofPattern("h:mma"),
            DateTimeFormatter.ofPattern("ha")
    };
    public final String timeslot;


    /**
     * Constructs a {@code Timeslot}.
     *
     * @param timeslot A valid timeslot.
     */
    public Timeslots(String timeslot) {
        requireNonNull(timeslot);
        checkArgument(isValidTimeslot(timeslot) & isStartTimeBeforeEndTime(timeslot), MESSAGE_CONSTRAINTS);
        this.timeslot = timeslot;
    }

    /**
     * Helper Function to check if time entered is of valid time format.
     *
     * @param timeStr A time as String.
     * @return parsed LocalTime if timeStr is of proper format.
     */
    public static LocalTime parseTime(String timeStr) {
        for (DateTimeFormatter formatter : TIME_FORMATTERS) {
            try {
                return LocalTime.parse(timeStr.toUpperCase(), formatter);
            } catch (DateTimeParseException e) {
                // Try the next formatter
            }
        }
        throw new DateTimeParseException("Could not parse time: " + timeStr, timeStr, 0);
    }

    /**
     * Checks if the start time is before the end time in the timeslot.
     *
     * @param timeslot A valid timeslot string.
     * @return true if the start time is before the end time.
     */
    public static boolean isStartTimeBeforeEndTime(String timeslot) {
        String[] parts = timeslot.split("\\s+");
        if (parts.length < 2) {
            return false;
        }
        String timePart = parts[1];
        String[] times = timePart.split("-");

        try {
            LocalTime startTime = parseTime(times[0]);
            LocalTime endTime = parseTime(times[1]);
            return startTime.isBefore(endTime);
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Returns true if a given string is a valid timeslot.
     */
    public static boolean isValidTimeslot(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Timeslots)) {
            return false;
        }

        Timeslots otherTimeslot = (Timeslots) other;
        return timeslot.equals(otherTimeslot.timeslot);
    }

    @Override
    public int hashCode() {
        return timeslot.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + timeslot + ']';
    }

    public String replace(String oldString, String newString) {
        return timeslot.replace(oldString, newString);
    }
}
