package test;


import manager.InMemoryTaskManager;
import manager.TaskManager;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EpicTest {
    TaskManager historyManager;
    private Epic blankEpic;
    private Subtask NEWSubtask;
    private Subtask IN_PROGRESSSubtask;
    private Subtask DONESubtask;
    @BeforeEach
     void beforeEach(){
        historyManager = new InMemoryTaskManager();
        blankEpic = new Epic("name","description");
        historyManager.addNewEpic(blankEpic);
        NEWSubtask = new Subtask("name","description",0,TaskStatus.NEW);
        IN_PROGRESSSubtask = new Subtask("name","description",0,TaskStatus.IN_PROGRESS);
        DONESubtask = new Subtask("name","description",0,TaskStatus.DONE);
    }
    @Test
    public void shouldHaveNEWStatusWhileZeroSubtasks(){
        Assertions.assertEquals(TaskStatus.NEW, blankEpic.getStatus());
    }
    @Test
    public void shouldHaveNEWStatusWhileNEWSubtasks(){
        historyManager.addNewSubtask(NEWSubtask,0);
        Assertions.assertEquals(TaskStatus.NEW, blankEpic.getStatus());
    }
    @Test
    public void shouldHaveDONEStatusWhileDONESubtasks(){
        historyManager.addNewSubtask(DONESubtask,0);
        Assertions.assertEquals(TaskStatus.DONE, blankEpic.getStatus());

    }
    @Test
    public void shouldHaveNEWStatusWhileDONEAndNEWSubtasks(){
        historyManager.addNewSubtask(DONESubtask,0);
        historyManager.addNewSubtask(NEWSubtask,0);
        Assertions.assertEquals(TaskStatus.NEW, blankEpic.getStatus());

    }
    @Test
    public void shouldHaveIN_PROGRESSStatusWhileIN_PROGRESSSubtasks(){
        historyManager.addNewSubtask(IN_PROGRESSSubtask,0);
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, blankEpic.getStatus());

    }

}