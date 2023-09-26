package src;

import java.util.Scanner;

public class CommandLineInterface {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            // Display a prompt
            System.out.print("Enter a command (type 'exit' to quit): ");
            String input = scanner.nextLine();

            // Process the user input
            switch (input) {
                case "help":
                    System.out.println("Available commands:");
                    System.out.println("1. as - Create and assign a student to a faculty.");
                    System.out.println("2. gs - Graduate a student from a faculty.");
                    System.out.println("3. de - Display current enrolled students (Graduates would be ignored).");
                    System.out.println("4. dg - Display graduates (Currently enrolled students would be ignored).");
                    System.out.println("5. b - Tell or not if a student belongs to this faculty.");
                    System.out.println("6. cf - Create a new faculty.");
                    System.out.println("7. ss - Search what faculty a student belongs to by a unique identifier (for example by email or a unique ID).");
                    System.out.println("8. df - Display University faculties");
                    System.out.println("8. dff - Display all faculties belonging to a field. (Ex. FOOD_TECHNOLOGY)");

                    break;
                case "as":
                    System.out.println("You entered command 1.");

                    break;
                case "gs":
                    System.out.println("You entered command 2.");
                    break;
                case "de":
                    System.out.println("You entered command 3.");
                    break;
                case "dg":
                    System.out.println("You entered command 4.");
                    break;
                case "b":
                    System.out.println("You entered command 5.");
                    break;
                case "c":
                    System.out.println("You entered command 6.");
                    break;
                case "ss":
                    System.out.println("You entered command 7.");
                    break;
                case "df":
                    System.out.println("You entered command 8.");

                    break;
                case "dff":
                    System.out.println("You entered command 9.");
                    // Add your logic for command 2 here
                    break;
                case "exit":
                    System.out.println("Exiting the CLI.");
                    running = false;
                    break;
                default:
                    System.out.println("Unknown command. Type 'help' for a list of commands.");
                    break;
            }
        }

        // Close the scanner when done
        scanner.close();
    }
}
