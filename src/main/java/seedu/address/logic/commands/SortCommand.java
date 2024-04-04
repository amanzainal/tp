package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.student.Student;

/**
 *
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

    private final String testName;
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
    public SortCommand(String testName, boolean isReverse) {
        this.testName = testName;
        this.isReverse = isReverse;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        Comparator<Student> gradeComparator = (student1, student2) -> {
            String grade1 = student1.getGradeForTest(testName);
            String grade2 = student2.getGradeForTest(testName);
            return grade1.compareTo(grade2);
        };

        if (isReverse) {
            gradeComparator = gradeComparator.reversed();
        }

        model.sortFilteredStudentList(gradeComparator);
        return new CommandResult(String.format(Messages.MESSAGE_STUDENTS_LISTED_OVERVIEW,
            model.getFilteredStudentList().size()));
    }

}
