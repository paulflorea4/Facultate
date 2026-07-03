package socialnetwork.domain.map;

import socialnetwork.domain.model.Student;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class MyMap {
    private final TreeMap<Integer, List<Student>> map;

    public MyMap() {
        map = new TreeMap<>(new StudentGradeComparator());
    }

    public static class StudentGradeComparator implements Comparator<Integer> {

        @Override
        public int compare(Integer o1, Integer o2) {
            return o2 - o1;
        }
    }

    public void addStudent(Student s) {
        int grade = Math.round(s.getGrade());
        List<Student> list = map.get(grade);
        if (list == null) {
            list = new ArrayList<>();
            map.put(grade, list);
        }
        list.add(s);
    }

    public Set<Map.Entry<Integer, List<Student>>> getEntries() {
        return map.entrySet();
    }
}
