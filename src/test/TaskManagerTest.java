package test;

import manager.TaskManager;
import model.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest <T extends TaskManager>{
    T taskManager;
    Task createNewTask(TaskStatus status){
        return new Task("Test addNewTask", "Test addNewTask description", status);
    }
    Subtask createNewSubtask(int epicId,TaskStatus status){
        return new Subtask("Test addNewTask", "Test addNewTask description", epicId, status);
    }
    Epic createNewEpic(){
        return new Epic("Test addNewTask", "Test addNewTask description");
    }

   @Test
    void addNewTaskTest(){
        Task task = new Task("Test addNewTask", "Test addNewTask description", TaskStatus.NEW);
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
        taskManager.addNewTask(task);
        final int taskId = task.getTaskID();

        final Task savedTask = taskManager.returnTaskByID(taskId);

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
        taskManager.addNewTask(task);
        final int taskId = task.getTaskID();

        final Task savedTask = taskManager.returnTaskByID(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.returnAllSubtasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }
    @Test
    void shouldPrintOneTask(){
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
    void shouldPrintOneSubtask(){
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
    void shouldPrintOneEpic(){
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
    void shouldReturnOneTask(){
        Task task = createNewTask(TaskStatus.NEW);
        taskManager.addNewTask(task);
        List<Task> listOfOneTask = Collections.singletonList(task);
        assertEquals(listOfOneTask,taskManager.returnAllTasks());
    }
    @Test
    void shouldReturnOneEpic(){
        Epic task = createNewEpic();
        taskManager.addNewEpic(task);
        List<Task> listOfOneEpic = Collections.singletonList(task);
        assertEquals(listOfOneEpic,taskManager.returnAllEpics());
    }
    @Test
    void shouldReturnOneSubtask(){
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


    }

