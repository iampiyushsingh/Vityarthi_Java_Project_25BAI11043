import java.util.List;
import java.util.Map;


public class PerformanceReport {

    /** Prints a detailed report card for one student. */
    public static void individualReport(Student s) {
        System.out.println("\n----- Individual Report -----");
        System.out.println("ID   : " + s.getId());
        System.out.println("Name : " + s.getName());
        System.out.println("Subject-wise marks:");
        for (Map.Entry<String, Integer> entry : s.getMarks().entrySet()) {
            System.out.printf("   %-15s : %d/100%n", entry.getKey(), entry.getValue());
        }
        System.out.printf("Total       : %d / %d%n", s.getTotalMarks(), s.getMaxMarks());
        System.out.printf("Percentage  : %.2f%%%n", s.getPercentage());
        System.out.println("Grade       : " + s.getGrade());
        System.out.println("Result      : " + (s.isPass() ? "PASS" : "FAIL"));
        System.out.println("------------------------------");
    }

    /** Prints the average percentage across the whole class. */
    public static void classAverage(List<Student> students) {
        if (students.isEmpty()) {
            System.out.println("No students to calculate an average for.");
            return;
        }
        double totalPct = 0;
        for (Student s : students) {
            totalPct += s.getPercentage();
        }
        double average = totalPct / students.size();
        System.out.printf("%nClass Average Percentage: %.2f%% (based on %d students)%n",
                average, students.size());
    }

    /** Finds and prints the student(s) with the highest and lowest percentage. */
    public static void highestLowest(List<Student> students) {
        if (students.isEmpty()) {
            System.out.println("No students available.");
            return;
        }
        Student highest = students.get(0);
        Student lowest = students.get(0);

        for (Student s : students) {
            if (s.getPercentage() > highest.getPercentage()) highest = s;
            if (s.getPercentage() < lowest.getPercentage()) lowest = s;
        }

        System.out.println("\nHighest Scorer: " + highest);
        System.out.println("Lowest Scorer : " + lowest);
    }

    /** Counts and lists how many students passed vs failed. */
    public static void passFailAnalysis(List<Student> students) {
        if (students.isEmpty()) {
            System.out.println("No students available.");
            return;
        }
        int passCount = 0, failCount = 0;
        System.out.println("\n----- Pass/Fail Analysis -----");
        for (Student s : students) {
            if (s.isPass()) {
                passCount++;
            } else {
                failCount++;
                System.out.println("FAILED: " + s);
            }
        }
        double passPercentageOfClass = (passCount * 100.0) / students.size();
        System.out.printf("Total Students : %d%n", students.size());
        System.out.printf("Passed         : %d%n", passCount);
        System.out.printf("Failed         : %d%n", failCount);
        System.out.printf("Pass Rate      : %.2f%%%n", passPercentageOfClass);
    }
}
