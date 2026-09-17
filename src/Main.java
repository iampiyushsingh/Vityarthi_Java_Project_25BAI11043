import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    private static final String DATA_FILE = "students.csv";

    private static Scanner sc = new Scanner(System.in);
    private static StudentManager manager = new StudentManager();

    public static void main(String[] args) {
        loadData();

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter your choice: ");

            try {
                switch (choice) {
                    case 1 -> addStudent();
                    case 2 -> updateStudent();
                    case 3 -> deleteStudent();
                    case 4 -> searchStudent();
                    case 5 -> enterMarks();
                    case 6 -> viewPercentageAndGrade();
                    case 7 -> individualReport();
                    case 8 -> PerformanceReport.classAverage(manager.getAllStudents());
                    case 9 -> PerformanceReport.highestLowest(manager.getAllStudents());
                    case 10 -> PerformanceReport.passFailAnalysis(manager.getAllStudents());
                    case 0 -> running = false;
                    default -> System.out.println("Invalid choice, try again.");
                }
            } catch (StudentNotFoundException | InvalidMarksException e) {
            
                System.out.println("Error: " + e.getMessage());
            }
        }

        saveData();
        System.out.println("Data saved. Exiting... Goodbye!");
        sc.close();
    }

    private static void printMenu() {
        System.out.println("\n===== STUDENT PERFORMANCE MANAGEMENT SYSTEM =====");
        System.out.println(" 1. Add Student");
        System.out.println(" 2. Update Student");
        System.out.println(" 3. Delete Student");
        System.out.println(" 4. Search Student");
        System.out.println(" 5. Enter Marks");
        System.out.println(" 6. View Percentage & Grade");
        System.out.println(" 7. Individual Report");
        System.out.println(" 8. Class Average");
        System.out.println(" 9. Highest/Lowest Marks");
        System.out.println("10. Pass/Fail Analysis");
        System.out.println(" 0. Save & Exit");
    }

    // ---------------- Student Management ----------------

    private static void addStudent() {
        System.out.print("Enter student name: ");
        String name = sc.nextLine();
        int id = manager.addStudent(name);
        System.out.println("Student added successfully! Assigned ID = " + id);
        saveData();
    }

    private static void updateStudent() throws StudentNotFoundException {
        int id = readInt("Enter student ID to update: ");
        System.out.print("Enter new name: ");
        String newName = sc.nextLine();
        manager.updateStudent(id, newName);
        System.out.println("Student updated successfully!");
        saveData();
    }

    private static void deleteStudent() throws StudentNotFoundException {
        int id = readInt("Enter student ID to delete: ");
        manager.deleteStudent(id);
        System.out.println("Student deleted successfully!");
        saveData();
    }

    private static void searchStudent() {
        System.out.println("Search by: 1) ID  2) Name");
        int mode = readInt("Choice: ");
        if (mode == 1) {
            int id = readInt("Enter ID: ");
            Optional<Student> s = manager.findById(id);
            System.out.println(s.isPresent() ? s.get() : "No student found with ID " + id);
        } else {
            System.out.print("Enter name (or part of it): ");
            String name = sc.nextLine();
            List<Student> results = manager.searchByName(name);
            if (results.isEmpty()) {
                System.out.println("No matching students found.");
            } else {
                results.forEach(System.out::println);
            }
        }
    }

    // ---------------- Marks Management ----------------

    private static void enterMarks() throws StudentNotFoundException, InvalidMarksException {
        int id = readInt("Enter student ID: ");
        Student s = manager.getByIdOrThrow(id);
        System.out.print("Enter subject name: ");
        String subject = sc.nextLine();
        int marksVal = readInt("Enter marks obtained (0-100): ");
        s.addMarks(subject, marksVal);
        System.out.println("Marks recorded for " + s.getName() + " in " + subject);
        saveData();
    }

    private static void viewPercentageAndGrade() throws StudentNotFoundException {
        int id = readInt("Enter student ID: ");
        Student s = manager.getByIdOrThrow(id);
        System.out.printf("Percentage: %.2f%%  Grade: %s%n", s.getPercentage(), s.getGrade());
    }

    // ---------------- Reports ----------------

    private static void individualReport() throws StudentNotFoundException {
        int id = readInt("Enter student ID: ");
        Student s = manager.getByIdOrThrow(id);
        PerformanceReport.individualReport(s);
    }

    // ---------------- File Persistence ----------------

    private static void loadData() {
        try {
            FileManager.load(manager, DATA_FILE);
            if (!manager.isEmpty()) {
                System.out.println("Loaded " + manager.getAllStudents().size()
                        + " student(s) from " + DATA_FILE);
            }
        } catch (IOException e) {
            System.out.println("Could not load saved data: " + e.getMessage());
        }
    }

    private static void saveData() {
        try {
            FileManager.save(manager, DATA_FILE);
        } catch (IOException e) {
            System.out.println("Warning: could not save data - " + e.getMessage());
        }
    }

    // ---------------- Helper for safe integer input ----------------

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine();
            try {
                return Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
