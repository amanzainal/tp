---
layout: default.md
title: "Developer Guide"
pageNav: 3
---

# TutorTrack Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

This project is based on the AddressBook-Level3 project created by the [SE-EDU initiative](https://se-education.org)

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280">

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/AY2324S2-CS2103-F08-4/tp/blob/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/AY2324S2-CS2103-F08-4/tp/blob/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574"></puml>

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2324S2-CS2103-F08-4/tp/blob/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `StudentListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2324S2-CS2103-F08-4/tp/blob/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2324S2-CS2103-F08-4/tp/blob/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Student` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2324S2-CS2103-F08-4/tp/blob/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/AY2324S2-CS2103-F08-4/tp/blob/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the address book data i.e., all `Student` objects (which are contained in a `UniqueStudentList` object).
* stores the currently 'selected' `Student` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Student>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Timeslot` list and `Grade` list in the `AddressBook`, which `Student` references. This allows `AddressBook` to only require one `Timeslot` object per unique timeslot and one `Grade` object per unique grade, instead of each `Student` needing their own `Timeslot` and `Grade` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/AY2324S2-CS2103-F08-4/tp/blob/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.addressbook.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

###  Grade parameter

####  Implementation
Grades
* are an optional attribute of every student
* can vary from student to student, both in quantity of grades stored and test scores
* of the form: [test name: grade] where
  + test name is non-empty
  + the grade is a value from 0 to 100, representing the percentage gotten in the test rounded to the nearest whole number

Grades can be added and edited with the add and edit command with the prefix `g/` and `!g` respectively.

Given below are example usages scenario of how adding grades behaves.
* The user executes `add n/David … g/ca1: 100` command to add a new student with one grade, with the test name being `ca1` and the score attained `100`
* The user executes `add n/David … g/ca1: 100 g/ ca2: 50` command to add a new student with two grade,
with the first test name being `ca1` and the score attained `100` and the second test name being `ca2` and the score attained `50`
* The user executes `add n/David …` command without any g/ prefixes, adding a new student with no grades, which is permissible.


###  Timeslot parameter

####  Implementation
Timeslots
* are an optional attribute of every student
* of the form: [DayOfWeek StartTime-EndTime] where
    + The DayOfWeek is any day from Monday to Sunday.
    + StartTime and EndTime include hours and optional minutes in 12-hour format
    + Minutes, if included, should be separated from hours by a colon
    + For example, 'Saturday 4pm-6pm', 'Tuesday 2:30pm-4:30pm'

Timeslots can be added and edited with the add and edit command with the prefix `t/` and `!t` respectively.

Given below are example usages scenario of how adding timeslots behaves.
* The user executes `add n/David … t/Saturday 4pm-6pm` command to add a new student with one timeslot on Saturdays 4pm-6pm
* The user executes `add n/David … t/Saturday 4pm-6pm t/Monday 11:30am-1:30pm` command to add a new student with two timeslots,
  one timeslot on Saturdays 4pm-6pm and the other on Mondays 11:30am-1:30pm.
* The user executes `add n/David …` command without any t/ prefixes, adding a new student with no timeslots, which is permissible.


### Sort Feature

#### Implementation

The sort feature allows the user to sort students based on the grades of a specified test. It is facilitated by `SortCommand`, which extends `Command` and uses a comparator to sort the students in either ascending or descending order based on their grades.

Given below are example usage scenarios of how the sort behaves:

1. The user executes `sort Math` command to sort the students by their Math test grades, where the highest graded student will be shown first (i.e. descending order). The `SortCommand` will create a comparator that compares students based on their Math grades and then uses this comparator to sort the list of students in the `Model`.
2. The user executes `sort English /r` command to sort the students by their English test grades in ascending order. The `SortCommand` recognizes the `/r` flag which indicates that the sorting should be done in reverse order. It then creates a reversed comparator and sorts the students accordingly.

Here's a simplified class diagram of the sort feature:

<puml src="diagrams/SortFeatureClassDiagram.puml" alt="Class Diagram for Sort Feature" />


#### Design Considerations

**Aspect: Sorting of students based on test grades**

* **Alternative 1 (current choice):** Use a comparator to sort students by grades.
  * Pros: Easy to implement and understand. Makes use of Java's built-in sorting algorithm, ensuring efficiency.
  * Cons: Only sorts based on one attribute at a time. If students have the same grade, their order is determined by the list's original order, which might not be desirable in all cases.

* **Alternative 2:** Implement a custom sorting algorithm that can sort based on multiple attributes.
  * Pros: Allows for more complex sorting criteria, such as sorting by name if grades are equal.
  * Cons: More difficult to implement and test. Increased complexity could lead to bugs.

#### Usage

* Sorting students by test grades allows tutors to quickly identify top performers or those who might need additional support for a specific test.
* This feature enhances the tutor's ability to manage and track student performance efficiently.


###  Filter feature

####  Implementation

The filter mechanism allows the user to filter through students based on their timeslots.
It is facilitated by `TimeslotsContainsKeywordsPredicate`, which extends `Predicate<Student>` with a list of keywords stored internally as `keywords`.
Additionally, it implements the following operations:
* `TimeslotsContainsKeywordsPredicate#test()` — Returns a boolean value for every student in the addressBook,
returning `true` if any of the student's timeslots matches the keywords, and `false` otherwise.

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/FilterSequenceDiagram.puml" alt="FilterSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `FilterCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

####  Rationale

A tutor would most likely only need to filter by timeslots to check if there are any clashes in timeslots in students, or if he wants an update on 
how many classes he has on a particular day. Filtering by other parameters would hardly be used, as instead a tutor would more likely use a `find` command.


#### Design considerations:

* **Alternative 1 (current choice):** Filter only by timeslot
    * Pros: Easy to implement. 
    * Cons: May not be robust enough for users who want to filter by other parameters

* **Alternative 2:** Filter by more parameters
    * Pros: Will be more robust. 
    * Cons: More work is required to implement. 

* **Alternative 3 (for future updates):** Smart filtering that can filter for timings between startTime and endTime
    * Pros: Will be more robust and could be convenient for a user to know if he would be teaching at a particular time.
    * Cons: More work is required to implement.


### Improvised Edit Feature

#### Implementation

The edit feature allows the user to modify information about the students. 
It is facilitated by `EditCommand`, which extends `Command` and uses `get` and `set` methods to retrieve and update information respectively.

Given below are example usage scenarios of how the edit behaves:

1. `edit 1 !p` will display `edit 1 phone: {PHONE_NUMBER}`, prompting user to make changes to the current phone number of the 1st student.
2. `edit 2 !g` will display `edit 2 grade: {GRADE}`, prompting user to modify (add/edit/delete) current grades of the 2nd student.

Here's a simplified sequence diagram of the edit feature:

<puml src="diagrams/EditSequenceDiagram.puml" alt="EditSequenceDiagram" />

#### Design Considerations

**Aspect: Edit student's information**

* **Alternative 1 (current choice):** Display the current information user wants to edit and use only a single edit command to modify any information about the student (including timeslots and grades).
    * Pros: Users only need to use one edit command, making the process straightforward and intuitive. It also allows modification of any information without accidentally overwriting pre-existing data since current values are displayed in the Command Box, ensuring data integrity.
    * Cons: Users may find it challenging to edit specific types of information within the single edit command when there are many data fields.

* **Alternative 2:** Add extra method such as AddGrade or AddTimeslot to solve the issue of overwriting pre-existing information.
    * Pros: Divides functionality into separate methods, potentially making the codebase more organized and easier to manage.
    * Cons: Users may need to learn multiple methods for different types of edits, potentially leading to a steeper learning curve compared to a single edit command approach.

#### Usage

* Before making changes, the system display the current information related to the selected data. This allows users to review what's already there and decide what modifications are needed.
* Users can make modifications (add/edit/delete) to timeslots and grades without overwriting the current values.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th Student in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new Student. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the Student was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the Student being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* a tutor who has many students
* currently busy studying
* wants a solution for smoother academic interactions with his students
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**: manage contacts faster than a typical mouse/GUI driven app and provides tutors a
streamlined approach to track information about their students.


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority  | As a …​                                      | I want to …​                              | So that I can…​                                                                    |
|-----------|----------------------------------------------|-------------------------------------------|------------------------------------------------------------------------------------|
| `* * *`   | tutor                                        | add a student's contact                   |                                                                                    |
| `* * *`   | tutor                                        | delete a student's contact                | remove entries of students that I no longer need to keep track of                  |
| `* * *`   | clumsy tutor                                 | edit a student's contact                  | correct mistakes I made when adding a contact                                      |
| `* * *`   | tutor                                        | search for a student's contact            | find a student's contact                                                           |
| `* * *`   | tutor with many students in the address book | view all students' contact                |                                                                                    |
| `* * `    | tutor with many students in the address book | find for specific students based on name  | quickly get information regarding a particular student                             |
| `* * `    | time-constrained tutor                       | edit one parameter at a time              | not waste time entering every parameter of the student again                       |
| `* * `    | new tutor                                    | see usage instructions                    | refer to instructions when I forget how to use the App                             |
| `* * `    | new tutor                                    | try out the programme with sample data    | explore the functionalities of the product                                         |
| `* * `    | administrative tutor                         | attach timeslots to students              | quickly note and see which class timing the student belongs to                     |
| `* * `    | insightful tutor                             | attach grades to students                 | note and keep track of how my student is doing                                     |
| `* * `    | forgetful tutor                              | filter through timeslots                  | search and thus keep of track of timeslots where I would need to teach             |
| `* * `    | organised tutor                              | sort through student's grades             | get an overview of any particular struggling student that I would need to focus on |


*{More to be added}*

### Use cases

(For all use cases below, the System is the `TutorTrack` and the Actor is the `Tutor`, unless specified otherwise)

**Use case:** UC01 - View all students.<br>

**MSS:**
1. User requests to list students.
2. System displays the list of students.

    Use case ends.


**Use case:** UC02 - Add a student.<br>

**MSS:**
1. User requests to add a specific student information.
2. System adds the student information.

   Use case ends.

**Extensions:**

* 2a. Required fields are left empty.
    * 2a1. System shows an error message.

      Use case ends.

* 2b. The given name is invalid.
    * 2b1. System shows an error message.

      Use case ends.
* 2c. The given phone number is invalid.
    * 2c1. System shows an error message.

      Use case ends.
* 2d. The given email is invalid.
    * 2d1. System shows an error message.

      Use case ends.
* 2e. The given address is invalid.
    * 2e1. System shows an error message.

      Use case ends.
* 2f. The given grades are invalid.
    * 2f1. System shows an error message.

      Use case ends.
* 2g. The given timeslots are invalid.
    * 2g1. System shows an error message.

      Use case ends.
* 2h. The student exists in the database already.
    * 2h1. System shows an error message.

      Use case ends.

**Use case:** UC03 - Edit a student.<br>

**MSS:**
1. User requests to list students.
2. System displays the list of students.
3. User requests to update a specific student information.
4. System displays the specific current data of the student.
5. User update the current data of the student.
6. System updates the student information.

   Use case ends.

**Extensions:**

* 3a. The given index is invalid.
    * 3a1. System shows an error message.

      Use case ends.
* 3b. The given variable is invalid.
    * 3b1. System shows an error message.

      Use case ends.
* 3c. The given variable is more than 1.
    * 3c1. System shows an error message.

      Use case ends.
* 3d. The given variable is less than 1.
    * 3d1. System shows an error message.

      Use case ends.
* 5a. The given phone number is invalid.
    * 5a1. System shows an error message.

      Use case ends.
* 5b. The given email is invalid.
    * 5b1. System shows an error message.

      Use case ends.
* 5c. The given grades are invalid.
    * 5c1. System shows an error message.

      Use case ends.
* 5d. The given timeslots are invalid.
    * 5d1. System shows an error message.

      Use case ends.
* 5e. The student exists in the database already.
    * 5e1. System shows an error message.

      Use case ends.

**Use case:** UC04 - Delete a student.<br>

**MSS:**
1. User requests to list students.
2. System displays the list of students.
3. User requests to delete a specific student information.
4System delete the student information from the database.

   Use case ends.

**Extensions:**

* 3a. The given index is invalid.
    * 3a1. System shows an error message.

      Use case ends.

**Use case:** UC05 - Find a student.<br>

**MSS:**
1. User requests to list students.
2. System displays the list of students.
3. User requests to find a specific student information.
4. System show the student information.

   Use case ends.

**Extensions:**

* 3a. The given index is invalid.
    * 3a1. System shows an error message.
    * 3a2. System requests for the correct index.
    * 3a3. User enters new index.

      Steps 3a2-3a3 are repeated until the data entered are correct.

      Use case resumes from step 4.


**Use case:** UC06 - Filter a student.<br>

**MSS:**
1. User requests to list students.
2. System displays the list of students.
3. User requests to filter for a specific timeslot.
4. System show all students with that timeslot information.

   Use case ends.


### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `11` or above installed.
2.  Should be able to hold up to 1000 Students without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4.  Should not take more than 30 MB of storage space.
5.  The application should respond to user input within 1 second.
6.  Should work locally, without requiring access to any remote servers
7.  The application must comply with data protection laws PDPA since it stores personal information.
8.  The system should ensure that there is no loss or corruption of data throughout its operations.

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Private contact detail**: A contact detail that is not meant to be shared with others
* **Tutor**: A user who wants to keep track of their students' contacts
* **Student/Tutee**: A person that is taught by the tutor
* **Contact List**: A collection of student's personal and contact information accessible by the tutor.
* **Grade**: A pair of test name and scores from quizzes, tests, assignments, and other forms of assessment for a student.
* **Timeslot**: A day and time, recurring weekly, that indicates class timings of a student.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.


### Deleting a Student

1. Deleting a Student while all Students are being shown

   1. Prerequisites: List all Students using the `list` command. Multiple Students in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No Student is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.


### Saving data

1. Dealing with missing/corrupted data files

   1. Test case: delete the Addressbook.json file located in the folder where the jar file is to simulate missing data
   2. Launch TutorTrack.
   3. Addressbook.json should be repopulated with sample data again.

