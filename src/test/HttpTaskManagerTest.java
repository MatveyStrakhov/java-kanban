package test;

import exception.ManagerSaveException;
import manager.HttpTaskManager;
import manager.Managers;
import model.Epic;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.KVServer;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager>{
    KVServer server;
    @BeforeEach
    void beforeEach() throws IOException {
        server = new KVServer();
        server.start();
        taskManager = (HttpTaskManager) Managers.getDefault();
    }
    @AfterEach
    void afterEach(){
        KVServer.stop();
    }
    @Test
    void shouldLoadNoTasksFromEmptyServer(){
        assertEquals(Collections.emptyList(),taskManager.returnAllTasks());
        assertEquals(Collections.emptyList(),taskManager.returnAllSubtasks());
        assertEquals(Collections.emptyList(),taskManager.returnAllEpics());
        assertEquals(Collections.emptyList(),taskManager.getHistory());
    }
    @Test
    void shouldLoadEpicAndHistoryFromPrefilledFile(){
        taskManager.addNewEpic( new Epic("d", "d"));
        taskManager.returnEpicByID(0);
        assertEquals(Collections.emptyList(),taskManager.returnAllTasks());
        assertEquals(Collections.emptyList(),taskManager.returnAllSubtasks());
        assertNotEquals(Collections.emptyList(),taskManager.returnAllEpics());
        assertEquals(1,taskManager.getHistory().size());
    }
}

