package seedu.address.model.grade;

import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;


public class GradeTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Grade(null));
    }

    @Test
    public void constructor_invalidGrade_throwsIllegalArgumentException() {
        String invalidGrade = "";
        assertThrows(IllegalArgumentException.class, () -> new Grade(invalidGrade));
    }

    @Test
    public void isValidGrade() {
        // null timeslot name
        assertThrows(NullPointerException.class, () -> Grade.isValidGrade(null));
    }

}
