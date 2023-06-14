package manager;

import exception.ManagerSaveException;
import model.*;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final String path;

    public FileBackedTasksManager(String path) {
        this.path = path;
    }

    private void save() throws ManagerSaveException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            bw.write("id,type,name,status,description,startTime,duration,epic");
            bw.newLine();
            for (Task task : tasks.values()) {
                bw.write(taskToString(task));
                bw.newLine();

            }

            for (Subtask task : subtasks.values()) {
                bw.write(taskToString(task));
                bw.newLine();
            }
            for (Epic task : epics.values()) {
                bw.write(taskToString(task));
                bw.newLine();
            }
            bw.newLine();
            bw.write(historyToString(historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Error while saving");
        }
    }

    private static String historyToString(HistoryManager manager) {
        return manager.toString();
    }

    private String taskToString(Task task) {
        return task.getTaskID() + "," + task.getTaskType().toString() + "," + task.getName() + ","
                + task.getStatus() + "," + task.getDescription()+"," + task.getStartTime().format(task.getDateTimeFormatter())+"," + task.getDuration();
    }

    private String taskToString(Subtask task) {
        return task.getTaskID() + "," + task.getTaskType().toString() + "," + task.getName() + ","
                + task.getStatus() + "," + task.getDescription()+"," + task.getStartTime().format(task.getDateTimeFormatter())+","
                + task.getDuration().toString() + "," + task.getEpicID();
    }

    private String taskToString(Epic task) {
        StringBuilder epicIds = new StringBuilder();
        for (int id : task.getMySubtasksID()) {
            epicIds.append(":").append(id);
        }
        if (!epicIds.toString().isEmpty())
            epicIds.deleteCharAt(0);

        return task.getTaskID() + "," + task.getTaskType().toString() + "," + task.getName() + ","
                + task.getStatus() + "," + task.getDescription()+","+ epicIds;
    }

    private static Task fromString(String string) {
        String[] line = string.split(",");
        switch (line[1]) {
            case "EPIC":
                Epic epic = new Epic(line[2], line[4]);
                epic.setTaskID(Integer.parseInt(line[0]));
                epic.setStatus(TaskStatus.valueOf(line[3]));
                if (line.length > 5) {
                    ArrayList<Integer> subtasksOfEpic = new ArrayList<>();
                    String[] ids = line[5].split(":");
                    for (int i = 0; i < ids.length; i++) {
                        subtasksOfEpic.add(Integer.parseInt(ids[i]));
                    }
                    epic.setMySubtasksID(subtasksOfEpic);
                }
                return epic;

            case "SUBTASK":
                Subtask subtask = new Subtask(line[2], line[4], Integer.parseInt(line[7]), TaskStatus.valueOf(line[3]));
                subtask.setTaskID(Integer.parseInt(line[0]));
                subtask.setStartTime(LocalDateTime.parse(line[5], subtask.getDateTimeFormatter()));
                subtask.setDuration(Duration.parse(line[6]));
                return subtask;
            case "TASK":
                Task task = new Task(line[2], line[4], TaskStatus.valueOf(line[3]));
                task.setTaskID(Integer.parseInt(line[0]));
                task.setStartTime(LocalDateTime.parse(line[5], task.getDateTimeFormatter()));
                task.setDuration(Duration.parse(line[6]));
                return task;
            default:
                return null;
        }
    }

    public static FileBackedTasksManager loadFromFile(String path) throws ManagerSaveException {
        FileBackedTasksManager manager = new FileBackedTasksManager(path);
        try (BufferedReader bw = new BufferedReader(new FileReader(path))) {
            while (bw.ready()) {
                String line = bw.readLine();
                if (line.equals("id,type,name,status,description,startTime,duration,epic")) {
                    continue;
                }
                if (!line.isBlank()) {
                    Task newTask = fromString(line);
                    if (newTask != null) {
                        if (newTask.getTaskType().equals(TaskType.EPIC)) {
                            manager.epics.put(newTask.getTaskID(), (Epic) newTask);
                            if(!((Epic) newTask).getMySubtasksID().isEmpty()){
                            newTask.setStartTime(((Epic) newTask).getStartTime(manager.subtasks).get());
                            newTask.setDuration(((Epic) newTask).getDuration(manager.subtasks));
                            ((Epic) newTask).setEndTime(((Epic) newTask).getEndTime(manager.subtasks));
                            }
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
            if (!(history == null) &&!history.isBlank()) {
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
            throw new ManagerSaveException("File not found");
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
        save();
    }

    @Override
    public void addNewTask(Task task) {
        super.addNewTask(task);
        save();
    }

    @Override
    public void addNewEpic(Epic task) {
        super.addNewEpic(task);
        save();
    }

    @Override
    public void removeTaskByID(int id) {
        super.removeTaskByID(id);
        save();
    }

    @Override
    public void removeSubtaskByID(int id) {
        super.removeSubtaskByID(id);
        save();
    }

    @Override
    public void removeEpicByID(int id) {
        super.removeEpicByID(id);
        save();
    }

    @Override
    public void printTaskByID(int id) {
        super.printTaskByID(id);
        save();
    }

    @Override
    public void printSubtaskByID(int id) {
        super.printSubtaskByID(id);
        save();
    }

    @Override
    public void printEpicByID(int id) {
        super.printEpicByID(id);
        save();
    }

    @Override
    public void updateTask(Task task, int id) {
        super.updateTask(task, id);
        save();
    }

    @Override
    public void updateEpic(Epic task, int id) {
        super.updateEpic(task, id);
        save();
    }

    @Override
    public void updateSubtask(Subtask task, int id) {
        super.updateSubtask(task, id);
        save();
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }


    public static void main(String[] args) throws InterruptedException {
        FileBackedTasksManager fileManager1 = (FileBackedTasksManager) Managers.getDefault();
        Task task0 = new Task("a", "a", TaskStatus.NEW,TaskType.TASK,LocalDateTime.now(),10);
        TimeUnit.SECONDS.sleep(1);
        Task task1 = new Task("b", "b", TaskStatus.NEW,TaskType.TASK,LocalDateTime.now(),10);
        TimeUnit.SECONDS.sleep(1);
        Epic epic2 = new Epic("c", "c");
        Epic epic3 = new Epic("d", "d");
        Subtask subtask4 = new Subtask("e", "e", 2, TaskStatus.NEW,LocalDateTime.now(),10);
        TimeUnit.SECONDS.sleep(1);
        Subtask subtask5 = new Subtask("f", "f", 2, TaskStatus.IN_PROGRESS,LocalDateTime.now(),10);
        TimeUnit.SECONDS.sleep(1);
        Subtask subtask6 = new Subtask("g", "g", 2, TaskStatus.NEW,LocalDateTime.now(),10);
        TimeUnit.SECONDS.sleep(1);
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
        //fileManager1.removeEpicByID(3);
        System.out.println("history1:");
        fileManager1.printHistory();
        FileBackedTasksManager fileManager2 = loadFromFile("src/lastSessionSaved.csv");
        System.out.println("history2:");
        fileManager2.printHistory();
        System.out.println("all tasks:");
        fileManager2.printAllTasks();
        fileManager2.printAllEpics();
        fileManager2.printAllSubtasks();
        System.out.println(fileManager2.returnEpicByID(2).getStartTime());
        System.out.println(fileManager1.getPrioritizedTasks());

    }

}