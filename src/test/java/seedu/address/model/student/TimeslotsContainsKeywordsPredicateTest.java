package seedu.address.model.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.StudentBuilder;

public class TimeslotsContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        TimeslotsContainsKeywordsPredicate firstPredicate =
                new TimeslotsContainsKeywordsPredicate(firstPredicateKeywordList);
        TimeslotsContainsKeywordsPredicate secondPredicate =
                new TimeslotsContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        TimeslotsContainsKeywordsPredicate firstPredicateCopy =
                new TimeslotsContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different student -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_timeslotsContainsKeywords_returnsTrue() {
        // One keyword
        TimeslotsContainsKeywordsPredicate predicate = new TimeslotsContainsKeywordsPredicate(Collections
                .singletonList("Saturday"));
        assertTrue(predicate.test(new StudentBuilder().withTimeslots("Saturday 3pm-5pm", "Sunday 2pm-4pm").build()));

        // Multiple keywords
        predicate = new TimeslotsContainsKeywordsPredicate(Arrays.asList("Saturday", "Sunday"));
        assertTrue(predicate.test(new StudentBuilder().withTimeslots("Saturday 3pm-5pm", "Sunday 2pm-4pm").build()));

        // Only one matching keyword
        predicate = new TimeslotsContainsKeywordsPredicate(Arrays.asList("Saturday", "Sunday"));
        assertTrue(predicate.test(new StudentBuilder().withTimeslots("Saturday 3pm-5pm", "Tuesday 2pm-4pm").build()));

        // Mixed-case keywords
        predicate = new TimeslotsContainsKeywordsPredicate(Arrays.asList("sAtuRday", "SunDaY"));
        assertTrue(predicate.test(new StudentBuilder().withTimeslots("Saturday 3pm-5pm", "Sunday 2pm-4pm").build()));
    }

    @Test
    public void test_timeslotsDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        TimeslotsContainsKeywordsPredicate predicate = new TimeslotsContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new StudentBuilder().withTimeslots("Saturday 3pm-5pm").build()));

        // Non-matching keyword
        predicate = new TimeslotsContainsKeywordsPredicate(Arrays.asList("Tuesday"));
        assertFalse(predicate.test(new StudentBuilder().withTimeslots("Saturday 3pm-5pm", "Sunday 2pm-4pm").build()));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("keyword1", "keyword2");
        TimeslotsContainsKeywordsPredicate predicate = new TimeslotsContainsKeywordsPredicate(keywords);

        String expected = TimeslotsContainsKeywordsPredicate.class.getCanonicalName() + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }
}
