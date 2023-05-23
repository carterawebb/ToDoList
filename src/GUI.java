import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;

class ToDoListApp {
    private LinkedList<String> tasks;
    private LinkedList<String> completedTasks;
    private String tasksFileName;
    private String completedTasksFileName;
    private GUI gui;

    public ToDoListApp(String tasksFileName, String completedTasksFileName) {
        this.tasksFileName = tasksFileName;
        this.completedTasksFileName = completedTasksFileName;
        tasks = new LinkedList<>();
        completedTasks = new LinkedList<>();
        gui = new GUI();
        gui.createAndShowGUI();
    }

    public void loadTasks() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(tasksFileName));
            String task;
            while ((task = reader.readLine()) != null) {
                tasks.add(task);
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
        }
    }

    public void loadCompletedTasks() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(completedTasksFileName));
            String task;
            while ((task = reader.readLine()) != null) {
                completedTasks.add(task);
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error loading completed tasks: " + e.getMessage());
        }
    }

    public void saveTasks() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(tasksFileName));
            for (String task : tasks) {
                writer.write(task);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }

    public void saveCompletedTasks() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(completedTasksFileName));
            for (String task : completedTasks) {
                writer.write(task);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving completed tasks: " + e.getMessage());
        }
    }

    // Add a new task to the list
    public void addTask(String task) {
        tasks.add(task);
        saveTasks();
        gui.updateTaskList();
    }

    // Remove a task from the list
    public void removeTask(String task) {
        tasks.remove(task);
        saveTasks();
        gui.updateTaskList();
    }

    // Mark a task as completed
    public void completeTask(String task) {
        tasks.remove(task);
        completedTasks.add(task);
        saveTasks();
        saveCompletedTasks();
        gui.updateTaskList();
    }

    // Move a task to the top of the list
    public void moveTaskToTop(String task) {
        tasks.remove(task);
        tasks.addFirst(task);
        saveTasks();
        gui.updateTaskList();
    }

    // Move a task to the bottom of the list
    public void moveTaskToBottom(String task) {
        tasks.remove(task);
        tasks.addLast(task);
        saveTasks();
        gui.updateTaskList();
    }

    // Move a task up one spot in the list
    public void moveTaskUp(String task) {
        int index = tasks.indexOf(task);
        if (index > 0) {
            tasks.remove(task);
            tasks.add(index - 1, task);
            saveTasks();
            gui.updateTaskList();
        }
    }

    // Move a task down one spot in the list
    public void moveTaskDown(String task) {
        int index = tasks.indexOf(task);
        if (index < tasks.size() - 1) {
            tasks.remove(task);
            tasks.add(index + 1, task);
            saveTasks();
            gui.updateTaskList();
        }
    }

    public class GUI implements ActionListener {
        private JFrame frame;
        private JPanel panel;
        private JLabel titleLabel;
        private JComboBox<String> taskTypeComboBox;
        private DefaultListModel<String> taskListModel;
        private JList<String> taskList;
        private JScrollPane taskListScrollPane;
        private JButton addButton;

        public void createAndShowGUI() {
            frame = new JFrame("To-Do List App");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            panel = new JPanel();
            panel.setLayout(new BorderLayout());

            titleLabel = new JLabel("To-Do List");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
            panel.add(titleLabel, BorderLayout.NORTH);

            taskTypeComboBox = new JComboBox<>();
            taskTypeComboBox.addItem("Uncompleted Tasks");
            taskTypeComboBox.addItem("Completed Tasks");
            taskTypeComboBox.addActionListener(this);
            panel.add(taskTypeComboBox, BorderLayout.SOUTH);

            taskListModel = new DefaultListModel<>();
            taskList = new JList<>(taskListModel);
            taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            taskListScrollPane = new JScrollPane(taskList);
            panel.add(taskListScrollPane, BorderLayout.CENTER);

            addButton = new JButton("Add Task");
            addButton.addActionListener(this);
            panel.add(addButton, BorderLayout.EAST);

            frame.getContentPane().add(panel);
            frame.pack();
            frame.setVisible(true);

            loadTasks();
            loadCompletedTasks();
            updateTaskList();
        }

        public void updateTaskList() {
            taskListModel.clear();
            if (taskTypeComboBox.getSelectedItem().equals("Uncompleted Tasks")) {
                for (String task : tasks) {
                    taskListModel.addElement(task);
                }
            } else if (taskTypeComboBox.getSelectedItem().equals("Completed Tasks")) {
                for (String task : completedTasks) {
                    taskListModel.addElement(task);
                }
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == taskTypeComboBox) {
                updateTaskList();
            } else if (e.getSource() == addButton) {
                String task = JOptionPane.showInputDialog(frame, "Enter task:");
                if (task != null && !task.isEmpty()) {
                    addTask(task);
                }
            }
        }
    }

    public static void main(String[] args) {
        ToDoListApp app = new ToDoListApp("tasks.txt", "completed_tasks.txt");
    }
}
