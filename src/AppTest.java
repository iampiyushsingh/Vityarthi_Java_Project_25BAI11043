import java.io.File;
import java.io.IOException;
import java.util.List;

public class AppTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        testAddAndFindStudent();
        testUpdateStudent();
        testDeleteStudent();
        testUpdateNonExistentStudentThrows();
        testGradeCalculation();
        testInvalidMarksRejected();
        testClassAverageAndPassFail();
        testFilePersistence();

        System.out.println("\n===== TEST SUMMARY =====");
        System.out.println("Passed: " + passed + "   Failed: " + failed);
    }

    private static void check(String testName, boolean condition) {
        if (condition) {
            System.out.println("[PASS] " + testName);
            passed++;
        } else {
            System.out.println("[FAIL] " + testName);
            failed++;
        }
    }

    private static void testAddAndFindStudent() {
        StudentManager mgr = new StudentManager();
        int id = mgr.addStudent("Piyush");
        check("addStudent assigns ID 1 to first student", id == 1);
        check("findById locates the newly added student",
                mgr.findById(id).isPresent() && mgr.findById(id).get().getName().equals("Piyush"));
    }

    private static void testUpdateStudent() {
        StudentManager mgr = new StudentManager();
        int id = mgr.addStudent("Old Name");
        try {
            mgr.updateStudent(id, "New Name");
            check("updateStudent changes the name",
                    mgr.findById(id).get().getName().equals("New Name"));
        } catch (StudentNotFoundException e) {
            check("updateStudent changes the name", false);
        }
    }

    private static void testDeleteStudent() {
        StudentManager mgr = new StudentManager();
        int id = mgr.addStudent("ToDelete");
        try {
            mgr.deleteStudent(id);
            check("deleteStudent removes the student", mgr.findById(id).isEmpty());
        } catch (StudentNotFoundException e) {
            check("deleteStudent removes the student", false);
        }
    }

    private static void testUpdateNonExistentStudentThrows() {
        StudentManager mgr = new StudentManager();
        boolean threw = false;
        try {
            mgr.updateStudent(999, "Nobody");
        } catch (StudentNotFoundException e) {
            threw = true;
        }
        check("updateStudent throws StudentNotFoundException for a bad ID", threw);
    }

    private static void testGradeCalculation() {
        Student s = new Student(1, "Test");
        try {
            s.addMarks("Maths", 95);
            s.addMarks("Science", 85);
            check("Percentage calculated correctly (95+85)/200",
                    Math.abs(s.getPercentage() - 90.0) < 0.001);
            check("Grade for 90% is A+", s.getGrade().equals("A+"));
            check("isPass() is true for 90%", s.isPass());
        } catch (InvalidMarksException e) {
            check("Grade calculation setup", false);
        }
    }

    private static void testInvalidMarksRejected() {
        Student s = new Student(1, "Test");
        boolean threwTooHigh = false;
        boolean threwNegative = false;
        try {
            s.addMarks("Maths", 150);
        } catch (InvalidMarksException e) {
            threwTooHigh = true;
        }
        try {
            s.addMarks("Science", -10);
        } catch (InvalidMarksException e) {
            threwNegative = true;
        }
        check("Marks > 100 are rejected", threwTooHigh);
        check("Negative marks are rejected", threwNegative);
    }

    private static void testClassAverageAndPassFail() {
        StudentManager mgr = new StudentManager();
        try {
            int id1 = mgr.addStudent("A");
            int id2 = mgr.addStudent("B");
            mgr.findById(id1).get().addMarks("Sub1", 80); // 80% -> pass
            mgr.findById(id2).get().addMarks("Sub1", 20); // 20% -> fail

            List<Student> all = mgr.getAllStudents();
            double avg = (all.get(0).getPercentage() + all.get(1).getPercentage()) / 2;
            check("Class average is (80+20)/2 = 50%", Math.abs(avg - 50.0) < 0.001);
            check("Student A passes", all.get(0).isPass());
            check("Student B fails", !all.get(1).isPass());
        } catch (InvalidMarksException e) {
            check("Class average / pass-fail setup", false);
        }
    }

    private static void testFilePersistence() {
        String testFile = "test_students_temp.csv";
        StudentManager mgr = new StudentManager();
        try {
            int id = mgr.addStudent("PersistMe");
            mgr.findById(id).get().addMarks("Maths", 77);
            FileManager.save(mgr, testFile);

            StudentManager reloaded = new StudentManager();
            FileManager.load(reloaded, testFile);

            check("Reloaded manager has 1 student", reloaded.getAllStudents().size() == 1);
            check("Reloaded student has correct name",
                    reloaded.findById(id).get().getName().equals("PersistMe"));
            check("Reloaded student has correct marks",
                    reloaded.findById(id).get().getMarks().get("Maths") == 77);
        } catch (IOException | InvalidMarksException e) {
            check("File persistence round-trip", false);
        } finally {
            new File(testFile).delete(); // clean up test artifact
        }
    }
}
