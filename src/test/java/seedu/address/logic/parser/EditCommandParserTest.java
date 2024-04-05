package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY_EDIT;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB_EDIT;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY_EDIT;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_BOB_EDIT;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC_EDIT;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC_EDIT;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC_EDIT;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC_EDIT;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY_EDIT;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY_EDIT;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB_EDIT;
import static seedu.address.logic.commands.CommandTestUtil.TIMESLOT_DESC_TUESDAY_EDIT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TIMESLOT_TUESDAY_1130AM_130PM;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS_EDIT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL_EDIT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE_EDIT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_STUDENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_STUDENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_STUDENT;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditStudentDescriptor;
import seedu.address.model.student.Address;
import seedu.address.model.student.Email;
import seedu.address.model.student.Name;
import seedu.address.model.student.Phone;
import seedu.address.testutil.EditStudentDescriptorBuilder;

public class EditCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);

    private EditCommandParser parser = new EditCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_NAME_AMY, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", EditCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + NAME_DESC_AMY_EDIT, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + NAME_DESC_AMY_EDIT, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_NAME_DESC_EDIT, Name.MESSAGE_CONSTRAINTS); // invalid name
        assertParseFailure(parser, "1" + INVALID_PHONE_DESC_EDIT, Phone.MESSAGE_CONSTRAINTS); // invalid phone
        assertParseFailure(parser, "1" + INVALID_EMAIL_DESC_EDIT, Email.MESSAGE_CONSTRAINTS); // invalid email
        assertParseFailure(parser, "1" + INVALID_ADDRESS_DESC_EDIT, Address.MESSAGE_CONSTRAINTS); // invalid address

        // invalid phone followed by valid email
        assertParseFailure(parser, "1" + INVALID_PHONE_DESC_EDIT + EMAIL_DESC_AMY_EDIT, Phone.MESSAGE_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, "1" + INVALID_NAME_DESC_EDIT + INVALID_EMAIL_DESC_EDIT + VALID_ADDRESS_AMY
                        + VALID_PHONE_AMY,
                Name.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_STUDENT;
        String userInput = targetIndex.getOneBased() + PHONE_DESC_BOB_EDIT + EMAIL_DESC_AMY_EDIT
                + ADDRESS_DESC_AMY_EDIT + NAME_DESC_AMY_EDIT;

        EditCommand.EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor,
                false, false, false,
                false, false, false);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_STUDENT;
        String userInput = targetIndex.getOneBased() + PHONE_DESC_BOB_EDIT + EMAIL_DESC_AMY_EDIT;

        EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder().withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_AMY).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor,
                false, false, false,
                false, false, false);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // name
        Index targetIndex = INDEX_THIRD_STUDENT;
        String userInput = targetIndex.getOneBased() + NAME_DESC_AMY_EDIT;
        EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder().withName(VALID_NAME_AMY).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor,
                false, false, false,
                false, false, false);
        assertParseSuccess(parser, userInput, expectedCommand);

        // phone
        userInput = targetIndex.getOneBased() + PHONE_DESC_AMY_EDIT;
        descriptor = new EditStudentDescriptorBuilder().withPhone(VALID_PHONE_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor,
                false, false, false,
                false, false, false);
        assertParseSuccess(parser, userInput, expectedCommand);

        // email
        userInput = targetIndex.getOneBased() + EMAIL_DESC_AMY_EDIT;
        descriptor = new EditStudentDescriptorBuilder().withEmail(VALID_EMAIL_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor,
                false, false, false,
                false, false, false);
        assertParseSuccess(parser, userInput, expectedCommand);

        // address
        userInput = targetIndex.getOneBased() + ADDRESS_DESC_AMY_EDIT;
        descriptor = new EditStudentDescriptorBuilder().withAddress(VALID_ADDRESS_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor,
                false, false, false,
                false, false, false);
        assertParseSuccess(parser, userInput, expectedCommand);

        // timeslots
        userInput = targetIndex.getOneBased() + TIMESLOT_DESC_TUESDAY_EDIT;
        descriptor = new EditStudentDescriptorBuilder().withTimeslots(VALID_TIMESLOT_TUESDAY_1130AM_130PM).build();
        expectedCommand = new EditCommand(targetIndex, descriptor,
                false, false, false,
                false, false, false);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_failure() {
        // More extensive testing of duplicate parameter detections is done in
        // AddCommandParserTest#parse_repeatedNonTimeslotValue_failure()

        // valid followed by invalid
        Index targetIndex = INDEX_FIRST_STUDENT;
        String userInput = targetIndex.getOneBased() + INVALID_PHONE_DESC_EDIT + PHONE_DESC_BOB_EDIT;

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE_EDIT));

        // invalid followed by valid
        userInput = targetIndex.getOneBased() + PHONE_DESC_BOB_EDIT + INVALID_PHONE_DESC_EDIT;

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE_EDIT));

        // mulltiple valid fields repeated
        userInput = targetIndex.getOneBased() + PHONE_DESC_AMY_EDIT + ADDRESS_DESC_AMY_EDIT + EMAIL_DESC_AMY_EDIT
                + PHONE_DESC_AMY_EDIT + ADDRESS_DESC_AMY_EDIT + EMAIL_DESC_AMY_EDIT + PHONE_DESC_BOB_EDIT
                + ADDRESS_DESC_BOB_EDIT + EMAIL_DESC_BOB_EDIT;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(
                        PREFIX_PHONE_EDIT, PREFIX_EMAIL_EDIT, PREFIX_ADDRESS_EDIT));

        // multiple invalid values
        userInput = targetIndex.getOneBased() + INVALID_PHONE_DESC_EDIT + INVALID_ADDRESS_DESC_EDIT
                + INVALID_EMAIL_DESC_EDIT + INVALID_PHONE_DESC_EDIT
                + INVALID_ADDRESS_DESC_EDIT + INVALID_EMAIL_DESC_EDIT;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(
                        PREFIX_PHONE_EDIT, PREFIX_EMAIL_EDIT, PREFIX_ADDRESS_EDIT));
    }
}
