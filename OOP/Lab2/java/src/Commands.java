package src;

import java.util.Date;
import java.util.List;

public class Commands {
    public void as(String abbreviation, String firstName, String lastName, String email, Date enrollmentDate, Date dateOfBirth) {
        Student student = new Student(firstName, lastName, email, enrollmentDate, dateOfBirth);
        Reader reader = new Reader();
        Writer writer = new Writer();
        List<Faculty> facultyList = reader.readObjects("src/docs/Faculty.ser", Faculty.class);
        for (Faculty faculty : facultyList) {
            if (faculty.getAbbreviation().equals(abbreviation)) {
                faculty.getStudents().add(student);
                writer.writeObjects(facultyList);
                break;
            }
        }
    }

}
