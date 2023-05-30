package manager;

import model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final String path;

    public FileBackedTasksManager(String path) {
        this.path = path;
    }

    private void save() throws ManagerSaveException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            bw.write("id,type,name,status,description,epic");
            bw.newLine();
            bw.flush();
            for (Task task : tasks.values()) {
                bw.write(taskToString(task));
                bw.flush();
                bw.newLine();

            }
            for (Epic task : epics.values()) {
                bw.write(taskToString(task));
                bw.flush();
                bw.newLine();
            }
            for (Subtask task : subtasks.values()) {
                bw.write(taskToString(task));
                bw.flush();
                bw.newLine();
            }
            bw.newLine();
            bw.flush();
            bw.write(historyToString(historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Error while saving");
        }
    }

    static String historyToString(HistoryManager manager) {
        return manager.toString();
    }

    private String taskToString(Task task) {
        return task.getTaskID() + "," + task.getTaskType().toString() + "," + task.getName() + ","
                + task.getStatus() + "," + task.getDescription();
    }

    private String taskToString(Subtask task) {
        return task.getTaskID() + "," + task.getTaskType().toString() + "," + task.getName() + ","
                + task.getStatus() + "," + task.getDescription() + "," + task.getEpicID();
    }

    private String taskToString(Epic task) {
        StringBuilder epicIds = new StringBuilder();
        for (int id : task.getMySubtasksID()) {
            epicIds.append(":").append(id);
        }
        if (!epicIds.toString().isEmpty())
            epicIds.deleteCharAt(0);

        return task.getTaskID() + "," + task.getTaskType().toString() + "," + task.getName() + ","
                + task.getStatus() + "," + task.getDescription() + "," + epicIds;
    }

    private static Task fromString(String string) {
        String[] line = string.split(",");
        switch (line[1]) {
            case "EPIC":
                Epic epic = new Epic(line[2], line[4]);
                epic.setTaskID(Integer.parseInt(line[0]));
                epic.setStatus(TaskStatus.valueOf(line[3]));
                if (line[5] != null) {
                    ArrayList<Integer> subtasksOfEpic = new ArrayList<>();
                    String[] ids = line[5].split(":");
                    for (int i = 0; i < ids.length; i++) {
                        subtasksOfEpic.add(Integer.parseInt(ids[i]));
                    }
                    epic.setMySubtasksID(subtasksOfEpic);
                }
                return epic;

            case "SUBTASK":
                Subtask subtask = new Subtask(line[2], line[4], Integer.parseInt(line[5]), TaskStatus.valueOf(line[3]));
                subtask.setTaskID(Integer.parseInt(line[0]));
                return subtask;
            case "TASK":
                Task task = new Task(line[2], line[4], TaskStatus.valueOf(line[3]));
                task.setTaskID(Integer.parseInt(line[0]));
                return task;
            default:
                return null;
        }
    }

    static FileBackedTasksManager loadFromFile(String path) {
        FileBackedTasksManager manager = new FileBackedTasksManager(path);
        try (BufferedReader bw = new BufferedReader(new FileReader(path))) {
            while (bw.ready()) {
                String line = bw.readLine();
                if (line.equals("id,type,name,status,description,epic")) {
                    continue;
                }
                if (!line.isBlank()) {
                    Task newTask = fromString(line);
                    if (newTask != null) {
                        if (newTask.getTaskType().equals(TaskType.EPIC)) {
                            manager.epics.put(newTask.getTaskID(), (Epic) newTask);
                        } else if (newTask.getTaskType().equals(TaskType.TASK)) {
                            manager.tasks.put(newTask.getTaskID(), newTask);
                        } else if (newTask.getTaskType().equals(TaskType.SUBTASK)) {
                            manager.subtasks.put(newTask.getTaskID(), (Subtask) newTask);
                        }
                    }
                } else {
                    break;
                }
            }
            String history = bw.readLine();
            if (!history.isBlank()) {
                for (int taskId : historyFromString(history)) {
                    if (manager.tasks.containsKey(taskId)) {
                        manager.historyManager.historyAdd(manager.tasks.get(taskId));
                    }
                    if (manager.subtasks.containsKey(taskId)) {
                        manager.historyManager.historyAdd(manager.subtasks.get(taskId));
                    }
                    if (manager.epics.containsKey(taskId)) {
                        manager.historyManager.historyAdd(manager.epics.get(taskId));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("File not found");
        }
        return manager;
    }

    static List<Integer> historyFromString(String history) {
        String[] historyLine = history.split(",");
        List<Integer> historyList = new ArrayList<>();
        for (String id : historyLine) {
            historyList.add(Integer.parseInt(id));
        }
        return historyList;
    }


    @Override
    public void addNewSubtask(Subtask subtask, int epicId) {
        super.addNewSubtask(subtask, epicId);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void addNewTask(Task task) {
        super.addNewTask(task);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addNewEpic(Epic task) {
        super.addNewEpic(task);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeTaskByID(int id) {
        super.removeTaskByID(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeSubtaskByID(int id) {
        super.removeSubtaskByID(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeEpicByID(int id) {
        super.removeEpicByID(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void printTaskByID(int id) {
        super.printTaskByID(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void printSubtaskByID(int id) {
        super.printSubtaskByID(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void printEpicByID(int id) {
        super.printEpicByID(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateTask(Task task, int id) {
        super.updateTask(task, id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateEpic(Epic task, int id) {
        super.updateEpic(task, id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateSubtask(Subtask task, int id) {
        super.updateSubtask(task, id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }


    public static void main(String[] args) {
        FileBackedTasksManager fileManager1 = new FileBackedTasksManager("src/lastSessionSaved.csv");
        Task task0 = new Task("a", "a", TaskStatus.NEW);
        Task task1 = new Task("b", "b", TaskStatus.NEW);
        Epic epic2 = new Epic("c", "c");
        Epic epic3 = new Epic("d", "d");
        Subtask subtask4 = new Subtask("e", "e", 2, TaskStatus.NEW);
        Subtask subtask5 = new Subtask("e", "e", 2, TaskStatus.IN_PROGRESS);
        Subtask subtask6 = new Subtask("e", "e", 2, TaskStatus.NEW);
        fileManager1.addNewTask(task0);
        fileManager1.addNewTask(task1);
        fileManager1.addNewEpic(epic2);
        fileManager1.addNewEpic(epic3);
        fileManager1.addNewSubtask(subtask4, 2);
        fileManager1.addNewSubtask(subtask5, 2);
        fileManager1.addNewSubtask(subtask6, 2);
        System.out.println("Поехали!");
        fileManager1.printTaskByID(0);
        fileManager1.printTaskByID(1);
        fileManager1.printEpicByID(2);
        fileManager1.printEpicByID(3);
        fileManager1.printSubtaskByID(4);
        fileManager1.printSubtaskByID(5);
        fileManager1.printSubtaskByID(6);
        fileManager1.printTaskByID(0);
        fileManager1.removeEpicByID(3);
        System.out.println("history1:");
        fileManager1.printHistory();
        FileBackedTasksManager fileManager2 = loadFromFile("src/lastSessionSaved.csv");
        System.out.println("history2:");
        fileManager2.printHistory();
        System.out.println("all tasks:");
        fileManager2.printAllTasks();
        fileManager2.printAllEpics();
        fileManager2.printAllSubtasks();

    }

}