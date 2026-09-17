import java.io.*;
import java.util.Map;

public class FileManager {

    private static final String DELIMITER_FIELD = ",";
    private static final String DELIMITER_SUBJECT = ";";
    private static final String DELIMITER_MARK = ":";

    /** Saves every student in the manager to the given file. */
    public static void save(StudentManager manager, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Student s : manager.getAllStudents()) {
                StringBuilder line = new StringBuilder();
                line.append(s.getId()).append(DELIMITER_FIELD).append(s.getName());
                for (Map.Entry<String, Integer> entry : s.getMarks().entrySet()) {
                    line.append(DELIMITER_FIELD)
                        .append(entry.getKey()).append(DELIMITER_MARK).append(entry.getValue());
                }
                writer.write(line.toString());
                writer.newLine();
            }
        }
    }

    public static void load(StudentManager manager, String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            return; // nothing to load yet - fine on first run
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(DELIMITER_FIELD);

                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                Student s = new Student(id, name);

                for (int i = 2; i < parts.length; i++) {
                    String[] subjectMark = parts[i].split(DELIMITER_MARK);
                    try {
                        s.addMarks(subjectMark[0], Integer.parseInt(subjectMark[1]));
                    } catch (InvalidMarksException e) {
                        System.out.println("Warning: skipped corrupted record - " + e.getMessage());
                    }
                }
                manager.restoreStudent(s);
            }
        }
    }
}
