import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class DataStructureGUI extends JFrame {
    private JComboBox<String> dataStructureSelector;
    private JButton pushButton;
    private JButton popButton;
    private JButton enqueueButton;
    private JButton dequeueButton;
    private JButton printButton;
    private JTextField pushInputField;
    private JTextField enqueueInputField;
    private JTextArea outputArea;
    private Object selectedDSInstance; // Selected Data Structure Instance
    private Class<?> clazz = null;
    private Object instance = null;
    private Method pushMethod = null;
    private Method popMethod = null;
    private Method peekMethod = null;
    private Method enqueueMethod = null;
    private Method dequeueMethod = null;
    private Method printMethod = null;

    // Constructor to initialize the GUI
    public DataStructureGUI() {
        setTitle("Data Structure Operations");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Dropdown to select data structure
        dataStructureSelector = new JComboBox<>(getDataStructureImplementations());
        mainPanel.add(dataStructureSelector, BorderLayout.NORTH);

        // Buttons for operations
        pushButton = new JButton("Push");
        popButton = new JButton("Pop");
        enqueueButton = new JButton("Enqueue");
        dequeueButton = new JButton("Dequeue");
        printButton = new JButton("Print");

        pushInputField = new JTextField(10); // Input field for Push
        enqueueInputField = new JTextField(10); // Input field for Enqueue

        JPanel pushPopPanel = new JPanel();
        pushPopPanel.setLayout(new FlowLayout());
        pushPopPanel.add(pushButton);
        pushPopPanel.add(pushInputField);
        pushPopPanel.add(popButton);

        // Panel for enqueue and dequeue buttons with their input field
        JPanel enqueueDequeuePanel = new JPanel();
        enqueueDequeuePanel.setLayout(new FlowLayout());
        enqueueDequeuePanel.add(enqueueButton);
        enqueueDequeuePanel.add(enqueueInputField);
        enqueueDequeuePanel.add(dequeueButton);

        // Panel for print button (centered)
        JPanel printPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        printPanel.add(printButton);

        // Main panel for organizing rows
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(pushPopPanel);
        mainPanel.add(enqueueDequeuePanel);
        mainPanel.add(printPanel);

        // Hide buttons and input fields initially
        pushButton.setVisible(false);
        popButton.setVisible(false);
        enqueueButton.setVisible(false);
        dequeueButton.setVisible(false);
        pushInputField.setVisible(false);
        enqueueInputField.setVisible(false);
        printButton.setVisible(false);


        // Output area to display messages or results
        outputArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(outputArea);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        add(mainPanel);




       // Action Listeners for buttons
       pushButton.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               try {

                   String inputValue = pushInputField.getText();
                   // Call the push method on the instance and pass an argument
                   pushMethod.invoke(instance, inputValue);
                   outputArea.append("Push operation performed with value: " + inputValue + "\n");
               } catch (IllegalAccessException | InvocationTargetException err) {
                   err.printStackTrace();
               }
           }
       });

       popButton.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               try {

                   // Call the pop method on the instance
                   popMethod.invoke(instance);
                   outputArea.append("Pop operation performed\n");
               } catch (IllegalAccessException | InvocationTargetException err) {
                   err.printStackTrace();

               }
           }
       });

       enqueueButton.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               try {
                   // Call the enqueue method on the instance and pass an argument

                   String inputValue = enqueueInputField.getText();
                   enqueueMethod.invoke(instance, inputValue);
                   outputArea.append("Enqueue operation performed with value: " + inputValue + "\n");
               } catch (IllegalAccessException | InvocationTargetException err) {
                   err.printStackTrace();
               }
           }
       });

       dequeueButton.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               try {
                   // Call the dequeue method on the instance
                   dequeueMethod.invoke(instance);
                   outputArea.append("Dequeue operation performed\n");
               } catch (IllegalAccessException | InvocationTargetException err) {
                   err.printStackTrace();
               }
           }
       });

        printButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    // Call the dequeue method on the instance
                    printMethod.invoke(instance);
                    outputArea.append("Print operation performed\n");
                } catch (IllegalAccessException | InvocationTargetException err) {
                    err.printStackTrace();
                }
            }
        });

       dataStructureSelector.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               String selectedDS = (String) dataStructureSelector.getSelectedItem();
               if (selectedDS == null) {
                   System.out.println("No Data Structure Selected");
                   return;
               }
               instantiateSelectedDataStructure(selectedDS);
               updateButtonsVisibility(selectedDS);
           }
       });

    }

    // Instantiate the selected data structure dynamically
    private void instantiateSelectedDataStructure(String selectedDS) {
        try {
            clazz = Class.forName("impl."+selectedDS);

            instance = clazz.getDeclaredConstructor().newInstance();
            if (selectedDS.contains("Stack")) {
                pushMethod = clazz.getMethod("push", Object.class);
                popMethod = clazz.getMethod("pop");
            }
            if (selectedDS.contains("Queue")) {
                enqueueMethod = clazz.getMethod("enqueue", Object.class);
                dequeueMethod = clazz.getMethod("dequeue");
            }



            printMethod = clazz.getMethod("print");

        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    // Update buttons' visibility based on the selected data structure
    private void updateButtonsVisibility(String selectedDS) {
        pushButton.setVisible(false);
        popButton.setVisible(false);
        enqueueButton.setVisible(false);
        dequeueButton.setVisible(false);
        pushInputField.setVisible(false);
        enqueueInputField.setVisible(false);
        printButton.setVisible(true);

        if (selectedDS.contains("Stack")) {
            pushButton.setVisible(true);
            popButton.setVisible(true);
            pushInputField.setVisible(true);
        }
        if (selectedDS.contains("Queue")) {
            enqueueButton.setVisible(true);
            dequeueButton.setVisible(true);
            enqueueInputField.setVisible(true);
        }
    }

    // Invoke methods of the selected data structure
    private void callSelectedDataStructureMethod(String methodName, Object... args) {
        try {
            Class<?>[] argTypes = new Class<?>[args.length];
            for (int i = 0; i < args.length; i++) {
                argTypes[i] = args[i].getClass();
            }
            selectedDSInstance.getClass().getMethod(methodName, argTypes).invoke(selectedDSInstance, args);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    // Get available data structure implementations from the "impl" directory
    private String[] getDataStructureImplementations() {
        List<String> classNames = new ArrayList<>();
        File directory = new File("out/production/Lab4/impl");
        if (directory.isDirectory()) {
            File[] files = directory.listFiles((dir, name) ->
                    name.endsWith(".class") && !name.contains("$"));

            if (files != null) {
                for (File file : files) {
                    String className = file.getName().replace(".class", "");
                    classNames.add(className);
                }
            }
        }
        return classNames.toArray(new String[0]);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                DataStructureGUI gui = new DataStructureGUI();
                gui.setVisible(true);
            }
        });
    }
}
