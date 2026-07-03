package socialnetwork.domain.map;

import socialnetwork.domain.model.Student;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class MyMapInheritance extends TreeMap<Integer, List<Student>> {
    public void addStudent(Student s)
    {
        int grade = Math.round(s.getGrade());
        List<Student> list = super.get(grade);
        if (list == null)
        {
            list = new ArrayList<>();
            super.put(grade, list);
        }
        list.add(s);
    }

    public Set<Map.Entry<Integer, List<Student>>> getEntries() {
        Comparator<Map.Entry<Integer, List<Student>>> byKeyDesc = (e1, e2) -> e2.getKey().compareTo(e1.getKey());
        Supplier<TreeSet<Map.Entry<Integer, List<Student>>>> sortedSet = () -> new TreeSet<>(byKeyDesc);
        return super.entrySet().stream().collect(Collectors.toCollection(sortedSet));
    }
}
