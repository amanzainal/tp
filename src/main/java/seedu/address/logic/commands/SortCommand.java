package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.student.Student;
import seedu.address.model.student.TestNameEqualsKeywordPredicate;


/**
 * Sorts and lists all students in address book by a specified test's grade.
 * Can sort in increasing (default) or decreasing order.
 */
public class SortCommand extends Command {

    public static final String COMMAND_WORD = "sort";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sorts all students by the specified test's grade "
            + "and displays them as a list with index numbers.\n"
            + "Parameters: [test-name] (optional: /r)\n"
            + "/r means reverse so it will sort in decreasing order, highest grade first.\n"
            + "Example: " + COMMAND_WORD + " ca1 /r";

    private final TestNameEqualsKeywordPredicate predicate;
    private final boolean isReverse;

    /**
     * Sorts and lists all students in the address book by a specified test's grade.
     * This command allows sorting in either increasing (default) or decreasing order based on the
     * grades for a particular test.
     * The sort order can be reversed by specifying the /r flag.
     *
     * Usage:
     * - sort [test-name]: Sorts the list of students by the specified test's grade in increasing order.
     * - sort [test-name] /r: Sorts the list of students by the specified test's grade in decreasing order, with higher
     * grades appearing first.
     *
     * Example:
     * - sort Math /r: Sorts students based on their Math test grades in decreasing order.
     */
    public SortCommand(TestNameEqualsKeywordPredicate predicate, boolean isReverse) {
        this.predicate = predicate;
        this.isReverse = isReverse;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        Comparator<Student> gradeComparator = (student1, student2) -> {
            int grade1 = student1.getGradeForTest(predicate.keyword);
            int grade2 = student2.getGradeForTest(predicate.keyword);
            return Integer.compare(grade1, grade2);
        };

        if (isReverse) {
            gradeComparator = gradeComparator.reversed();
        }

        model.sortFilteredStudentList(gradeComparator, predicate);
        return new CommandResult(String.format(Messages.MESSAGE_STUDENTS_LISTED_OVERVIEW,
                model.getFilteredStudentList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof SortCommand)) {
            return false;
        }

        SortCommand otherSortCommand = (SortCommand) other;
        return predicate.equals(otherSortCommand.predicate) && isReverse == otherSortCommand.isReverse;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .add("isReverse", isReverse)
                .toString();
    }
}
