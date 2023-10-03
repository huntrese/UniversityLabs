package src;

import src.classes.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

public class CommandLineInterface {
    private static Commands commandController = new Commands();
    private static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.print("Enter a command (type 'exit' to quit,'help' for help): ");
            String input = scanner.nextLine();
            String[] inputList = input.split("/");

            switch (input.toLowerCase()) {
                case "help":
                    displayHelp();
                    break;
                case "exit":
                    System.out.println("Exiting the CLI.");
                    running = false;
                    break;
                default:
                    handleCommand(inputList);
                    break;
            }
        }

        scanner.close();
    }

    private static void displayHelp() {
        System.out.println("""
                Available commands:
                1. as - Create and assign a student to a faculty.
                2. gs - Graduate a student from a faculty.
                3. de - Display current enrolled students (Graduates would be ignored).
                4. dg - Display graduates (Currently enrolled students would be ignored).
                5. b - Tell or not if a student belongs to this faculty.
                6. cf - Create a new faculty.
                7. fs - Find faculty by student email.
                8. df - Display University faculties
                9. dff - Display all faculties belonging to a field. (Ex. FOOD_TECHNOLOGY)
                10. dsf - Display all StudyFields
                11. dfe - Display currently enrolled students in faculty.
                12. asb - create and assign students to faculty from file(batch)
                13. gsb - Graduate students from file (batch)""");
    }

    private static void handleCommand(String[] inputList) {
        if (inputList.length == 0) {
            return;
        }

        String command = inputList[0].toLowerCase();
        try {
            switch (command) {
                case "as":
                    handleAddStudent(inputList);
                    break;
                case "gs":
                    handleGraduateStudent(inputList);
                    break;
                case "de":
                    handleDisplayEnrolled();
                    break;
                case "dg":
                    handleDisplayGraduates();
                    break;
                case "b":
                    handleBelongsToFaculty(inputList);
                    break;
                case "cf":
                    handleCreateFaculty(inputList);
                    break;
                case "fs":
                    handleFindFacultyByEmail(inputList);
                    break;
                case "df":
                    handleDisplayFaculties();
                    break;
                case "dff":
                    handleDisplayFacultiesByField(inputList);
                    break;
                case "dsf":
                    handleDisplayStudyFields();
                    break;
                case "dfe":
                    handleDisplayFacultyEnrolled(inputList);
                    break;
                case "asb":
                    handleBatchEnrollment(inputList);
                    break;
                case "gsb":
                    handleBatchGraduation(inputList);
                    break;
                default:
                    System.out.println("Unknown command: " + command);
                    break;
            }
        } catch (Exception e) {
            System.out.println("Error executing command '" + command + "': " + e.getMessage());
        }
    }


    private static void handleAddStudent(String[] inputList) throws ParseException {
        if (inputList.length == 7) {
            String abbreviation = inputList[1].toUpperCase();
            String firstName = inputList[2];
            String lastName = inputList[3];
            String email = inputList[4];
            String date1Str = inputList[5];
            String date2Str = inputList[6];
            Date date1 = formatter.parse(date1Str);
            Date date2 = formatter.parse(date2Str);
            commandController.addStudent(abbreviation, firstName, lastName, email, date1, date2);
            System.out.println("You entered the 'as' command.");
        } else {
            System.out.println("Incorrect number of parameters for the 'as' command. Usage: as/abbreviation/firstName/lastName/email/date1/date2");
        }
    }

    private static void handleGraduateStudent(String[] inputList) {
        if (inputList.length == 5) {
            String abbreviation = inputList[1];
            String firstName = inputList[2];
            String lastName = inputList[3];
            String email = inputList[4];
            commandController.graduateStudent(abbreviation, firstName, lastName, email);
        } else {
            System.out.println("Incorrect number of parameters for the 'gs' command. Usage: gs/abbreviation/firstName/lastName/email");
        }
    }

    private static void handleDisplayEnrolled() {
        commandController.displayEnrolled();
    }

    private static void handleDisplayGraduates() {
        commandController.displayGraduates();
    }

    private static void handleBelongsToFaculty(String[] inputList) {
        if (inputList.length == 5) {
            String abbreviation = inputList[1];
            String firstName = inputList[2];
            String lastName = inputList[3];
            String email = inputList[4];
            commandController.belongsToFaculty(abbreviation, firstName, lastName, email);
        } else {
            System.out.println("Incorrect number of parameters for the 'b' command. Usage: b/abbreviation/firstName/lastName/email");
        }
    }

    private static void handleCreateFaculty(String[] inputList) {
        if (inputList.length == 4) {
            String name = inputList[1];
            String abbreviation = inputList[2];
            StudyField studyField = StudyField.valueOf(inputList[3]);
            commandController.createFaculty(name, abbreviation, studyField);
        } else {
            System.out.println("Incorrect number of parameters for the 'cf' command. Usage: cf/name/abbreviation/field");
        }
    }

    private static void handleFindFacultyByEmail(String[] inputList) {
        if (inputList.length == 2) {
            String email = inputList[1];
            commandController.findStudentFaculty(email);
        } else {
            System.out.println("Incorrect number of parameters for the 'fs' command. Usage: fs/email");
        }
    }

    private static void handleDisplayFaculties() {
            commandController.displayFaculties();

    }

    private static void handleDisplayFacultiesByField(String[] inputList) {
        if (inputList.length == 2) {
            StudyField studyField = StudyField.valueOf(inputList[1]);
            commandController.displayFacultiesOfField(studyField);
        } else {
            System.out.println("Incorrect number of parameters for the 'dff' command. Usage: dff/StudyField");
        }
    }

    private static void handleDisplayStudyFields() {
            commandController.displayStudyFields();
    }

    private static void handleDisplayFacultyEnrolled(String[] inputList) {
        if (inputList.length == 2) {
            String abbreviation = inputList[1];
            commandController.displayEnrolledInFaculty(abbreviation);
        } else {
            System.out.println("Incorrect number of parameters for the 'dfe' command. Usage: dfe/abbreviation");
        }
    }

    private static void handleBatchEnrollment(String[] inputList) {
        if (inputList.length == 3) {
            String abbreviation = inputList[1].toUpperCase();
            String filePath = inputList[2];
            commandController.batchAddStudents(abbreviation, filePath);
            System.out.println("Batch enrollment completed.");
        } else {
            System.out.println("Incorrect number of parameters for the 'asb' command. Usage: asb/abbreviation/filePath");
        }
    }

    private static void handleBatchGraduation(String[] inputList) {
        if (inputList.length == 3) {
            String abbreviation = inputList[1].toUpperCase();
            String filePath = inputList[2];
            commandController.batchGraduateStudents(abbreviation, filePath);
            System.out.println("Batch graduation completed.");
        } else {
            System.out.println("Incorrect number of parameters for the 'gsb' command. Usage: gsb/abbreviation/filePath");
        }
    }
}
