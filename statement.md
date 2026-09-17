# Problem Statement

Manually tracking student marks, percentages, and grades in a classroom or
small institute is error-prone and time-consuming when done with pen-and-paper
or plain spreadsheets that aren't purpose-built for it: recalculating
percentages by hand, forgetting who scored what, and comparing students against
the class average all become tedious as the class grows. There is a need for a
lightweight, dependency-free tool that a teacher or student coordinator can run
immediately, without setting up a database or web server, to manage student
records and generate performance insights on demand.

## Scope of the Project

This project is a **console-based Java application** that manages student
records and their academic performance for a single class/section. It covers:

- Maintaining a roster of students (create, read, update, delete)
- Recording subject-wise marks for each student
- Automatically deriving percentage and letter grade from recorded marks
- Generating class-level performance insights (average, best/worst performer,
  pass/fail breakdown)
- Persisting all data to a local file so it survives between program runs

**Out of scope:** multi-class/multi-section handling, a graphical or web UI,
user authentication/login, and networked/multi-user access — all reasonable
directions for future enhancement, but not required for this coursework
submission.

## Target Users

- **Students** (like the project author) who want a personal tool to track
  their own subject-wise performance across a semester.
- **Teachers / class coordinators** managing a small set of students (a
  classroom, a lab batch, a mentoring group) who need quick add/update/search
  and instant class statistics without opening a spreadsheet.

## High-Level Features

1. **Student Management** — Add, Update, Delete, Search (by ID or name)
2. **Marks Management** — Enter subject-wise marks, calculate percentage and
   grade automatically
3. **Performance Reports** — Individual report card, class average, highest/
   lowest scorer, pass/fail analysis
4. **Data Persistence** — All changes are saved to `students.csv` and reloaded
   automatically the next time the program runs
5. **Validation & Error Handling** — Custom checked exceptions
   (`InvalidMarksException`, `StudentNotFoundException`) reject bad input and
   bad lookups instead of letting the program crash or silently corrupt data
