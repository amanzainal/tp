package seedu.address.model.student;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that any of {@code student}'s {@code Grades} matches any of the keywords given.
 */
public class TestNameEqualsKeywordPredicate implements Predicate<Student> {
    public final String keyword;

    public TestNameEqualsKeywordPredicate(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public boolean test(Student student) {
        return student.getGrades().stream()
                .anyMatch(grades -> grades.testName.equals(keyword));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof TestNameEqualsKeywordPredicate)) {
            return false;
        }

        TestNameEqualsKeywordPredicate gradesContainsKeywordsPredicate =
                (TestNameEqualsKeywordPredicate) other;
        return keyword.equals(gradesContainsKeywordsPredicate.keyword);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keyword).toString();
    }
}
