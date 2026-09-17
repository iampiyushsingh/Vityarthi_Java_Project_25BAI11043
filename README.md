# AIML Vityarthi BYOP — <Your Roll Number>
# Student Performance Management System

![Java](https://img.shields.io/badge/Java-17%2B-blue?logo=openjdk)
![Dependencies](https://img.shields.io/badge/Dependencies-None-orange?logo=java)
![License](https://img.shields.io/badge/License-Academic-green)
![Status](https://img.shields.io/badge/Status-Active-brightgreen)

## Overview

I built this because I was tired of watching percentages get recalculated by hand every time someone's marks changed. It's a console-based Java app — no database, no GUI, no server — that keeps a class roster, records subject-wise marks, and works out percentage, grade, and pass/fail the moment you enter something.

- Modules: **Student Management**, **Marks Management**, **Performance Reports**
- Storage: a plain **CSV file** (`students.csv`) that auto-saves after every change
- Validation: two custom checked exceptions so bad input can't silently corrupt data
- Tests: a small hand-rolled test harness — 16 checks, all passing, no JUnit needed

## Key Features

- Add, update, delete, and search students (by ID or by partial name match)
- Enter subject-wise marks with 0–100 validation baked in
- Percentage and letter grade (A+ down to F) computed automatically, no manual math
- Class average, highest/lowest scorer, and a full pass/fail breakdown on demand
- Data survives between runs — everything reloads from `students.csv` on startup
- `Student`, `StudentManager`, and `FileManager` all have tests written against them

## Table of Contents

- [Overview](#overview)
- [Key Features](#key-features)
- [Project Structure](#project-structure)
- [Requirements](#requirements)
- [Installation](#installation)
- [Configuration](#configuration)
- [Usage](#usage)
- [How It Works](#how-it-works)
- [Data File](#data-file)
- [Workflow](#workflow)
- [Testing](#testing)
- [Troubleshooting](#troubleshooting)
- [Notes](#notes)
- [Key Classes & Methods](#key-classes--methods)
- [License](#license)
- [Contact](#contact)

## Project Structure

```
BYOP_JAVA/
├── diagrams/                          # Architecture, workflow & UML diagrams (PNG)
├── out/
│   ├── AppTest.class
│   ├── FileManager.class
│   ├── InvalidMarksException.class
│   ├── Main.class
│   ├── PerformanceReport.class
│   ├── Student.class
│   ├── StudentManager.class
│   └── StudentNotFoundException.class
├── src/
│   ├── Student.java                   # One student's data + grade/percentage math
│   ├── StudentManager.java            # CRUD on the in-memory roster
│   ├── PerformanceReport.java         # Individual + class-level report generation
│   ├── FileManager.java               # CSV save/load
│   ├── InvalidMarksException.java     # Custom checked exception - bad marks input
│   ├── StudentNotFoundException.java  # Custom checked exception - bad ID lookup
│   ├── Main.java                      # Console menu / entry point
│   └── AppTest.java                   # Dependency-free test harness
├── .gitignore
├── Project_Report.pdf                 # Detailed report, submitted separately on the portal
├── README.md
└── statement.md                       # Problem statement, scope, target users
```

`out/` is just what `javac` spits out locally — I'm not committing it. `.gitignore` keeps `out/`, `bin/`, `*.class`, and `students.csv` out of the repo, so only `src/`, `diagrams/`, and the docs actually get pushed.

## Requirements

| Requirement | Version |
|-------------|---------|
| JDK | 17+ (I built and tested this on JDK 21) |
| Build tool | None — just `javac` / `java` |
| External libraries | None |

- **Hardware**: anything that can run a JVM
- **Storage**: basically nothing — `students.csv` is a few bytes per student

## Installation

1. Clone it:
   ```bash
   git clone https://github.com/<your-username>/BYOP_JAVA.git
   cd BYOP_JAVA
   ```
2. Check your Java version:
   ```bash
   java -version
   javac -version
   ```
   Both need to say **17 or higher**. If they don't, grab [Adoptium Temurin JDK 21](https://adoptium.net/temurin/releases/) for your OS and install it.
3. Compile:
   ```bash
   javac -d out src/*.java
   ```
   That builds all 8 files in `src/` into `out/`. No output means it worked — `javac` only talks when something's wrong.

## Configuration

There's nothing to configure — no keys, no env vars, no config file. The only file this program ever creates is `students.csv`, in whatever folder you run it from, and it shows up automatically the first time you add a student.

## Usage

### Run it

```bash
java -cp out Main
```

which drops you into:

```
===== STUDENT PERFORMANCE MANAGEMENT SYSTEM =====
 1. Add Student
 2. Update Student
 3. Delete Student
 4. Search Student
 5. Enter Marks
 6. View Percentage & Grade
 7. Individual Report
 8. Class Average
 9. Highest/Lowest Marks
10. Pass/Fail Analysis
 0. Save & Exit
Enter your choice:
```

Punch in a number, hit Enter, follow the prompts. `0` saves and exits, but honestly you don't even need to remember that — it auto-saves after every add, update, delete, and marks entry anyway.

### Run the tests

```bash
java -cp out AppTest
```

You should see this at the bottom:
```
===== TEST SUMMARY =====
Passed: 16   Failed: 0
```

## How It Works

### Student Management (`StudentManager`)
Keeps a plain `ArrayList<Student>` in memory with IDs that auto-increment from 1. `addStudent`, `updateStudent`, `deleteStudent`, `findById`, `searchByName` cover the basics. Try to update or delete an ID that doesn't exist and you get a `StudentNotFoundException` instead of a silent no-op.

### Marks Management (`Student`)
`addMarks(subject, marks)` checks the mark is between 0 and 100 and the subject isn't blank — anything else throws `InvalidMarksException`. Percentage is just total marks obtained divided by (number of subjects × 100). Grade comes off a fixed scale (A+ at 90%+, down to F below 40%), and 40% is the pass line.

### Performance Reports (`PerformanceReport`)
- `individualReport` — one student's full report card
- `classAverage` — mean percentage across everyone
- `highestLowest` — top and bottom scorer
- `passFailAnalysis` — who passed, who didn't, and the class pass rate

### Persistence (`FileManager`)
Each student becomes one CSV line: `id,name,subject:marks,subject:marks,...`. On startup it reads the file back, rebuilds every `Student`, and even restores `nextId` correctly so new students don't collide with old IDs. If a line's gotten corrupted somehow, it skips that record and prints a warning instead of crashing the whole load.

## Data File

| File | Purpose |
|------|---------|
| `students.csv` | The whole roster + marks, auto-generated. Delete it if you want to start over. |

## Workflow

```
Console input (Main) → StudentManager (CRUD) / Student (marks, grade, %)
                                    ↓
                    FileManager saves after every change
                                    ↓
                    students.csv persists between runs
                                    ↓
        PerformanceReport (average, highest/lowest, pass/fail)
```

## Testing

`AppTest.java` doesn't use any testing framework — just a `check()` helper and plain `main()`. It covers:
- Add / find / update / delete, including the exception path when the ID doesn't exist
- Grade and percentage math
- Rejecting out-of-range and negative marks
- Class average and pass/fail logic
- A full save-then-reload round trip through `FileManager`

Run it with `java -cp out AppTest`.

## Troubleshooting

| Problem | Fix |
|---------|-----|
| `'javac' is not recognized` / `command not found: javac` | JDK isn't installed, or isn't on your PATH — install JDK 17+. |
| `error: release version 21 not supported` | Your JDK is older than 17 — update it. |
| `Could not find or load main class Main` | Run the command from the project root, and make sure `out/Main.class` actually exists (compile first). |
| Program exits or skips a prompt right away | Type a plain number with no stray spaces, then press Enter. |
| Data isn't there next time I run it | Run `java -cp out Main` from the exact same folder each time — `students.csv` is relative to wherever you launch it from. |
| `Warning: skipped corrupted record` | Someone (probably me) hand-edited `students.csv` and broke a line — fix or delete that line. |

## Notes

- Everything lives in one flat CSV — no setup, no server, nothing to install beyond the JDK
- Reports only reflect whoever's currently loaded in memory, which is whatever was in `students.csv` at startup
- `out/`, `bin/`, `*.class`, and `students.csv` are all git-ignored on purpose — they're generated, not source
- The full write-up with diagrams and testing notes is `Project_Report.pdf`, submitted separately through the portal, not part of this repo's evaluation

## Key Classes & Methods

- `Student.addMarks()` — validates and records one subject's marks
- `Student.getPercentage()` / `getGrade()` / `isPass()` — the derived numbers
- `StudentManager.addStudent()` / `updateStudent()` / `deleteStudent()` — roster CRUD
- `StudentManager.getByIdOrThrow()` — lookup that throws instead of returning null
- `FileManager.save()` / `load()` — the CSV round trip
- `PerformanceReport.individualReport()` / `classAverage()` / `highestLowest()` / `passFailAnalysis()` — the reporting side

## License

Coursework submission for VITyarthi — Build Your Own Project.

## Contact

- **Project maintainer**: <Your Name>
- **Email**: <your.email@vitbhopal.ac.in>
- **GitHub**: [@<your-github-username>](https://github.com/<your-github-username>)