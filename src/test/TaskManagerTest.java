package test;

import manager.TaskManager;
import model.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest <T extends TaskManager>{
    T taskManager;
    Task createNewTask(TaskStatus status){
        return new Task("Test", "Test description", status);
    }
    Subtask createNewSubtask(int epicId,TaskStatus status){
        return new Subtask("Test", "Test description", epicId, status);
    }
    Epic createNewEpic(){
        return new Epic("Test", "Test description");
    }

   @Test
    void addNewTaskTest(){
        Task task = new Task("Test", "Test description", TaskStatus.NEW);
        taskManager.addNewTask(task);
        final int taskId = task.getTaskID();

        final Task savedTask = taskManager.returnTaskByID(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.returnAllTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }
    @Test
    void addNewEpicTest(){
        Epic task = createNewEpic();
        taskManager.addNewEpic(task);
        final int taskId = task.getTaskID();

        final Epic savedTask = taskManager.returnEpicByID(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.returnAllEpics();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }
    @Test
    void addNewSubtaskTest(){
        Subtask task = createNewSubtask(0,TaskStatus.NEW);
        Epic epic = createNewEpic();
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(task,0);
        final int taskId = task.getTaskID();

        final Subtask savedTask = taskManager.returnSubtaskByID(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.returnAllSubtasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }
    @Test
    void shouldPrintAllTasks(){
        Task task = createNewTask(TaskStatus.NEW);
        taskManager.addNewTask(task);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        taskManager.printAllTasks();
        assertEquals(task.toString()+"\r\n",out.toString());
    }
    @Test
    void shouldPrintNothingForTasks(){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        taskManager.printAllTasks();
        assertEquals("",out.toString());
    }
    @Test
    void shouldPrintAllSubtasks(){
        Epic epic = createNewEpic();
        Subtask task = createNewSubtask(0,TaskStatus.NEW);
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(task,0);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        taskManager.printAllSubtasks();
        assertEquals(task.toString()+"\r\n",out.toString());
    }
    @Test
    void shouldPrintNothingForSubtasks(){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        taskManager.printAllSubtasks();
        assertEquals("",out.toString());
    }
    @Test
    void shouldPrintAllEpics(){
        Epic task = createNewEpic();
        taskManager.addNewEpic(task);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        taskManager.printAllEpics();
        assertEquals(task.toString()+"\r\n",out.toString());
    }
    @Test
    void shouldPrintNothingForEpics(){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        taskManager.printAllEpics();
        assertEquals("",out.toString());
    }
    @Test
    void shouldReturnAllTasks(){
        Task task = createNewTask(TaskStatus.NEW);
        taskManager.addNewTask(task);
        List<Task> listOfOneTask = Collections.singletonList(task);
        assertEquals(listOfOneTask,taskManager.returnAllTasks());
    }
    @Test
    void shouldReturnAllEpics(){
        Epic task = createNewEpic();
        taskManager.addNewEpic(task);
        List<Task> listOfOneEpic = Collections.singletonList(task);
        assertEquals(listOfOneEpic,taskManager.returnAllEpics());
    }
    @Test
    void shouldReturnAllSubtasks(){
        Subtask task = createNewSubtask(0,TaskStatus.NEW);
        Epic epic = createNewEpic();
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(task,0);
        List<Task> listOfOneTask = Collections.singletonList(task);
        assertEquals(listOfOneTask,taskManager.returnAllSubtasks());
    }
    @Test
    void shouldReturnNothingForTaskSubtaskEpic(){
        List<Task> emptyList = Collections.emptyList();
        assertEquals(emptyList,taskManager.returnAllSubtasks());
        assertEquals(emptyList,taskManager.returnAllEpics());
        assertEquals(emptyList,taskManager.returnAllTasks());
    }
    @Test
    void shouldReturnNullAfterRemovingOneTask(){
        Task task = createNewTask(TaskStatus.NEW);
        taskManager.addNewTask(task);
        taskManager.removeTaskByID(0);
        assertNull(taskManager.returnTaskByID(0));
    }
    @Test
    void shouldReturnNullAfterRemovingOneEpic(){
        Epic task = createNewEpic();
        taskManager.addNewEpic(task);
        taskManager.removeEpicByID(0);
        assertNull(taskManager.returnEpicByID(0));
    }
    @Test
    void shouldReturnNullAfterRemovingOneSubtask(){
        Epic task = createNewEpic();
        taskManager.addNewEpic(task);
        Subtask subtask = createNewSubtask(0,TaskStatus.NEW);
        taskManager.addNewSubtask(subtask,0);
        taskManager.removeSubtaskByID(1);
        assertNull(taskManager.returnSubtaskByID(1));
    }
    @Test
    void shouldReturnNullAfterRemovingAllTasks(){
        Task task = createNewTask(TaskStatus.NEW);
        taskManager.addNewTask(task);
        taskManager.removeAllTasks();
        assertNull(taskManager.returnTaskByID(0));
    }
    @Test
    void shouldReturnNullAfterRemovingAllEpics(){
        Epic task = createNewEpic();
        taskManager.addNewEpic(task);
        taskManager.removeAllEpics();
        assertNull(taskManager.returnEpicByID(0));
    }
    @Test
    void shouldReturnNullAfterRemovingAllSubtasks(){
        Epic epic = createNewEpic();
        taskManager.addNewEpic(epic);
        Subtask subtask = createNewSubtask(epic.getTaskID(), TaskStatus.NEW);
        taskManager.addNewSubtask(subtask, epic.getTaskID());
        taskManager.removeAllSubtasks();
        assertNull(taskManager.returnSubtaskByID(subtask.getTaskID()));
    }
    @Test
    void shouldUpdateTask(){
        Task task1 = createNewTask(TaskStatus.NEW);
        Task task2 = createNewTask(TaskStatus.IN_PROGRESS);
        taskManager.addNewTask(task1);
        taskManager.updateTask(task2,task1.getTaskID());
        assertEquals(task2, taskManager.returnTaskByID(task1.getTaskID()));
    }
    @Test
    void shouldUpdateEpic(){
        Epic epic1 = createNewEpic();
        Epic epic2 = createNewEpic();
        taskManager.addNewEpic(epic1);
        taskManager.updateEpic(epic2,epic1.getTaskID());
        assertEquals(epic2, taskManager.returnEpicByID(epic1.getTaskID()));
    }
    @Test
    void shouldUpdateSubtask(){
        Epic epic = createNewEpic();
        Subtask subtask1 = createNewSubtask(0,TaskStatus.IN_PROGRESS);
        Subtask subtask2 = createNewSubtask(0,TaskStatus.NEW);
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask1,0);
        taskManager.updateSubtask(subtask2, subtask1.getTaskID());
        assertEquals(subtask2, taskManager.returnSubtaskByID(subtask1.getTaskID()));
    }
    @Test
    void shouldReturnCorrectTask(){
        Task task = createNewTask(TaskStatus.NEW);
        taskManager.addNewTask(task);
        int taskId = task.getTaskID();
        assertEquals(task,taskManager.returnTaskByID(taskId));
    }
    @Test
    void shouldReturnCorrectEpic(){
        Epic epic = createNewEpic();
        taskManager.addNewEpic(epic);
        int taskId = epic.getTaskID();
        assertEquals(epic,taskManager.returnEpicByID(taskId));
    }
    @Test
    void shouldReturnCorrectSubtask(){
        Epic epic = createNewEpic();
        Subtask subtask = createNewSubtask(epic.getTaskID(),TaskStatus.NEW);
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask,epic.getTaskID());
        int taskId = subtask.getTaskID();
        assertEquals(subtask,taskManager.returnSubtaskByID(taskId));
    }
    @Test
    void shouldReturnNullWhenIncorrectID(){
        assertNull(taskManager.returnSubtaskByID(0));
        assertNull(taskManager.returnTaskByID(0));
        assertNull(taskManager.returnEpicByID(0));
    }
    @Test
    void shouldPrintTask(){
        Task task = createNewTask(TaskStatus.NEW);
        taskManager.addNewTask(task);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        taskManager.printTaskByID(task.getTaskID());
        assertEquals(task.toString()+"\r\n",out.toString());
    }
    @Test
    void shouldPrintNothingForTask(){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        taskManager.printTaskByID(0);
        assertEquals("",out.toString());
    }
    @Test
    void shouldPrintSubtask(){
        Epic epic = createNewEpic();
        Subtask task = createNewSubtask(0,TaskStatus.NEW);
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(task,0);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        taskManager.printSubtaskByID(task.getTaskID());
        assertEquals(task.toString()+"\r\n",out.toString());
    }
    @Test
    void shouldPrintNothingForSubtask(){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        taskManager.printSubtaskByID(0);
        assertEquals("",out.toString());
    }
    @Test
    void shouldPrintEpic(){
        Epic task = createNewEpic();
        taskManager.addNewEpic(task);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        taskManager.printEpicByID(task.getTaskID());
        assertEquals(task.toString()+"\r\n",out.toString());
    }
    @Test
    void shouldPrintNothingForEpic(){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        taskManager.printEpicByID(0);
        assertEquals("",out.toString());
    }
    @Test
    void shouldReturnCorrectHistory(){
        Task task1 = createNewTask(TaskStatus.NEW);
        taskManager.addNewTask(task1);
        Task task2 = taskManager.returnTaskByID(task1.getTaskID());
        List<Task> history = Collections.singletonList(task2);
        assertEquals(history, taskManager.getHistory());
    }
    @Test
    void shouldReturnEmptyHistory(){
        Task task1 = createNewTask(TaskStatus.NEW);
        taskManager.addNewTask(task1);
        List<Task> history = Collections.emptyList();
        assertEquals(history, taskManager.getHistory());
    }
    @Test
    void shouldPrintCorrectHistory(){
        Task task1 = createNewTask(TaskStatus.NEW);
        taskManager.addNewTask(task1);
        Task task2 = taskManager.returnTaskByID(task1.getTaskID());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        taskManager.printHistory();
        assertEquals(task2.toString()+"\r\n",out.toString());

    }
    @Test
    void shouldPrintNothingIfHistoryIsAbsent(){
        Task task1 = createNewTask(TaskStatus.NEW);
        taskManager.addNewTask(task1);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        taskManager.printHistory();
        assertEquals("",out.toString());
    }
    @Test
    void shouldPrintCorrectSubtasksByEpicID() {
        Epic epic = createNewEpic();
        taskManager.addNewEpic(epic);
        Subtask subtask1 = createNewSubtask(epic.getTaskID(), TaskStatus.NEW);
        Subtask subtask2 = createNewSubtask(epic.getTaskID(), TaskStatus.IN_PROGRESS);
        taskManager.addNewSubtask(subtask1,epic.getTaskID());
        taskManager.addNewSubtask(subtask2,epic.getTaskID());
        List<Task> subtasks = new ArrayList<>();
        subtasks.add(subtask1);
        subtasks.add(subtask2);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        taskManager.printSubtasksByEpicID(epic.getTaskID());
        assertEquals(subtasks.toString().replaceAll("}, ","}\r\n")
                .replace("[","").replace("]","\r\n"),out.toString());
    }
    @Test
    void shouldPrintNothingIfThereIsNoSubtasks(){
        Epic epic = createNewEpic();
        taskManager.addNewEpic(epic);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        taskManager.printSubtasksByEpicID(epic.getTaskID());
        assertEquals("",out.toString());
    }
    @Test
    void shouldCheckStatusOfEpic(){
        Epic epic = createNewEpic();
        taskManager.addNewEpic(epic);
        assertEquals(epic.getStatus(),TaskStatus.NEW);
        Subtask subtask1 = createNewSubtask(epic.getTaskID(), TaskStatus.IN_PROGRESS);
        taskManager.addNewSubtask(subtask1,epic.getTaskID());
        taskManager.checkStatusOfEpic(epic);
        assertEquals(epic.getStatus(),TaskStatus.IN_PROGRESS);
    }
    @Test
    void shouldReturnEmptyPrioritizedTasks(){
        List<Task> emptyList = Collections.emptyList();
        assertEquals(emptyList,taskManager.getPrioritizedTasks());
    }
    @Test
    void shouldReturnFilledPrioritizedList(){
        List<Task> filledList = new ArrayList<>();
        Task task0 = new Task("a", "a", TaskStatus.NEW,TaskType.TASK, LocalDateTime.of(2021,6,15,11,6,0),10);
        Task task1 = new Task("b", "b", TaskStatus.NEW,TaskType.TASK,LocalDateTime.of(2021,6,15,12,6,0),10);
        filledList.add(task0);
        filledList.add(task1);
        taskManager.addNewTask(task0);
        taskManager.addNewTask(task1);
        assertEquals(filledList,taskManager.getPrioritizedTasks());
    }



    }

