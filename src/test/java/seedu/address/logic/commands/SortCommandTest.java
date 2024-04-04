package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_STUDENTS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalStudents.ALICE;
import static seedu.address.testutil.TypicalStudents.BENSON;
import static seedu.address.testutil.TypicalStudents.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.student.Student;
import seedu.address.model.student.TestNameEqualsKeywordPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FilterCommand}.
 */
public class SortCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        TestNameEqualsKeywordPredicate firstPredicate =
                new TestNameEqualsKeywordPredicate("first");
        TestNameEqualsKeywordPredicate secondPredicate =
                new TestNameEqualsKeywordPredicate("second");

        SortCommand sortFirstCommand = new SortCommand(firstPredicate, false);
        SortCommand sortSecondCommand = new SortCommand(secondPredicate, false);

        // same object -> returns true
        assertTrue(sortFirstCommand.equals(sortFirstCommand));

        // same values -> returns true
        SortCommand sortFirstCommandCopy = new SortCommand(firstPredicate, false);
        assertTrue(sortFirstCommand.equals(sortFirstCommandCopy));

        // different types -> returns false
        assertFalse(sortFirstCommand.equals(1));

        // null -> returns false
        assertFalse(sortFirstCommand.equals(null));

        // different student -> returns false
        assertFalse(sortFirstCommand.equals(sortSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_nostudentFound() {
        String expectedMessage = String.format(MESSAGE_STUDENTS_LISTED_OVERVIEW, 0);
        TestNameEqualsKeywordPredicate predicate = preparePredicate(" ");
        Comparator<Student> gradeComparator = (student1, student2) -> {
            String grade1 = Optional.ofNullable(student1.getGradeForTest(predicate.keyword)).orElse("0");
            String grade2 = Optional.ofNullable(student2.getGradeForTest(predicate.keyword)).orElse("0");
            return grade1.compareTo(grade2);
        };
        SortCommand command = new SortCommand(predicate, false);
        expectedModel.sortFilteredStudentList(gradeComparator, predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredStudentList());
    }

    @Test
    public void execute_multipleKeywords_multiplestudentsFound() {
        String expectedMessage = String.format(MESSAGE_STUDENTS_LISTED_OVERVIEW, 2);
        TestNameEqualsKeywordPredicate predicate = preparePredicate("Ca1");
        Comparator<Student> gradeComparator = (student1, student2) -> {
            String grade1 = Optional.ofNullable(student1.getGradeForTest(predicate.keyword)).orElse("0");
            String grade2 = Optional.ofNullable(student2.getGradeForTest(predicate.keyword)).orElse("0");
            return grade1.compareTo(grade2);
        };
        SortCommand command = new SortCommand(predicate, false);
        expectedModel.sortFilteredStudentList(gradeComparator, predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ALICE, BENSON), model.getFilteredStudentList());
    }

    @Test
    public void toStringMethod() {
        TestNameEqualsKeywordPredicate predicate = new TestNameEqualsKeywordPredicate("keyword");
        Comparator<Student> gradeComparator = (student1, student2) -> {
            String grade1 = Optional.ofNullable(student1.getGradeForTest(predicate.keyword)).orElse("0");
            String grade2 = Optional.ofNullable(student2.getGradeForTest(predicate.keyword)).orElse("0");
            return grade1.compareTo(grade2);
        };
        SortCommand sortCommand = new SortCommand(predicate, false);
        String expected = SortCommand.class.getCanonicalName() + "{predicate=" + predicate + ", "
                + "isReverse=" + false + "}";
        assertEquals(expected, sortCommand.toString());
    }

    /**
     * Parses {@code userInput} into a {@code TestNameEqualsKeywordPredicate}.
     */
    private TestNameEqualsKeywordPredicate preparePredicate(String userInput) {
        return new TestNameEqualsKeywordPredicate(userInput);
    }
}
