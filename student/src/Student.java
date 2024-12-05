// Lớp Student
public class Student {
    int id;
    String name;
    int age;

    // Constructor
    public Student(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    // Phương thức in thông tin Student
    public void printStudent() {
        System.out.println("ID: " + id + ", Name: " + name + ", Age: " + age);
    }
}
