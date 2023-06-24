package manager;

import exception.ManagerSaveException;
import model.*;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;


public class FileBackedTasksManager extends InMemoryTaskManager {
    private String path;

    public FileBackedTasksManager(String path) {
        this.path = path;
    }
    public FileBackedTasksManager() {
    }

    protected void save() throws ManagerSaveException {
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

    protected static String historyToString(HistoryManager manager) {
        return manager.toString();
    }

    private String taskToString(Task task) {
        return task.getTaskID() + "," + task.getTaskType().toString() + "," + task.getName() + ","
                + task.getStatus() + "," + task.getDescription()+"," + task.getStartTime().format(Task.getDateTimeFormatter())+"," + task.getDuration().toString();
    }

    private String taskToString(Subtask task) {
        return task.getTaskID() + "," + task.getTaskType().toString() + "," + task.getName() + ","
                + task.getStatus() + "," + task.getDescription()+"," + task.getStartTime().format(Task.getDateTimeFormatter())+","
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

    protected static Task fromString(String string) {
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
                subtask.setStartTime(LocalDateTime.parse(line[5], Task.getDateTimeFormatter()));
                subtask.setDuration(Duration.parse(line[6]));
                return subtask;
            case "TASK":
                Task task = new Task(line[2], line[4], TaskStatus.valueOf(line[3]));
                task.setTaskID(Integer.parseInt(line[0]));
                task.setStartTime(LocalDateTime.parse(line[5], Task.getDateTimeFormatter()));
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
                            if(!((Epic) newTask).getMySubtasksID().isEmpty()&((Epic)newTask).getStartTime(manager.subtasks).isPresent()){
                            newTask.setStartTime(((Epic) newTask).getStartTime(manager.subtasks).get());
                            newTask.setDuration(((Epic) newTask).getDuration(manager.subtasks));
                            ((Epic) newTask).setEndTime(((Epic) newTask).getEndTime(manager.subtasks));
                            }
                            else{
                                newTask.setStartTime(null);
                                newTask.setDuration(null);
                                ((Epic) newTask).setEndTime(null);
                            }
                            manager.sortedTasks.add(newTask);
                        } else if (newTask.getTaskType().equals(TaskType.TASK)) {
                            manager.tasks.put(newTask.getTaskID(), newTask);
                            manager.sortedTasks.add(newTask);
                        } else if (newTask.getTaskType().equals(TaskType.SUBTASK)) {
                            manager.subtasks.put(newTask.getTaskID(), (Subtask) newTask);
                            manager.sortedTasks.add(newTask);
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

    private static List<Integer> historyFromString(String history) {
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
    public boolean removeTaskByID(int id) {
        if(super.removeTaskByID(id)){
            save();
            return true;
        }
        else {return false;}


    }

    @Override
    public boolean removeSubtaskByID(int id) {
        if(super.removeSubtaskByID(id)){
        save();
        return true;}
        else{return false;}

    }

    @Override
    public boolean removeEpicByID(int id) {
        if(super.removeEpicByID(id)){
        save();
        return true;}
        else{return false;}
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

}