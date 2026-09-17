import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentManager {

    private List<Student> students = new ArrayList<>();
    private int nextId = 1; // auto-incrementing roll number / ID generator

    /** Adds a new student and returns the auto-generated ID. */
    public int addStudent(String name) {
        Student s = new Student(nextId, name);
        students.add(s);
        return nextId++;
    }

    /** Updates a student's name by ID. Throws if the ID doesn't exist. */
    public void updateStudent(int id, String newName) throws StudentNotFoundException {
        Student s = getByIdOrThrow(id);
        s.setName(newName);
    }

    /** Deletes a student by ID. Throws if the ID doesn't exist. */
    public void deleteStudent(int id) throws StudentNotFoundException {
        Student s = getByIdOrThrow(id);
        students.remove(s);
    }

    /** Looks up a student by ID, throwing a custom checked exception if absent. */
    public Student getByIdOrThrow(int id) throws StudentNotFoundException {
        return findById(id).orElseThrow(() -> new StudentNotFoundException(id));
    }

    /** Searches for a student by ID. */
    public Optional<Student> findById(int id) {
        return students.stream().filter(s -> s.getId() == id).findFirst();
    }

    public List<Student> searchByName(String namePart) {
        List<Student> result = new ArrayList<>();
        for (Student s : students) {
            if (s.getName().toLowerCase().contains(namePart.toLowerCase())) {
                result.add(s);
            }
        }
        return result;
    }

    /** Returns the full list (used by the reports module). */
    public List<Student> getAllStudents() {
        return students;
    }

    public boolean isEmpty() {
        return students.isEmpty();
    }

    public void restoreStudent(Student s) {
        students.add(s);
        if (s.getId() >= nextId) {
            nextId = s.getId() + 1;
        }
    }
}
