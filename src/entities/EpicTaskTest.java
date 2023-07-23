package entities;

import logic.FileBackedTasksManager;
import logic.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class EpicTaskTest {

    private static InMemoryTaskManager manager;

    @BeforeAll
    static void createManager() {
        manager = new InMemoryTaskManager();
    }


    @Test
    private void shouldReturnNewWhenSubTasksListEmpty() {
        EpicTask epic = new EpicTask("TestEpic", "Epic Testing");
        Assertions.assertEquals(Status.NEW, epic.status);
    }


    @Test
    private void shouldReturnNewWhenSubTasksListNew() {
        EpicTask epic = new EpicTask("TestEpic", "Epic Testing");
        SubTask testSub1 = new SubTask("SubTask1", "Sub Testing", Status.NEW, 0, Instant.now(), Duration.ofHours(15));
        SubTask testSub2 = new SubTask("SubTask2", "Sub Testing", Status.NEW, 0, Instant.now(), Duration.ofDays(2));
        manager.createEpicTask(epic);
        manager.createSubTask(testSub1);
        manager.createSubTask(testSub2);
        Assertions.assertEquals(Status.NEW, epic.status);
    }

    @Test
    private void shouldReturnNewWhenSubTasksListDone() {
        EpicTask epic = new EpicTask("TestEpic", "Epic Testing");
        SubTask testSub1 = new SubTask("SubTask1", "Sub Testing", Status.NEW, 0, Instant.now(), Duration.ofHours(15));
        SubTask testSub2 = new SubTask("SubTask2", "Sub Testing", Status.NEW, 0, Instant.now(), Duration.ofDays(2));
        manager.createEpicTask(epic);
        manager.createSubTask(testSub1);
        manager.createSubTask(testSub2);
        Assertions.assertEquals(Status.DONE, epic.status);
    }

    @Test
    private void shouldReturnNewWhenSubTasksListNewAndDone() {
        EpicTask epic = new EpicTask("TestEpic", "Epic Testing");
        SubTask testSub1 = new SubTask("SubTask1", "Sub Testing", Status.NEW, 0, Instant.now(), Duration.ofHours(15));
        SubTask testSub2 = new SubTask("SubTask2", "Sub Testing", Status.NEW, 0, Instant.now(), Duration.ofDays(2));
        manager.createEpicTask(epic);
        manager.createSubTask(testSub1);
        manager.createSubTask(testSub2);
        Assertions.assertEquals(Status.IN_PROGRESS, epic.status);
    }

    @Test
    private void shouldReturnNewWhenSubTasksListNewAndInProgress() {
        EpicTask epic = new EpicTask("TestEpic", "Epic Testing");
        SubTask testSub1 = new SubTask("SubTask1", "Sub Testing", Status.NEW, 0, Instant.now(), Duration.ofHours(15));
        SubTask testSub2 = new SubTask("SubTask2", "Sub Testing", Status.NEW, 0, Instant.now(), Duration.ofDays(2));
        manager.createEpicTask(epic);
        manager.createSubTask(testSub1);
        manager.createSubTask(testSub2);
        Assertions.assertEquals(Status.IN_PROGRESS, epic.status);
    }


}