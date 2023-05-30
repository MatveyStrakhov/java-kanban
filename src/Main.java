import manager.FileBackedTasksManager;
import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import model.*;

public class Main {

    public static void main(String[] args) {
        Task task0 = new Task("a", "a", TaskStatus.NEW);
        Task task1 = new Task("b", "b", TaskStatus.NEW);
        Epic epic2 = new Epic("c", "c");
        Epic epic3 = new Epic("d", "d");
        Subtask subtask4 = new Subtask("e", "e", 2, TaskStatus.NEW);
        Subtask subtask5 = new Subtask("e", "e", 2, TaskStatus.IN_PROGRESS);
        Subtask subtask6 = new Subtask("e", "e", 2, TaskStatus.NEW);
        TaskManager taskManager = Managers.getDefault();
        taskManager.addNewTask(task0);
        taskManager.addNewTask(task1);
        taskManager.addNewEpic(epic2);
        taskManager.addNewEpic(epic3);
        taskManager.addNewSubtask(subtask4, 2);
        taskManager.addNewSubtask(subtask5, 2);
        taskManager.addNewSubtask(subtask6, 2);
        System.out.println("Поехали!");
        taskManager.printTaskByID(0);
        taskManager.printTaskByID(1);
        taskManager.printEpicByID(2);
        taskManager.printEpicByID(3);
        taskManager.printEpicByID(3);
        taskManager.printSubtaskByID(4);
        taskManager.printSubtaskByID(5);
        taskManager.printSubtaskByID(6);
        taskManager.printTaskByID(0);
        taskManager.removeEpicByID(3);
        System.out.println("History");
        taskManager.printHistory();
        //taskManager.removeAllEpics();
        System.out.println("History");
        taskManager.printHistory();
        InMemoryTaskManager manager = new FileBackedTasksManager("src/lastSessionSaved.csv");
        manager.addNewTask(task0);
    }
}
