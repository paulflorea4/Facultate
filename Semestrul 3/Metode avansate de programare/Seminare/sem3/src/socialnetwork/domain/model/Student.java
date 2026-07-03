package socialnetwork.domain.model;

import java.util.Objects;

public class Student extends Entity<Integer> implements Comparable<Student> {
    private String name;
    private float grade;

    public Student(String name, float grade) {
        super(null);
        this.name = name;
        this.grade = grade;
    }

    public Student(Integer id, String name, float grade) {
        super(id);
        this.name = name;
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getGrade() {
        return grade;
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "Student{" +
               "name='" + name + '\'' +
               ", grade=" + grade +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student student)) return false;
        return Float.compare(student.getGrade(), getGrade()) == 0 &&
               Objects.equals(getName(), student.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getGrade());
    }

    @Override
    public int compareTo(Student o) {
        return this.name.compareTo(o.getName());
    }
}
