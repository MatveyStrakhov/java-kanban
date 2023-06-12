package test;

import manager.FileBackedTasksManager;
import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager>{
    @BeforeEach
    void beforeEachTest(){
        taskManager = new InMemoryTaskManager();
    }

}
