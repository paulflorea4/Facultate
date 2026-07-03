package socialnetwork.domain;

import socialnetwork.domain.map.MyMap;
import socialnetwork.domain.model.Student;
import socialnetwork.domain.model.validators.StudentValidator;
import socialnetwork.domain.repository.InMemoryRepository;
import socialnetwork.domain.repository.Repository;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Sem3Test {
    private static List<Student> getList() {
        return List.of(
                new Student(1, "2", 7.3f),
                new Student(2, "3", 6f),
                new Student(3, "4", 6.9f),
                new Student(4, "5", 9.5f),
                new Student(5, "6", 9.9f),
                new Student(6, "1", 9.7f)
        );
    }

    public static void main(String[] args) {
        //initialTest();

        //setSamples();

        //mapSamples();

        //myMapCustom();

        repositoryMap();
    }

    private static void repositoryMap() {
        Repository<Integer, Student> repository = new InMemoryRepository<>(new StudentValidator());
        var students = getList();
        for (Student s : students) {
            repository.save(s);
        }
        for (Student s : repository.findAll()) {
            System.out.println(s);
        }
    }

    private static void initialTest() {
        Student s1 = new Student("Dan", 4.5f);
        Student s2 = new Student("Ana", 8.5f);
        Student s3 = new Student("Dan", 4.5f);
        Set<Student> students = new HashSet<>();
        students.add(s1);
        students.add(s2);
        students.add(s3);
        for (Student s : students) {
            System.out.println(s);
        }
        System.out.println(s1.equals(s3));
        // Test the following with and without implementing Student.hashCode()
        System.out.println(students.contains(new Student("Dan", 4.5f)));
    }

    private static void setSamples() {
        Student s1 = new Student("Dan", 4.5f);
        Student s2 = new Student("Ana", 8.5f);
        Student s3 = new Student("Dan", 4.5f);
        // TreeSet sorts all its entries according to their natural ordering
        Set<Student> students2 = new TreeSet<>();
        students2.add(s1);
        students2.add(s2);
        students2.add(s3);
        for (Student s : students2) {
            System.out.println(s);
        }

        Set<Student> students3 = new TreeSet<>((o1, o2) -> {
            //return o1.getName().compareTo(o2.getName());
            return -(int) (o1.getGrade() - o2.getGrade());
        });
        students3.add(s1);
        students3.add(s2);
        students3.add(s3);
        for (Student s : students3) {
            System.out.println(s);
        }
    }

    private static void mapSamples() {
        Student s1 = new Student("Dan", 4.5f);
        Student s2 = new Student("Ana", 8.5f);
        Student s3 = new Student("Dan", 4.5f);
        // TreeMap sorts all its keys according to their natural ordering
        // Map<String, Student> studentMap = new HashMap<>();
        Map<String, Student> studentMap = new TreeMap<>();
        studentMap.put(s1.getName(), s1);
        studentMap.put(s2.getName(), s2);
        studentMap.put(s3.getName(), s3);
        for (Map.Entry<String, Student> s : studentMap.entrySet()) {
            System.out.println(s);
        }

        List<Student> l = getList();
        Collections.sort(l);
        l.sort(new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                return 0;
            }
        });
    }

    private static void myMapCustom() {
        // MyMapInheritance map = new MyMapInheritance();
        MyMap map = new MyMap();
        List<Student> l = getList();
        for (Student s : l) {
            map.addStudent(s);
        }
        for (Map.Entry<Integer, List<Student>> studentListEntry : map.getEntries()) {
            System.out.println("Students with grade : " + studentListEntry.getKey());
            List<Student> studentsL = studentListEntry.getValue();
            // For ArrayList, you need to call Collections.sort() explicitly if you want the elements to be sorted, since elements are not automatically sorted as you insert them. They are sorted in their insertion order.
            // For TreeSet, the sorting is handled automatically, and you don't need to explicitly call a sorting method.
            // The TreeSet maintains the sorted order as elements are added, since it's a collection that implements the SortedSet interface.
            // When you add elements to a TreeSet, they are automatically sorted based on their natural order (if the elements implement the Comparable interface) or using a custom comparator if one is provided during the TreeSet instantiation.
            Collections.sort(studentsL);
            for (Student s : studentsL) {
                System.out.println(s);
            }
        }
    }
}
