import java.util.LinkedHashMap;
import java.util.Map;

class Student {

    private int id;
    private String name;
    // subject name -> marks obtained (out of 100)
    private Map<String, Integer> marks = new LinkedHashMap<>();

    public Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // ---------- Getters & Setters ----------
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Integer> getMarks() {
        return marks;
    }

    // ---------- Marks Management ----------

   
    public void addMarks(String subject, int marksObtained) throws InvalidMarksException {
        if (marksObtained < 0 || marksObtained > 100) {
            throw new InvalidMarksException(
                    "Marks must be between 0 and 100. Got: " + marksObtained);
        }
        if (subject == null || subject.trim().isEmpty()) {
            throw new InvalidMarksException("Subject name cannot be empty.");
        }
        marks.put(subject.trim(), marksObtained);
    }

    /** Total marks obtained across all subjects. */
    public int getTotalMarks() {
        int total = 0;
        for (int m : marks.values()) {
            total += m;
        }
        return total;
    }

    /** Maximum possible marks = number of subjects * 100. */
    public int getMaxMarks() {
        return marks.size() * 100;
    }

    /** Percentage = (total obtained / total possible) * 100. */
    public double getPercentage() {
        if (marks.isEmpty()) return 0.0;
        return (getTotalMarks() * 100.0) / getMaxMarks();
    }

   
    public String getGrade() {
        double pct = getPercentage();
        if (pct >= 90) return "A+";
        if (pct >= 80) return "A";
        if (pct >= 70) return "B";
        if (pct >= 60) return "C";
        if (pct >= 50) return "D";
        if (pct >= 40) return "E";
        return "F"; // Fail
    }

    /** A student is considered PASS if percentage >= 40. */
    public boolean isPass() {
        return getPercentage() >= 40.0;
    }

    @Override
    public String toString() {
        return String.format("ID: %-4d Name: %-15s Percentage: %6.2f%%  Grade: %s",
                id, name, getPercentage(), getGrade());
    }
}
