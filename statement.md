# Problem Statement

Manually keeping track of student marks, percentages, and grades in a
classroom or small institute can be cumbersome and prone to mistakes if not
done using specialized tools like pen-and-paper method or basic spreadsheet
tools not tailored for this task: calculating percentages manually,
forgetting whom which percentage belongs to, comparing students to As the
class grows, the average turns out to be quite boring. There arises the need
for a small, lightweight program without external dependencies that a teacher
or student coordinator could run immediately without any setup of database or
web server for managing student
data.

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

- **Students** (like me) who want a personal tool to track
  their individual performance in each subject over the course of a semester.
- **Teachers / class coordinators** managing a small set of students (a
  classroom, a lab batch, a mentoring group) who need quick add/update/search
  and having the class statistics available at once without opening a spreadsheet.

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
