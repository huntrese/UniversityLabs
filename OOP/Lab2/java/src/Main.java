package src;

import java.util.Date; // Import the old java.util.Date class
import java.util.List;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        List<Student> studentList= new ArrayList<>();
        List<Faculty> facultyList= new ArrayList<>();

//        Student gigel = new Student("Gigel", "vieru", "darkfury@yahoo.com", new Date(), new Date());
//        Student vasea = new Student("vasea", "igor", "dimony@yahoo.com", new Date(), new Date());
//        studentList.add(gigel);
//        studentList.add(vasea);

//        Faculty isa= new Faculty("SOFT","ISA", studentList, StudyField.FIELDS.SOFTWARE_ENGINEERING);
//        facultyList.add(isa);

//        Writer writer = new Writer();
//        writer.writeObjects(facultyList);

        Reader reader = new Reader();
        studentList= reader.readObjects("src/docs/Student.ser",Student.class);
        facultyList = reader.readObjects("src/docs/Faculty.ser",Faculty.class);
        System.out.println(studentList.get(0).getFirstName());
        System.out.println(facultyList.get(0).getAbbreviation());

    }
}
