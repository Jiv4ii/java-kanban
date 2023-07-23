package interfaces;

import entities.EpicTask;
import entities.Status;
import entities.SubTask;
import entities.Task;
import logic.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTest {

    private  TaskManager manager;
    private TaskManager emptyManager = new InMemoryTaskManager();

    private  Task testTask = new Task("Task", "Task testring", Status.NEW, Instant.now(), Duration.ofHours(15));
    private  EpicTask testEpic = new EpicTask("TestEpic", "Epic Testing");
    private  SubTask testSub1 = new SubTask("SubTask1", "Sub Testing", Status.NEW, 0, Instant.now(), Duration.ofHours(15));
    private  SubTask testSub2 = new SubTask("SubTask2", "Sub Testing", Status.NEW, 0, Instant.now(), Duration.ofHours(15));

    @BeforeEach
     void create() {
        manager = new InMemoryTaskManager();
        manager.createEpicTask(testEpic);
        manager.createTask(testTask);
        manager.createSubTask(testSub1);
        manager.createSubTask(testSub2);
    }


    @Test
    void showTasksListWhenStandardBehavior() {
        Collection expected = Arrays.asList(testTask);
        Collection real = manager.showTasksList();
        Assertions.assertIterableEquals(expected,real);
    }

    @Test
    void showTasksListWhenEmpty() {
        Collection real = emptyManager.showTasksList();
        Assertions.assertTrue(real.isEmpty());

    }

    @Test
    void showEpicTasksListStandardBehavior() {
        Collection expected = Arrays.asList(testEpic);
        Collection real = manager.showEpicTasksList();
        Assertions.assertIterableEquals(expected,real);
    }

    @Test
    void showEpicTasksListWhenEmpty() {
        Collection real = emptyManager.showEpicTasksList();
        Assertions.assertTrue(real.isEmpty());
    }

    @Test
    void showSubTasksListStandardBehavior() {
        Collection expected = Arrays.asList(testSub1,testSub2);
        Collection real = manager.showSubTasksList();
        Assertions.assertIterableEquals(expected,real);
    }

    @Test
    void showSubTasksListWhenEmpty() {
        Collection real = emptyManager.showSubTasksList();
        Assertions.assertTrue(real.isEmpty());
    }

    @Test
    void deleteAllTasksStandardBehavior() {
        manager.deleteAllTasks();
        Assertions.assertTrue(manager.showTasksList().isEmpty());
    }

    @Test
    void deleteAllTasksStandardWhenEmpty() {
        Assertions.assertTrue(emptyManager.showTasksList().isEmpty());
    }


    @Test
    void deleteAllEpicsStandardBehavior() {
        manager.deleteAllEpics();
        Assertions.assertTrue(manager.showEpicTasksList().isEmpty());
    }

    @Test
    void deleteAllEpicsStandardWhenEmpty() {
        Assertions.assertTrue(emptyManager.showEpicTasksList().isEmpty());
    }

    @Test
    void deleteAllSubTasksStandardBehavior() {
        manager.deleteAllSubTasks();
        Assertions.assertTrue(manager.showSubTasksList().isEmpty());
    }

    @Test
    void deleteAllSubTasksStandardWhenEmpty() {
        Assertions.assertTrue(emptyManager.showSubTasksList().isEmpty());
    }

    @Test
    void getByIdStandardBehavior() {
        Assertions.assertEquals(testTask, manager.getById(1));
    }

    @Test
    void getByIdStandardBehaviorWhenEmpty() {
        Assertions.assertEquals(testTask, emptyManager.getById(1));
    }

    @Test
    void createTaskStandardBehavior() {
        Assertions.assertEquals(testTask, manager.getById(1));
    }

    @Test
    void createEpicTaskStandardBehavior() {
        Assertions.assertEquals(testEpic, manager.getById(0));
    }

    @Test
    void createSubTaskStandardBehavior() {
        Assertions.assertEquals(testSub1, manager.getById(2));
    }

    @Test
    void deleteByIdStandardBehavior() {
        manager.deleteById(1);
        Assertions.assertNull(manager.getById(1));
    }

    @Test
    void updateTaskStandardBehavior() {
        Task updatedTask = new Task(1,"Task", "Task updated", Status.NEW, Instant.now(), Duration.ofHours(15));
        manager.updateTask(updatedTask);
        Assertions.assertEquals(updatedTask, manager.getById(1));
    }

    @Test
    void updateEpicTaskStandardBehavior() {
        Task updatedEpic = new EpicTask(0,"EpicTask", "EpicTask updated");
        manager.updateTask(updatedEpic);
        Assertions.assertEquals(updatedEpic, manager.getById(0));
    }

    @Test
    void updateSubTaskStandardBehavior() {
        Task updatedTask = new SubTask(2,"SubTask1", "Sub Testing", Status.NEW, 0, Instant.now(), Duration.ofHours(15));
        manager.updateTask(updatedTask);
        Assertions.assertEquals(updatedTask, manager.getById(2));
    }

    @Test
    void showEpicsSubTasksStandardBehavior() {
        ArrayList<SubTask> expected = new ArrayList<>();
        expected.addAll(Arrays.asList(testSub1,testSub2));
        EpicTask epic = (EpicTask)manager.getById(0);
        Assertions.assertEquals(expected,manager.showEpicsSubTasks(epic));
    }

/*    @Test
    void getHistoryManager() {
    }*/
}