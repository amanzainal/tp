---
  layout: default.md
  title: "User Guide"
  pageNav: 3
---

# TutorTrack User Guide

TutorTrack is a **desktop app for managing contacts, optimized for use via a  Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type fast, TutorTrack can get your contact management tasks done faster than traditional GUI apps.

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `11` or above installed in your Computer.

1. Download the latest `addressbook.jar` from [here](https://github.com/se-edu/addressbook-level3/releases).

1. Copy the file to the folder you want to use as the _home folder_ for your AddressBook.

1. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar addressbook.jar` command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

   * `list` : Lists all contacts.

   * `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01` : Adds a contact named `John Doe` to the Address Book.

   * `delete 3` : Deletes the 3rd contact shown in the current list.

   * `clear` : Deletes all contacts.

   * `exit` : Exits the app.

1. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<box type="info" seamless>

**Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [t/TIMESLOTS]` can be used as `n/John Doe t/Saturday 4pm-6pm` or as `n/John Doe`.
* e.g `n/NAME [g/GRADE]` can be used as `n/John Doe g/ca1: 100` or as `n/John Doe`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[t/TIMESLOTS]…​` can be used as ` ` (i.e. 0 times), `t/Saturday 4pm-6pm`, `t/Saturday 4pm-6pm t/Monday 4pm-6pm` etc.
* e.g. `[g/GRADE]…​` can be used as ` ` (i.e. 0 times), `g/ca1: 100`, `g/ca1: 100 g/ca1: 99` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</box>

### Viewing help : `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`


### Adding a student: `add`

Adds a student to the address book.

Format: `add n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/TIMESLOTS]…​ [g/GRADE]…​`

<box type="tip" seamless>

**Tip:** A student can have any number of timeslots or grades (including 0)
</box>

Examples:
* `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01`
* `add n/Betsy Crowe e/betsycrowe@example.com a/Newgate Prison p/1234567 t/Saturday 5pm-7pm g/ca1: 2`

### Listing all students : `list`

Shows a list of all students in the address book.

Format: `list`

### Editing a student : `edit`

Edits an existing student in the TutorTrack.

Format: `edit INDEX !n | !p | !e | !a | !t | !g`

* Edits the student at the specified `INDEX`. The index refers to the index number shown in the displayed student list. The index **must be a positive integer** 1, 2, 3, …​
* Only one of the variable fields must be provided.
* Existing values will be updated to the input values.
* Adding and modifying multiple timeslots and grades are allowed, i.e. they will not be overwritten.

Examples:
*  `edit 1 !p` will display `edit 1 phone: {previous phone number}`, prompting user to make changes to the phone number of the 1st student.
*  `edit 2 !g` will display `edit 2 grade: {previous grades}`, prompting user to add (single or multiple) additional grades or modify current grades of the 2nd student.

### Locating students by name: `find`

Finds students whose names contain any of the given keywords.

Format: `find KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive. e.g `hans` will match `Hans`
* The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
* Only the name is searched.
* The search functionality allow for partial matches e.g. `Han` will match `Hans`
* Students matching at least one keyword will be returned (i.e. `OR` search).
  e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`

Examples:
* `find John` returns `john` and `John Doe`
* `find alex david` returns `Alex Yeoh`, `David Li`<br>
  ![result for 'find alex david'](images/findAlexDavidResult.png)

### Filtering students by timeslots: `filter`

Filters students whose timeslots contain any of the given keywords.

Format: `filter KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive. e.g `saturday` will match `Saturday`
* The order of the keywords does not matter. e.g. `Saturday Monday` will match `Monday Saturday`
* Only the timeslots are searched.
* The search functionality allow for partial matches e.g. `Sat` will match `Saturday`

Examples:
* `filter Sat` returns all students with `Saturday` in their timeslot
* `filter Saturday Sunday` returns all students with `Saturday` or `Sunday` in their timeslot<br>
![result for 'find alex david'](images/filterSaturdayResult.png)

### Sorting students by grades: `sort`

Sorts the list of students in the address book by the grades of a specified test.

Format: `sort TEST_NAME [/r]`

Sorts students based on their grades for the specified TEST_NAME.
If /r is specified, the students will be sorted in ascending order (lowest grade first). Otherwise, students will be sorted in ascending order (highest grade first).
The TEST_NAME is case-sensitive.
Examples:

sort Math sorts the students by their grades in Math in ascending order.
sort English /r sorts the students by their grades in English in descending order.

### Deleting a student : `delete`

Deletes the specified student from the address book.

Format: `delete INDEX`

* Deletes the student at the specified `INDEX`.
* The index refers to the index number shown in the displayed student list.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `list` followed by `delete 2` deletes the 2nd student in the address book.
* `find Betsy` followed by `delete 1` deletes the 1st student in the results of the `find` command.

### Clearing all entries : `clear`

Clears all entries from the address book.

Format: `clear`

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

TutorTrack data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

TutorTrack data are saved automatically as a JSON file `[JAR file location]/data/addressbook.json`. Advanced users are welcome to update data directly by editing that data file.

<box type="warning" seamless>

**Caution:**
If your changes to the data file makes its format invalid, TutorTrack will discard all data and start with an empty data file at the next run.  Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause TutorTrack to behave in unexpected ways (e.g., if a value entered is outside the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</box>


--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous AddressBook home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.

--------------------------------------------------------------------------------------------------------------------

## Command summary

| Action     | Format, Examples                                                                                                                                                                               |
|------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Add**    | `add n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/TIMESLOT]…​ [g/GRADE]…​` <br> e.g., `add n/John Doe p/98765432 e/johnd@example.com a/311, Clementi Ave 2, #02-25 t/Saturday 4pm-6pm g/ca1: 50` |
| **Clear**  | `clear`                                                                                                                                                                                        |
| **Delete** | `delete INDEX`<br> e.g., `delete 3`                                                                                                                                                            |
| **Edit**   | `edit INDEX !n or !p or !e or !a or !t or !g` <br> e.g.,`edit 2 !n`                                                                                                                            |
| **Find**   | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find James Jake`                                                                                                                                     |
| **Filter** | `filter KEYWORD [MORE_KEYWORDS]`<br> e.g., `filter Saturday Sunday`                                                                                                                            |
| **Sort**   | `sort TEST_NAME [/r]` <br> e.g., `sort Math`, `sort English /r`                                                                                                                                |
| **List**   | `list`                                                                                                                                                                                         |
| **Help**   | `help`                                                                                                                                                                                         |
