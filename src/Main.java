public class Main {

    public static void main(String[] args) {
        Task task1 = new Task("a", "a", "NEW");
        Task task2 = new Task("b", "b", "NEW");
        Epic epic1 = new Epic("c", "c");
        Epic epic2 = new Epic("d", "d");
        Epic epic3 = new Epic("updated", "updated");
        Subtask subtask1 = new Subtask("e", "e", 2, "NEW");
        Subtask subtask2 = new Subtask("e", "e", 2, "IN_PROGRESS");
        Subtask subtask3 = new Subtask("e", "e", 3, "NEW");
        TaskManager taskManager = new TaskManager();
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        taskManager.addNewEpic(epic1);
        taskManager.addNewEpic(epic2);
        taskManager.addNewSubtask(subtask1, 2);
        taskManager.addNewSubtask(subtask2, 2);
        taskManager.addNewSubtask(subtask3, 3);
        taskManager.printAllTasks();
        taskManager.printAllEpics();
        taskManager.printAllSubtasks();
        taskManager.printEpicByID(2);
        taskManager.updateEpic(epic3, 2);
        taskManager.printSubtasksByEpicID(2);
        Subtask subtask4 = new Subtask("e", "e", 2, "NEW");
        taskManager.updateSubtask(subtask4, 5);
        taskManager.printEpicByID(2);
        System.out.println("Поехали!");
    }
}
