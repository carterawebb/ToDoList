import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.LinkedList;

class ToDoListApp {
    private LinkedList<String> tasks;
    private LinkedList<String> completedTasks;
    private String tasksFileName;
    private String completedTasksFileName;
    private GUI gui;

    enum TaskType { ALL, INCOMPLETE, COMPLETED }
    TaskType taskType = TaskType.ALL;

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
        if (taskType == TaskType.ALL || taskType == TaskType.INCOMPLETE) {
            tasks.add(task);
            saveTasks();
            gui.updateTaskList();
        }
    }

    // Edit an existing task
    public void editTask(String task, String editedDescription) {
        if (taskType == TaskType.ALL || taskType == TaskType.INCOMPLETE) {
            int index = tasks.indexOf(task);
            if (index != -1) {
                tasks.set(index, editedDescription);
                saveTasks();
                gui.updateTaskList();
            }
        }
    }

    // Remove a task from the list
    public void removeTask(String task) {
        if (taskType == TaskType.INCOMPLETE) {
            tasks.remove(task);
            saveTasks();
            gui.updateTaskList();
        } else if (taskType == TaskType.COMPLETED) {
            completedTasks.remove(task);
            saveCompletedTasks();
            gui.updateTaskList();
        }
    }

    // Mark a task as completed
    public void completeTask(String task) {
        if (taskType == TaskType.INCOMPLETE) {
            tasks.remove(task);
            completedTasks.add(task);
            saveTasks();
            saveCompletedTasks();
            gui.updateTaskList();
        }
    }

    public void unCompleteTask(String task) {
        if (taskType == TaskType.COMPLETED) {
            completedTasks.remove(task);
            tasks.add(task);
            saveTasks();
            saveCompletedTasks();
            gui.updateTaskList();
        }
    }

    // Move a task to the top of the list
    public void moveTaskToTop(String task) {
        if (taskType == TaskType.INCOMPLETE) {
            tasks.remove(task);
            tasks.addFirst(task);
            saveTasks();
            gui.updateTaskList();
        } else if (taskType == TaskType.COMPLETED) {
            completedTasks.remove(task);
            completedTasks.addFirst(task);
            saveCompletedTasks();
            gui.updateTaskList();
        }
    }

    // Move a task to the bottom of the list
    public void moveTaskToBottom(String task) {
        if (taskType == TaskType.INCOMPLETE) {
            tasks.remove(task);
            tasks.addLast(task);
            saveTasks();
            gui.updateTaskList();
        } else if (taskType == TaskType.COMPLETED) {
            completedTasks.remove(task);
            completedTasks.addLast(task);
            saveCompletedTasks();
            gui.updateTaskList();
        }
    }

    // Move a task up one spot in the list
    public void moveTaskUp(String task) {
        if (taskType == TaskType.INCOMPLETE) {
            int index = tasks.indexOf(task);
            if (index > 0) {
                tasks.remove(task);
                tasks.add(index - 1, task);
                saveTasks();
                gui.updateTaskList();
            }
        } else if (taskType == TaskType.COMPLETED) {
            int index = completedTasks.indexOf(task);
            if (index > 0) {
                completedTasks.remove(task);
                completedTasks.add(index - 1, task);
                saveCompletedTasks();
                gui.updateTaskList();
            }
        }
    }

    // Move a task down one spot in the list
    public void moveTaskDown(String task) {
        if (taskType == TaskType.INCOMPLETE) {
            int index = tasks.indexOf(task);
            if (index < tasks.size() - 1) {
                tasks.remove(task);
                tasks.add(index + 1, task);
                saveTasks();
                gui.updateTaskList();
            }
        } else if (taskType == TaskType.COMPLETED) {
            int index = completedTasks.indexOf(task);
            if (index < completedTasks.size() - 1) {
                completedTasks.remove(task);
                completedTasks.add(index + 1, task);
                saveCompletedTasks();
                gui.updateTaskList();
            }
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
        private JButton editButton;
        private JButton deleteButton;
        private JButton completeButton;
        private JButton unCompleteButton;
        private JButton moveUpButton;
        private JButton moveDownButton;
        private JButton moveToTopButton;
        private JButton moveToBottomButton;

        public void createAndShowGUI() {
            frame = new JFrame("To-Do List App");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);

            ImageIcon icon = new ImageIcon("C:\\Users\\Carter\\IdeaProjects\\ToDoList\\src\\favicon.png");
            frame.setIconImage(icon.getImage());

            JPanel panel = new JPanel(new BorderLayout());

            Border outline = new LineBorder(Color.BLACK, 2);

            Color initialColor = Color.decode("#ec6900");
            Color hoverColor = Color.decode("#804028");

            int animationDuration = 500; // Animation duration in milliseconds
            int animationSteps = 60; // Number of animation steps
            int delay = animationDuration / animationSteps;
            final int[] stepCount = {0};

            JPanel buttonPanel = new JPanel(new GridLayout(8, 1));
            editButton = new JButton("Edit");
            editButton.addActionListener(this);
            editButton.setBackground(Color.decode("#ec6900"));
            editButton.setForeground(Color.WHITE);
            editButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    animateButtonColor(editButton, initialColor, hoverColor);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    animateButtonColor(editButton, hoverColor, initialColor);
                }
            });
            editButton.setBorder(outline);

            deleteButton = new JButton("Delete");
            deleteButton.addActionListener(this);
            deleteButton.setBackground(Color.decode("#ec6900"));
            deleteButton.setForeground(Color.WHITE);
            deleteButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    animateButtonColor(deleteButton, initialColor, hoverColor);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    animateButtonColor(deleteButton, hoverColor, initialColor);
                }
            });
            deleteButton.setBorder(outline);

            completeButton = new JButton("Complete");
            completeButton.addActionListener(this);
            completeButton.setBackground(Color.decode("#ec6900"));
            completeButton.setForeground(Color.WHITE);
            completeButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    animateButtonColor(completeButton, initialColor, hoverColor);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    animateButtonColor(completeButton, hoverColor, initialColor);
                }
            });
            completeButton.setBorder(outline);

            unCompleteButton = new JButton("Un-Complete");
            unCompleteButton.addActionListener(this);
            unCompleteButton.setBackground(Color.decode("#ec6900"));
            unCompleteButton.setForeground(Color.WHITE);
            unCompleteButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    animateButtonColor(unCompleteButton, initialColor, hoverColor);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    animateButtonColor(unCompleteButton, hoverColor, initialColor);
                }
            });
            unCompleteButton.setBorder(outline);

            moveUpButton = new JButton("Move Up");
            moveUpButton.addActionListener(this);
            moveUpButton.setBackground(Color.decode("#ec6900"));
            moveUpButton.setForeground(Color.WHITE);
            moveUpButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    animateButtonColor(moveUpButton, initialColor, hoverColor);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    animateButtonColor(moveUpButton, hoverColor, initialColor);
                }
            });
            moveUpButton.setBorder(outline);

            moveDownButton = new JButton("Move Down");
            moveDownButton.addActionListener(this);
            moveDownButton.setBackground(Color.decode("#ec6900"));
            moveDownButton.setForeground(Color.WHITE);
            moveDownButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    animateButtonColor(moveDownButton, initialColor, hoverColor);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    animateButtonColor(moveDownButton, hoverColor, initialColor);
                }
            });
            moveDownButton.setBorder(outline);

            moveToTopButton = new JButton("Move to Top");
            moveToTopButton.addActionListener(this);
            moveToTopButton.setBackground(Color.decode("#ec6900"));
            moveToTopButton.setForeground(Color.WHITE);
            moveToTopButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    animateButtonColor(moveToTopButton, initialColor, hoverColor);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    animateButtonColor(moveToTopButton, hoverColor, initialColor);
                }
            });
            moveToTopButton.setBorder(outline);

            moveToBottomButton = new JButton("Move to Bottom");
            moveToBottomButton.addActionListener(this);
            moveToBottomButton.setBackground(Color.decode("#ec6900"));
            moveToBottomButton.setForeground(Color.WHITE);
            moveToBottomButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    animateButtonColor(moveToBottomButton, initialColor, hoverColor);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    animateButtonColor(moveToBottomButton, hoverColor, initialColor);
                }
            });
            moveToBottomButton.setBorder(outline);

            buttonPanel.add(editButton);
            buttonPanel.add(deleteButton);
            buttonPanel.add(completeButton);
            buttonPanel.add(unCompleteButton);
            buttonPanel.add(moveUpButton);
            buttonPanel.add(moveDownButton);
            buttonPanel.add(moveToTopButton);
            buttonPanel.add(moveToBottomButton);
            buttonPanel.setBackground(Color.decode("#984303"));
            panel.add(buttonPanel, BorderLayout.WEST);

            taskTypeComboBox = new JComboBox<>();
            taskTypeComboBox.addItem("Uncompleted Tasks");
            taskTypeComboBox.addItem("Completed Tasks");
            taskTypeComboBox.addItem("All Tasks");
            taskTypeComboBox.addActionListener(this);
            panel.add(taskTypeComboBox, BorderLayout.SOUTH);

            taskListModel = new DefaultListModel<>();
            taskList = new JList<>(taskListModel);
            taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            taskList.setBackground(Color.decode("#c24902"));
            taskList.setForeground(Color.WHITE);
            taskListScrollPane = new JScrollPane(taskList);
            taskListScrollPane.setBackground(Color.decode("#c24902"));
            taskListScrollPane.setForeground(Color.WHITE);
            taskListScrollPane.setBorder(outline);
            panel.add(taskListScrollPane, BorderLayout.CENTER);

            addButton = new JButton("Add Task");
            addButton.addActionListener(this);
            addButton.setBackground(Color.decode("#FF7F50"));
            addButton.setForeground(Color.WHITE);
            addButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    animateButtonColor(addButton, Color.decode("#FF7F50"), hoverColor);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    animateButtonColor(addButton, hoverColor, Color.decode("#FF7F50"));
                }
            });
            addButton.setBorder(outline);

            panel.add(addButton, BorderLayout.NORTH);

            frame.getContentPane().add(panel);
            frame.setVisible(true);

            loadTasks();
            loadCompletedTasks();
            updateTaskList();
        }

        private static void animateButtonColor(JButton button, Color startColor, Color endColor) {
            int animationDuration = 200; // Animation duration in milliseconds
            int animationSteps = 15; // Number of animation steps
            int delay = animationDuration / animationSteps;
            float stepSize = 1f / animationSteps;
            final float[] progress = {0f};

            javax.swing.Timer timer = new javax.swing.Timer(delay, null);
            timer.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    progress[0] += stepSize;
                    if (progress[0] > 1f) {
                        progress[0] = 1f;
                        timer.stop();
                    }
                    Color color = interpolateColors(startColor, endColor, progress[0]);
                    button.setBackground(color);
                }
            });

            timer.start();
        }

        private static Color interpolateColors(Color color1, Color color2, float progress) {
            int r = (int) (color1.getRed() * (1 - progress) + color2.getRed() * progress);
            int g = (int) (color1.getGreen() * (1 - progress) + color2.getGreen() * progress);
            int b = (int) (color1.getBlue() * (1 - progress) + color2.getBlue() * progress);
            return new Color(r, g, b);
        }

        public void updateTaskList() {
            taskListModel.clear();
            if (taskTypeComboBox.getSelectedItem().equals("Uncompleted Tasks")) {
                for (String task : tasks) {
                    taskListModel.addElement(task);
                }

                taskType = TaskType.INCOMPLETE;
            } else if (taskTypeComboBox.getSelectedItem().equals("Completed Tasks")) {
                for (String task : completedTasks) {
                    taskListModel.addElement(task);
                }

                taskType = TaskType.COMPLETED;
            } else if (taskTypeComboBox.getSelectedItem().equals("All Tasks")) {
                for (String task : tasks) {
                    taskListModel.addElement(task);
                }

                for (String task : completedTasks) {
                    taskListModel.addElement(task);
                }

                taskType = TaskType.ALL;
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == taskTypeComboBox) {
                updateTaskList();
            } else if (e.getSource() == addButton) {
                if (taskType == TaskType.COMPLETED) {
                    JOptionPane.showMessageDialog(frame, "You can only add tasks to the Uncompleted Tasks list.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String task = JOptionPane.showInputDialog(frame, "Enter task:");
                if (task != null && !task.isEmpty()) {
                    addTask(task);
                }
            }

            if (e.getSource() == deleteButton) {
                int selectedIndex = taskList.getSelectedIndex();

                if (taskType == TaskType.ALL) {
                    JOptionPane.showMessageDialog(frame, "You can only delete tasks from their respective Tasks list.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (selectedIndex != -1) {
                    String selectedTask = taskListModel.getElementAt(selectedIndex);
                    removeTask(selectedTask);
                }
            } else if (e.getSource() == editButton) {
                int selectedIndex = taskList.getSelectedIndex();
                if (selectedIndex != -1) {
                    String selectedTask = taskListModel.getElementAt(selectedIndex);
                    String editedTask = JOptionPane.showInputDialog(frame, "Edit task:", selectedTask);
                    if (editedTask != null && !editedTask.isEmpty()) {
                        editTask(selectedTask, editedTask);
                    }
                }
            } else if (e.getSource() == completeButton) {
                int selectedIndex = taskList.getSelectedIndex();

                if (taskType == TaskType.COMPLETED) {
                    JOptionPane.showMessageDialog(frame, "You can only add uncomplete tasks from the Completed Tasks list.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (selectedIndex != -1) {
                    String selectedTask = taskListModel.getElementAt(selectedIndex);
                    completeTask(selectedTask);
                }
            } else if (e.getSource() == unCompleteButton) {
                int selectedIndex = taskList.getSelectedIndex();

                if (taskType == TaskType.INCOMPLETE) {
                    JOptionPane.showMessageDialog(frame, "You can only complete tasks from the Uncompleted Tasks list.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (selectedIndex != -1) {
                    String selectedTask = taskListModel.getElementAt(selectedIndex);
                    unCompleteTask(selectedTask);
                }
            } else if (e.getSource() == moveUpButton) {
                int selectedIndex = taskList.getSelectedIndex();
                if (selectedIndex != -1) {
                    String selectedTask = taskListModel.getElementAt(selectedIndex);
                    moveTaskUp(selectedTask);
                }
            } else if (e.getSource() == moveDownButton) {
                int selectedIndex = taskList.getSelectedIndex();
                if (selectedIndex != -1) {
                    String selectedTask = taskListModel.getElementAt(selectedIndex);
                    moveTaskDown(selectedTask);
                }
            } else if (e.getSource() == moveToTopButton) {
                int selectedIndex = taskList.getSelectedIndex();
                if (selectedIndex != -1) {
                    String selectedTask = taskListModel.getElementAt(selectedIndex);
                    moveTaskToTop(selectedTask);
                }
            } else if (e.getSource() == moveToBottomButton) {
                int selectedIndex = taskList.getSelectedIndex();
                if (selectedIndex != -1) {
                    String selectedTask = taskListModel.getElementAt(selectedIndex);
                    moveTaskToBottom(selectedTask);
                }
            }
        }
    }

    public class Task {
        private String description;

        public Task(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return description;
        }
    }

    public static void main(String[] args) {
        ToDoListApp app = new ToDoListApp("tasks.txt", "completed_tasks.txt");
    }
}
