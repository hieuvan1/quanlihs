package binary;
import java.util.stream.Collectors;

import java.util.*;

enum StudentRank {
    FAIL("[0 - 5.0)", 0.0, 5.0),
    MEDIUM("[5.0 - 6.5)", 5.0, 6.5),
    GOOD("[6.5 - 7.5)", 6.5, 7.5),
    VERY_GOOD("[7.5 - 9.0)", 7.5, 9.0),
    EXCELLENT("[9.0 - 10.0]", 9.0, 10.1);

    private final String range;
    private final double min;
    private final double max;

    StudentRank(String range, double min, double max) {
        this.range = range;
        this.min = min;
        this.max = max;
    }

    public static StudentRank fromMarks(double marks) {
        for (StudentRank rank : values()) {
            if (marks >= rank.min && marks < rank.max) {
                return rank;
            }
        }
        return FAIL;
    }

    @Override
    public String toString() {
        return name() + " " + range;
    }
}

class Student {
    private final int id;
    private String fullName;
    private double marks;
    private StudentRank rank;

    public Student(int id, String fullName, double marks) {
        validateInput(id, fullName, marks);
        this.id = id;
        this.fullName = fullName;
        setMarks(marks);
    }

    private void validateInput(int id, String fullName, double marks) {
        if (id <= 0) {
            throw new IllegalArgumentException("Student ID must be positive");
        }
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be empty");
        }
        if (marks < 0 || marks > 10) {
            throw new IllegalArgumentException("Marks must be between 0 and 10");
        }
    }

    // Getters
    public int getId() { return id; }
    public String getFullName() { return fullName; }
    public double getMarks() { return marks; }
    public StudentRank getRank() { return rank; }

    // Setters
    public void setFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be empty");
        }
        this.fullName = fullName;
    }

    public void setMarks(double marks) {
        if (marks < 0 || marks > 10) {
            throw new IllegalArgumentException("Marks must be between 0 and 10");
        }
        this.marks = marks;
        this.rank = StudentRank.fromMarks(marks);
    }

    @Override
    public String toString() {
        return String.format("ID: %d | Name: %s | Marks: %.2f | Rank: %s",
                id, fullName, marks, rank);
    }
}

class StudentManagementSystem {
    private final Map<Integer, Student> students;
    private final int maxStudents;

    public StudentManagementSystem(int maxStudents) {
        if (maxStudents <= 0) {
            throw new IllegalArgumentException("Maximum number of students must be positive");
        }
        this.maxStudents = maxStudents;
        this.students = new HashMap<>();
    }

    // Add student
    public void addStudent(int id, String fullName, double marks) {
        if (students.size() >= maxStudents) {
            throw new IllegalStateException("Maximum class size reached");
        }
        if (students.containsKey(id)) {
            throw new IllegalArgumentException("Student with ID " + id + " already exists");
        }
        students.put(id, new Student(id, fullName, marks));
    }

    // Edit student
    public void editStudent(int id, String newName, Double newMarks) {
        Student student = findStudentById(id);
        if (newName != null) {
            student.setFullName(newName);
        }
        if (newMarks != null) {
            student.setMarks(newMarks);
        }
    }

    public void deleteStudent(int id) {
        if (students.remove(id) == null) {
            throw new IllegalArgumentException("Student with ID " + id + " not found");
        }
    }

    public Student findStudentById(int id) {
        Student student = students.get(id);
        if (student == null) {
            throw new IllegalArgumentException("Student with ID " + id + " not found");
        }
        return student;
    }

    public List<Student> findStudentsByName(String name) {
        String searchName = name.toLowerCase();
        return students.values().stream()
                .filter(s -> s.getFullName().toLowerCase().contains(searchName))
                .toList();
    }

    public List<Student> findStudentsByRank(StudentRank rank) {
        return students.values().stream()
                .filter(s -> s.getRank() == rank)
                .toList();
    }

    public List<Student> getAllStudentsSorted(Comparator<Student> comparator) {
        return students.values().stream()
                .sorted(comparator)
                .toList();
    }

    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        if (students.isEmpty()) {
            return stats;
        }

        DoubleSummaryStatistics markStats = students.values().stream()
                .mapToDouble(Student::getMarks)
                .summaryStatistics();

        Map<StudentRank, Long> rankDistribution = students.values().stream()
                .collect(Collectors.groupingBy(Student::getRank, Collectors.counting()));

        stats.put("totalStudents", students.size());
        stats.put("averageMarks", markStats.getAverage());
        stats.put("highestMarks", markStats.getMax());
        stats.put("lowestMarks", markStats.getMin());
        stats.put("rankDistribution", rankDistribution);

        return stats;
    }
}

public class Main {
    public static void main(String[] args) {

        try {
            Scanner scanner = new Scanner(System.in);

            // Get maximum class size
            System.out.print("Enter maximum number of students in the class: ");
            int maxStudents = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            StudentManagementSystem sms = new StudentManagementSystem(maxStudents);

            while (true) {
                System.out.println("\nStudent Management System");
                System.out.println("1. Add Student");
                System.out.println("2. Edit Student");
                System.out.println("3. Delete Student");
                System.out.println("4. Search Student");
                System.out.println("5. Display All Students");
                System.out.println("6. Display Statistics");
                System.out.println("7. Exit");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1 -> {
                        System.out.print("Enter Student ID: ");
                        int id = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Enter Student Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter Marks (0-10): ");
                        double marks = scanner.nextDouble();
                        sms.addStudent(id, name, marks);
                        System.out.println("Student added successfully!");
                    }
                    case 2 -> {
                        System.out.print("Enter Student ID to edit: ");
                        int id = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Enter new name (or press Enter to skip): ");
                        String name = scanner.nextLine();
                        System.out.print("Enter new marks (or -1 to skip): ");
                        double marks = scanner.nextDouble();
                        sms.editStudent(id,
                                name.isEmpty() ? null : name,
                                marks < 0 ? null : marks);
                        System.out.println("Student updated successfully!");
                    }
                    case 3 -> {
                        System.out.print("Enter Student ID to delete: ");
                        int id = scanner.nextInt();
                        sms.deleteStudent(id);
                        System.out.println("Student deleted successfully!");
                    }
                    case 4 -> {
                        System.out.println("Search by:");
                        System.out.println("1. ID");
                        System.out.println("2. Name");
                        System.out.println("3. Rank");
                        int searchChoice = scanner.nextInt();
                        scanner.nextLine();

                        switch (searchChoice) {
                            case 1 -> {
                                System.out.print("Enter ID: ");
                                int id = scanner.nextInt();
                                System.out.println(sms.findStudentById(id));
                            }
                            case 2 -> {
                                System.out.print("Enter name: ");
                                String name = scanner.nextLine();
                                sms.findStudentsByName(name).forEach(System.out::println);
                            }
                            case 3 -> {
                                System.out.println("Select rank:");
                                StudentRank[] ranks = StudentRank.values();
                                for (int i = 0; i < ranks.length; i++) {
                                    System.out.println((i + 1) + ". " + ranks[i]);
                                }
                                int rankChoice = scanner.nextInt() - 1;
                                if (rankChoice >= 0 && rankChoice < ranks.length) {
                                    sms.findStudentsByRank(ranks[rankChoice])
                                            .forEach(System.out::println);
                                }
                            }
                        }
                    }
                    case 5 -> {
                        System.out.println("Sort by:");
                        System.out.println("1. ID");
                        System.out.println("2. Name");
                        System.out.println("3. Marks");
                        int sortChoice = scanner.nextInt();

                        Comparator<Student> comparator = switch (sortChoice) {
                            case 1 -> Comparator.comparingInt(Student::getId);
                            case 2 -> Comparator.comparing(Student::getFullName);
                            case 3 -> Comparator.comparingDouble(Student::getMarks).reversed();
                            default -> Comparator.comparingInt(Student::getId);
                        };

                        sms.getAllStudentsSorted(comparator).forEach(System.out::println);
                    }
                    case 6 -> {
                        Map<String, Object> stats = sms.getStatistics();
                        System.out.println("\nClass Statistics:");
                        stats.forEach((key, value) -> System.out.println(key + ": " + value));
                    }
                    case 7 -> {
                        System.out.println("Thank you for using Student Management System!");
                        return;
                    }
                    default -> System.out.println("Invalid choice! Please try again.");
                }
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }

    }

}
