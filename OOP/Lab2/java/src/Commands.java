package src;


import src.classes.*;
import src.ser.*;
import src.logging.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Commands {
    CustomLogger logger = new CustomLogger("APP.log");
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

    public void addStudent(String abbreviation, String firstName, String lastName, String email, Date enrollmentDate, Date dateOfBirth) {
        Student student = new Student(firstName, lastName, email, enrollmentDate, dateOfBirth, abbreviation);
        Reader reader = new Reader();
        Writer writer = new Writer();
        List<Faculty> facultyList = reader.readObjects("java/src/docs/Faculty.ser", Faculty.class);
        for (Faculty faculty : facultyList) {
            if (faculty.getAbbreviation().equals(abbreviation.toUpperCase())) {
                faculty.getStudents().add(student);
                writer.writeObjects(facultyList);
                break;
            }
        }
        List<Student> studentList = reader.readObjects("java/src/docs/Student.ser", Student.class);
        studentList.add(student);
        writer.writeObjects(studentList);
        logger.log(String.format("Added student(%s, %s) in %s", firstName, lastName, abbreviation));


    }

    public void graduateStudent(String abbreviation, String firstName, String lastName, String email) {
        Reader reader = new Reader();
        Writer writer = new Writer();
        List<Faculty> facultyList = reader.readObjects("java/src/docs/Faculty.ser", Faculty.class);
        List<Student> studentsToRemove = new ArrayList<>();

        for (Faculty faculty : facultyList) {
            if (faculty.getAbbreviation().equals(abbreviation.toUpperCase())) {
                List<Student> studentList = faculty.getStudents();

                for (Student student : studentList) {
                    if (student.getFirstName().equals(firstName) && student.getLastName().equals(lastName) && student.getEmail().equals(email)) {
                        Graduate graduate = new Graduate(student.getFirstName(), student.getLastName(), student.getEmail(), student.getEnrollmentDate(), student.getDateOfBirth(), abbreviation);
                        List<Graduate> graduateList = reader.readObjects("java/src/docs/Graduate.ser", Graduate.class);
                        graduateList.add(graduate);
                        writer.writeObjects(graduateList);
                        studentList.remove(student);
                        writer.writeObjects(studentList);
                        logger.log(String.format("Graduated student(%s, %s) from %s", firstName, lastName, abbreviation));
                        break;
                    }
                }

                // Remove the graduated students from the studentList

                return; // Exit the method after a successful graduation
            }
        }
    }


    public void displayEnrolled() {
        Reader reader = new Reader();
        List<Student> studentList = reader.readObjects("java/src/docs/Student.ser", Student.class);

        for (Student student : studentList) {
            student.getStudentInfo();
        }
        // TODO not log everything only state changes

    }

    public void displayGraduates() {
        Reader reader = new Reader();
        List<Graduate> graduateList = reader.readObjects("java/src/docs/Graduate.ser", Graduate.class);

        for (Graduate student : graduateList) {
            student.getStudentInfo();
        }

    }

    public void belongsToFaculty(String abbreviation, String firstName, String lastName, String email) {
//        Student student = new Student(firstName, lastName, email, enrollmentDate, dateOfBirth);
        Reader reader = new Reader();
        Boolean found = false;
//        Writer writer = new Writer();
        List<Faculty> facultyList = reader.readObjects("java/src/docs/Faculty.ser", Faculty.class);
        for (Faculty faculty : facultyList) {
            if (faculty.getAbbreviation().equals(abbreviation.toUpperCase())) {
                List<Student> studentList = faculty.getStudents();
                for (Student student : studentList) {
                    if (student.getFirstName().equals(firstName) && student.getLastName().equals(lastName) && student.getEmail().equals(email)) {
                        found = true;
                        System.out.println("Student found in Faculty");
                    }
                }

            }
        }
        if (!found) {
            System.out.println("Student doesn't belong to faculty");
        }
        logger.log(String.format("Chcked belonging of student(%s, %s) in %s", firstName, lastName, abbreviation));

    }

    public void createFaculty(String name, String abbreviation, StudyField studyField) {
        Reader reader = new Reader();
        Writer writer = new Writer();

        List<Faculty> facultyList = reader.readObjects("java/src/docs/Faculty.ser", Faculty.class);
        facultyList.add(new Faculty(name, abbreviation.toUpperCase(), studyField));
        writer.writeObjects(facultyList);
        logger.log(String.format("Created faculty %s, %s, of %s", name, abbreviation, studyField));

    }

    public void findStudentFaculty(String email) {
        Reader reader = new Reader();
        List<Faculty> facultyList = reader.readObjects("java/src/docs/Faculty.ser", Faculty.class);
        boolean found = false;

        for (Faculty faculty : facultyList) {
            for (Student student : faculty.getStudents()) {
                if (student.getEmail().equals(email)) {
                    System.out.println("Student found in " + faculty.getAbbreviation());
                    student.getStudentInfo();
                    found = true;
                    break;
                }
            }
            if (found) {
                break;
            }
        }

        if (!found) {
            System.out.println("Student with email '" + email + "' not found in any faculty.");
        }

    }

    public void displayFaculties() {
        Reader reader = new Reader();
        List<Faculty> facultyList = reader.readObjects("java/src/docs/Faculty.ser", Faculty.class);

        for (Faculty faculty : facultyList) {
            faculty.getFacultyInfo();
        }

    }

    public void displayFacultiesOfField(StudyField studyField) {
        Reader reader = new Reader();
        List<Faculty> facultyList = reader.readObjects("java/src/docs/Faculty.ser", Faculty.class);

        for (Faculty faculty : facultyList) {
            if (faculty.getStudyField().equals(studyField)) {
                faculty.getFacultyInfo();
            }
        }

    }

    public void displayStudyFields() {
        for (StudyField studyField : StudyField.values()) {
            System.out.println(studyField);
        }

    }

    public void displayEnrolledInFaculty(String abbreviation) {
        Reader reader = new Reader();
        List<Faculty> facultyList = reader.readObjects("java/src/docs/Faculty.ser", Faculty.class);
        boolean found = false;
        for (Faculty faculty : facultyList) {
            if (faculty.getAbbreviation().equals(abbreviation)) {
                found = true;
                for (Student student : faculty.getStudents()) {
                    student.getStudentInfo();
                }
            }
        }
        if (!found) {
            System.out.println("Faculy not found.\nList of faculties:");
            Commands commandController = new Commands();
            commandController.displayFaculties();
        }

    }

    public void batchAddStudents(String abbreviation, String filePath) {
        Reader reader = new Reader();
        Writer writer = new Writer();
        List<Faculty> facultyList = reader.readObjects("java/src/docs/Faculty.ser", Faculty.class);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String firstName = parts[0].trim();
                    String lastName = parts[1].trim();
                    String email = parts[2].trim();
                    Date enrollmentDate = formatter.parse(parts[3].trim());
                    Date dateOfBirth = formatter.parse(parts[4].trim());

                    Student student = new Student(firstName, lastName, email, enrollmentDate, dateOfBirth, abbreviation);

                    for (Faculty faculty : facultyList) {
                        if (faculty.getAbbreviation().equals(abbreviation.toUpperCase())) {
                            faculty.getStudents().add(student);
                            writer.writeObjects(facultyList);
                            break;
                        }
                    }
                    List<Student> studentList = reader.readObjects("java/src/docs/Student.ser", Student.class);
                    studentList.add(student);
                    writer.writeObjects(studentList);
                    logger.log(String.format("Added student(%s, %s) in %s", firstName, lastName, abbreviation));
                }
            }
        } catch (IOException | ParseException e) {
        }
    }

    public void batchGraduateStudents(String abbreviation, String filePath) {
        Reader reader = new Reader();
        Writer writer = new Writer();
        List<Faculty> facultyList = reader.readObjects("java/src/docs/Faculty.ser", Faculty.class);


        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));

            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String firstName = parts[0].trim();
                    String lastName = parts[1].trim();
                    String email = parts[2].trim();


                    for (Faculty faculty : facultyList) {
                        if (faculty.getAbbreviation().equals(abbreviation.toUpperCase())) {
                            List<Student> studentList = faculty.getStudents();
                            Iterator<Student> iterator = studentList.iterator();

                            while (iterator.hasNext()) {
                                Student student = iterator.next();

                                if (student.getFirstName().equals(firstName) && student.getLastName().equals(lastName) && student.getEmail().equals(email)) {
                                    Graduate graduate = new Graduate(student.getFirstName(), student.getLastName(), student.getEmail(), student.getEnrollmentDate(), student.getDateOfBirth(), abbreviation);
                                    List<Graduate> graduateList = reader.readObjects("java/src/docs/Graduate.ser", Graduate.class);
                                    graduateList.add(graduate);
                                    writer.writeObjects(graduateList);
                                    studentList.remove(student); // Safely remove the student
                                    logger.log(String.format("Graduated student(%s, %s) from %s", firstName, lastName, abbreviation));
                                    break;
                                }
                            }
                            writer.writeObjects(facultyList);
                            writer.writeObjects(studentList);
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

