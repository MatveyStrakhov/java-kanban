import manager.Managers;
import manager.TaskManager;
import model.*;
import server.KVServer;

import java.io.IOException;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) throws IOException {
        KVServer kvServer = new KVServer();
        kvServer.start();
        Task task9 = new Task("9", "update test", TaskStatus.NEW,TaskType.TASK, LocalDateTime.of(2021,6,15,10,6,0),10);
        Task task0 = new Task("a", "a", TaskStatus.NEW,TaskType.TASK,LocalDateTime.of(2021,6,15,11,6,0),10);
        Task task1 = new Task("b", "b", TaskStatus.NEW,TaskType.TASK,LocalDateTime.of(2021,6,15,12,6,0),10);
        Epic epic2 = new Epic("c", "c");
        Epic epic3 = new Epic("d", "d");
        Subtask subtask4 = new Subtask("e", "e", 2, TaskStatus.NEW,LocalDateTime.of(2021,6,15,13,6,0),10);
        Subtask subtask5 = new Subtask("f", "f", 2, TaskStatus.IN_PROGRESS,LocalDateTime.of(2021,6,15,14,6,0),10);
        Subtask subtask6 = new Subtask("g", "g", 2, TaskStatus.NEW,LocalDateTime.of(2021,6,15,15,6,0),10);
        TaskManager taskManager = Managers.getDefault();
        assert taskManager != null;
        taskManager.addNewTask(task0);
        taskManager.addNewTask(task1);
        taskManager.addNewEpic(epic2);
        taskManager.addNewEpic(epic3);
        taskManager.addNewSubtask(subtask4, 2);
        taskManager.addNewSubtask(subtask5, 2);
        taskManager.addNewSubtask(subtask6, 2);
        System.out.println("Поехали!");
        System.out.println("History");
        taskManager.printHistory();
        System.out.println("all tasks");
        taskManager.printAllEpics();
        taskManager.printAllTasks();
        taskManager.printAllSubtasks();
        System.out.println("deleted epic");
        taskManager.removeEpicByID(3);
        taskManager.printAllEpics();
        kvServer.stop();
    }
}
